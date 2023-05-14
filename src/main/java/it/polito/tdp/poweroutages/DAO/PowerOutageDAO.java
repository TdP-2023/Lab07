package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {
	
	
	/**
	 * Method that reads the NERCs present in the database, and returns them as a 
	 * list ordered in ascending values of their names
	 * @return
	 */
	public List<Nerc> getNercList() {
		String sql = "SELECT id, value FROM nerc ORDER BY value ASC";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	
	
	/**
	 * Method that reads the PowerOutages present in the database for a certain NERC, and returns them as a 
	 * list ordered in ascending values of dates
	 * @return
	 */
	public List<PowerOutage> getPowerOutagesByNerc(Nerc n){
		String sql = "SELECT id, customers_affected, date_event_began, date_event_finished "
				+ "FROM PowerOutages "
				+ "WHERE nerc_id = ? "
				+ "ORDER BY date_event_began ASC";
		List<PowerOutage> poList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				PowerOutage p = new PowerOutage(rs.getInt("id"), n, rs.getInt("customers_affected"), 
						rs.getTimestamp("date_event_began").toLocalDateTime(), rs.getTimestamp("date_event_finished").toLocalDateTime());
				poList.add(p);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return poList;
	}
	
	
	
	/**
	 * Method that reads a NERC given its ID
	 * @return
	 */
	public Nerc getNercByID(int id) {
		String sql = "SELECT value "
				+ "FROM Nerc "
				+ "WHERE id = ? ";
		
		Nerc n = null;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet res = st.executeQuery();

			if (res.first()) {
				n = new Nerc(id, res.getString("value"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return n;
	}

}
