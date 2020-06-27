package it.polito.tdp.crimes.model;

public class Agente {

	Integer posizione;
	boolean occupato;

	/**
	 * @param posizione
	 */
	public Agente(Integer posizione) {
		super();
		this.posizione = posizione;
		this.occupato = false;
	}

	public Integer getPosizione() {
		return posizione;
	}

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	public boolean isOccupato() {
		return occupato;
	}

	public void setOccupato(boolean occupato) {
		this.occupato = occupato;
	}

}
