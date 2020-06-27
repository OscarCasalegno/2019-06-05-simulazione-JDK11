package it.polito.tdp.crimes.model;

public class DistrictLength implements Comparable<DistrictLength> {

	private Integer id;
	private Double length;

	/**
	 * @param id
	 * @param length
	 */
	public DistrictLength(Integer id, Double length) {
		super();
		this.id = id;
		this.length = length;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((length == null) ? 0 : length.hashCode());
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
		DistrictLength other = (DistrictLength) obj;
		if (length == null) {
			if (other.length != null) {
				return false;
			}
		} else if (!length.equals(other.length)) {
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

	@Override
	public int compareTo(DistrictLength other) {
		return this.length.compareTo(other.length);
	}

	@Override
	public String toString() {
		return "Distanza da distretto " + id + ": " + length;
	}

}
