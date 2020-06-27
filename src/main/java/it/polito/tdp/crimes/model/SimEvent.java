package it.polito.tdp.crimes.model;

import java.time.LocalTime;

public class SimEvent implements Comparable<SimEvent> {

	public enum SimEventType {
		NUOVO_CRIMINE, CRIMINE_GESTITO, CRIMINE_MAL_GESTITO
	}

	private LocalTime time;
	private SimEventType type;
	private Event crimine;
	private Agente agente;

	/**
	 * @param time
	 * @param type
	 * @param crimine
	 * @param agente
	 */
	public SimEvent(LocalTime time, SimEventType type, Event crimine, Agente agente) {
		super();
		this.time = time;
		this.type = type;
		this.crimine = crimine;
		this.agente = agente;
	}

	public LocalTime getTime() {
		return time;
	}

	public Event getCrimine() {
		return crimine;
	}

	public Agente getAgente() {
		return agente;
	}

	public SimEventType getType() {
		return type;
	}

	@Override
	public int compareTo(SimEvent other) {

		return this.time.compareTo(other.time);
	}

}