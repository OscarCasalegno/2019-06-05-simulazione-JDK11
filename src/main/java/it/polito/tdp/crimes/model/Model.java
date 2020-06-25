package it.polito.tdp.crimes.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	Graph<Integer, DefaultWeightedEdge> graph;

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

}
