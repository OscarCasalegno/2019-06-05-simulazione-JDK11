package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.SimEvent.SimEventType;

public class Simulator {

	// TODO PARAMETRI DI SIMULAZIONE
	private Integer numeroAgenti = 1; // parametro modificabile

	// TODO OUTPUT DA CALCOLARE
	private Integer malGestiti = 0;

	// TODO STATO DEL SISTEMA
	private Graph<Integer, DefaultWeightedEdge> graph;
	private List<Agente> agenti;// spesso presente lista
	private Integer agentiOccupati = 0;
	private PriorityQueue<SimEvent> attesa;

	// TODO CODA DEGLI EVENTI
	private PriorityQueue<SimEvent> queue;// coda, sempre presente

	// TODO INIZIALIZZAZIONE
	public void init(Graph<Integer, DefaultWeightedEdge> graph, List<Event> crimini, Integer start) {
		// coda
		this.queue = new PriorityQueue<>();

		// stato del sistema
		this.agenti = new ArrayList<>();
		this.graph = graph;
		this.attesa = new PriorityQueue<>();

		for (int i = 0; i < this.numeroAgenti; i++) {
			this.agenti.add(new Agente(start));
		}

		// output
		this.malGestiti = 0; // presumibilmente si conta qualcosa

		// generiamo eventi iniziali

		for (Event e : crimini) {
			queue.add(new SimEvent(e.getReported_date().toLocalTime(), SimEventType.NUOVO_CRIMINE, e, null));
		}

	}

	// TODO getter output

	public Integer getMalGestiti() {
		return this.malGestiti;
	}

	// TODO ESECUZIONE
	public void run() {
		// presumibilmente si può lasciare così
		while (!this.queue.isEmpty()) {
			SimEvent e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(SimEvent e) {

		switch (e.getType()) {
		case NUOVO_CRIMINE:
			if (this.agentiOccupati != this.numeroAgenti) {
				Agente a = this.selezionaAgente(e.getCrimine());
				Duration tempoViaggio = this.calcolaTempoViaggio(a, e.getCrimine());
				Duration tempoGestione = this.generaTempoGestione(e.getCrimine());
				Duration tempoTotale = tempoViaggio.plus(tempoGestione);
				if (tempoViaggio.compareTo(Duration.ofMinutes(15)) <= 0) {
					this.queue.add(new SimEvent(e.getTime().plus(tempoTotale), SimEventType.CRIMINE_GESTITO,
							e.getCrimine(), a));

				} else {
					this.queue.add(new SimEvent(e.getTime().plus(tempoTotale), SimEventType.CRIMINE_MAL_GESTITO,
							e.getCrimine(), a));

				}
				a.setOccupato(true);
				a.setPosizione(e.getCrimine().getDistrict_id());
				this.agentiOccupati++;
			} else {
				attesa.add(e);
			}
			break;

		case CRIMINE_MAL_GESTITO:
			this.malGestiti++;

		case CRIMINE_GESTITO:
			this.agentiOccupati--;
			e.getAgente().setOccupato(false);
			if (!attesa.isEmpty()) {
				SimEvent s = attesa.poll();
				Agente a = e.getAgente();
				Duration tempoViaggio = this.calcolaTempoViaggio(a, e.getCrimine())
						.plus(Duration.between(s.getTime(), e.getTime()));
				Duration tempoGestione = this.generaTempoGestione(e.getCrimine());
				Duration tempoTotale = tempoViaggio.plus(tempoGestione);
				if (tempoViaggio.compareTo(Duration.ofMinutes(15)) <= 0) {
					this.queue.add(new SimEvent(e.getTime().plus(tempoTotale), SimEventType.CRIMINE_GESTITO,
							e.getCrimine(), a));
				} else {
					this.queue.add(new SimEvent(e.getTime().plus(tempoTotale), SimEventType.CRIMINE_MAL_GESTITO,
							e.getCrimine(), a));
				}
				a.setOccupato(true);
				a.setPosizione(e.getCrimine().getDistrict_id());
				this.agentiOccupati++;

			}
			break;

		}

	}

	private Duration generaTempoGestione(Event crimine) {
		String tipo = crimine.getOffense_category_id();
		if (tipo.contentEquals("all_other_crimes")) {
			if (Math.random() < 0.5) {
				return Duration.ofMinutes(60);
			} else {
				return Duration.ofMinutes(120);
			}
		}
		return Duration.ofMinutes(120);
	}

	private Duration calcolaTempoViaggio(Agente a, Event crimine) {
		if (a.getPosizione() == crimine.getDistrict_id()) {
			return Duration.ofMinutes(0);
		}
		Double distanza = this.graph.getEdgeWeight(this.graph.getEdge(a.getPosizione(), crimine.getDistrict_id()));

		return Duration.ofMinutes(distanza.longValue());

	}

	private Agente selezionaAgente(Event crimine) {
		Agente best = null;
		Double bestD = null;

		for (Agente a : this.agenti) {
			if (!a.isOccupato()) {
				if (a.getPosizione() == crimine.getDistrict_id()) {
					return a;
				}
				Double distanza = this.graph
						.getEdgeWeight(this.graph.getEdge(crimine.getDistrict_id(), a.getPosizione()));
				if (best == null || bestD > distanza) {
					best = a;
					bestD = distanza;
				}
			}
		}
		return best;
	}

	public Integer getNumeroAgenti() {
		return numeroAgenti;
	}

	public void setNumeroAgenti(Integer numeroAgenti) {
		this.numeroAgenti = numeroAgenti;
	}

}
