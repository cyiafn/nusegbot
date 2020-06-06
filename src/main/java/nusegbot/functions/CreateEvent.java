package nusegbot.functions;

import java.sql.ResultSet;

import nusegbot.DBConnection;

public class CreateEvent {
	
	public static DBConnection db;
	
	public CreateEvent(DBConnection db2) {
		db = db2;
	}
	public String selectTest() {
		db.connect();
		ResultSet rs = db.select("SELECT * FROM nusegbot.testtable;");
		try {
			String[] arr = null;
			while (rs.next()) {
			    String em = rs.getString("prikey");
			    arr = em.split("\n");
			    for (int i =0; i < arr.length; i++){
			        System.out.println(arr[i]);
			    }
			}
			db.close();
			System.out.println(arr[0]);
			return arr[0].toString();
		}

		catch(Exception e) {
			System.out.println(e);
		}
		return "Not working";
	}
}
