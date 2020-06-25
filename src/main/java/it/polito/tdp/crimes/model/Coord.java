package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class Coord {

	private Integer id;
	private LatLng coord;

	/**
	 * @param id
	 * @param coord
	 */
	public Coord(Integer id, LatLng coord) {
		super();
		this.id = id;
		this.coord = coord;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LatLng getCoord() {
		return coord;
	}

	public void setCoord(LatLng coord) {
		this.coord = coord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Coord other = (Coord) obj;
		if (coord == null) {
			if (other.coord != null) {
				return false;
			}
		} else if (!coord.equals(other.coord)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
