package nusegbot.functions;

import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import nusegbot.DBConnection;

public class Giveaway {
	
	public static DBConnection db;
	public static String title;
	public static String link;
	
	public Giveaway(DBConnection db2, String Title, String Link) {
		db = db2;
		title = Title;
		link = Link;
	}
	
	public void populateDatabase() {
		String[] nameList = null;
		System.out.println("Populating Database...");
		try {
			Document doc = Jsoup.connect(link).get();
			System.out.println(doc.toString());
			Element ele = doc.select("body").first();
			String names = ele.text();
			nameList = names.split(" ");
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		db.connect();
		db.update("INSERT INTO giveaways (name, active) VALUES ('"+ title +"', TRUE)");
		for(int i = 0; i < nameList.length; i++) {
			db.update("INSERT INTO participants(ign, joinedVoice, giveawayName) VALUES ('"+nameList[i]+"', FALSE, '"+title+"')");
		}
		db.close();
	}
	
	public String getTitle() {
		return title;
	}
	
}
