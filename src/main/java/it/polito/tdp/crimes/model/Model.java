package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private Graph<Integer, DefaultWeightedEdge> graph;
	private Simulator sim;

	public List<Integer> getAnni() {
		return EventsDao.getAnni();
	}

	public void createGraph(Integer year) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		List<Coord> coord = EventsDao.getDistrictCoordByYear(year);

		for (Coord c : coord) {
			this.graph.addVertex(c.getId());
		}

		for (Coord c : coord) {
			for (Coord c2 : coord) {
				if (!c.equals(c2) && !this.graph.containsEdge(c.getId(), c2.getId())) {
					Double weight = LatLngTool.distance(c.getCoord(), c2.getCoord(), LengthUnit.KILOMETER);
					this.graph.setEdgeWeight(this.graph.addEdge(c.getId(), c2.getId()), weight);
				}
			}
		}

		System.out.println(true);
	}

	public Map<Integer, List<DistrictLength>> getConnections() {

		Map<Integer, List<DistrictLength>> mappa = new HashMap<>();

		for (Integer i : this.graph.vertexSet()) {
			mappa.put(i, new ArrayList<DistrictLength>());
			for (int j = 1; j < 8; j++) {
				if (i != j) {
					mappa.get(i).add(new DistrictLength(j, this.graph.getEdgeWeight(this.graph.getEdge(i, j))));
				}
			}
			mappa.get(i).sort(null);
		}

		return mappa;
	}

	public List<Integer> getMesi(Integer year) {
		return EventsDao.getMesi(year);
	}

	public List<Integer> getGiorni(Integer anno) {
		return EventsDao.getGiorni(anno);
	}

	public boolean simula(Integer giorno, Integer mese, Integer anno, Integer agenti) {

		sim = new Simulator();
		sim.setNumeroAgenti(agenti);
		List<Event> crimini = EventsDao.listEventsByDay(giorno, mese, anno);
		if (crimini.size() == 0) {
			return false;
		}
		Integer start = EventsDao.getDistrettoMigliore(anno);
		this.sim.init(this.graph, crimini, start);
		this.sim.run();
		return true;

	}

	public Integer getMalGestiti() {
		return sim.getMalGestiti();
	}

}
