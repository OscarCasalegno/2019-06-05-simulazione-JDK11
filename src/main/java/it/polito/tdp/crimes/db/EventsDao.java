package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.Coord;
import it.polito.tdp.crimes.model.Event;

public class EventsDao {

	public List<Event> listAllEvents() {
		String sql = "SELECT * FROM events";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<Integer> getAnni() {
		String sql = "SELECT DISTINCT YEAR(reported_date) AS anno " + "FROM EVENTS " + "ORDER BY YEAR(reported_date)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Integer> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getInt("anno"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<Coord> getDistrictCoordByYear(Integer year) {
		String sql = "SELECT district_id as id, AVG(geo_lon) as lon, AVG(geo_lat) as lat " + "FROM `events` "
				+ "WHERE YEAR(reported_date) = ? " + "GROUP BY district_id";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, year);

			List<Coord> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Coord(res.getInt("id"), new LatLng(res.getDouble("lat"), res.getDouble("lon"))));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<Integer> getMesi(Integer year) {
		String sql = "SELECT DISTINCT Month(reported_date) AS mese " + "FROM EVENTS WHERE YEAR(reported_date)= ? "
				+ "ORDER BY Month(reported_date)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			List<Integer> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getInt("mese"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<Integer> getGiorni(Integer year) {
		String sql = "SELECT DISTINCT Day(reported_date) AS giorno " + "FROM EVENTS WHERE YEAR(reported_date)= ? "
				+ "ORDER BY Day(reported_date)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			List<Integer> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getInt("giorno"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<Event> listEventsByDay(Integer giorno, Integer mese, Integer anno) {
		String sql = "SELECT * FROM events where Day(reported_date)= ? and month(reported_date)= ? and year(reported_date)= ?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, giorno);
			st.setInt(2, mese);
			st.setInt(3, anno);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
						res.getInt("offense_code_extension"), res.getString("offense_type_id"),
						res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
						res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
						res.getInt("is_crime"), res.getInt("is_traffic")));

			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Integer getDistrettoMigliore(Integer anno) {
		String sql = "SELECT district_id as id, COUNT(*) as crimini " + "FROM `events` "
				+ "WHERE YEAR(reported_date)=? " + "GROUP BY district_id";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			Integer best = 0;
			Integer bestC = Integer.MAX_VALUE;

			ResultSet res = st.executeQuery();

			while (res.next()) {
				if (res.getInt("crimini") < bestC) {
					best = res.getInt("id");
				}
			}

			conn.close();
			return best;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
