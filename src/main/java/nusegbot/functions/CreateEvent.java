package nusegbot.functions;

//import java.sql.ResultSet;

//import nusegbot.DBConnection;

public class CreateEvent {
	
	//public static DBConnection db;
	public String event;
	
	public CreateEvent( String event) {

		this.event = event;
	}
	public String formatEvent(String event) {
		return event.substring(13);
	}
}

