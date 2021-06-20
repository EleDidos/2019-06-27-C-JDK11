package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	
	public List<String> listAllCategories(){
		String sql = "SELECT offense_category_id FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					if(!list.contains(res.getString("offense_category_id")))
						list.add(res.getString("offense_category_id"));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	//VERTICI
	public List<String> listAllReati( String cat,LocalDate ld){
		String sql = "SELECT DISTINCT offense_type_id as reato "
				+ "FROM events "
				+ "WHERE YEAR(reported_date)=? and MONTH(reported_date) =? and DAY(reported_date)=? and offense_category_id=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			Integer y = ld.getYear();
			Integer m=ld.getMonthValue();
			Integer d=ld.getDayOfMonth();
			st.setInt(1, y);
			st.setInt(2, m);
			st.setInt(3, d);
			st.setString(4, cat);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					if(!list.contains(res.getString("offense_type_id")))
						list.add(res.getString("offense_type_id"));
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<LocalDate> listAllDays(){
		String sql = "SELECT YEAR(reported_date) as y, MONTH(reported_date) as m, DAY(reported_date) as d "
				+ "FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<LocalDate> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
						Integer year= res.getInt("y");
						Integer month= res.getInt("m");
						Integer day= res.getInt("d");
						LocalDate ld = LocalDate.of(year, month, day);
						if(!list.contains(ld))
							list.add(ld);
						
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	//quelli che selezioni sono già in reati
	public List <Arco> listArchi(String cat,LocalDate ld){
		String sql = "SELECT e1.offense_type_id as r1,  e2.offense_type_id as r2, COUNT(DISTINCT e1.precinct_id) as peso "
				+ "FROM events as e1, events as e2 "
				+ "WHERE e1.offense_type_id>e2.offense_type_id and e1.precinct_id=e2.precinct_id and  YEAR(e1.reported_date)=? and MONTH(e1.reported_date) =? and DAY(e1.reported_date)=? and e1.offense_category_id=? and YEAR(e2.reported_date)=? and MONTH(e2.reported_date) =? and DAY(e2.reported_date)=? and e2.offense_category_id=? "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id";

		Connection conn = DBConnect.getConnection();
		List <Arco> archi = new ArrayList <Arco>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, ld.getYear());
			st.setInt(2, ld.getMonthValue());
			st.setInt(3, ld.getDayOfMonth());
			st.setString(4, cat);
			st.setInt(5, ld.getYear());
			st.setInt(6, ld.getMonthValue());
			st.setInt(7, ld.getDayOfMonth());
			st.setString(8, cat);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				if(res.getInt("peso")>0) {
					Arco a = new Arco(res.getString("r1"),res.getString("r2"),res.getInt("peso"));
					archi.add(a);
					System.out.println(a);
				}
			

			}
			conn.close();
			return archi;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


}
