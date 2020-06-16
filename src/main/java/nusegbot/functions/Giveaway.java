package nusegbot.functions;

import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

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
			doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
			doc.select("br").append("\\n");
			doc.select("p").prepend("\\n\\n");
			String str = doc.html().replaceAll("\\\\n", "\n");
			String strWithNewLines = 
		            Jsoup.clean(str, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
			//Element ele = doc.select("body").first();
			//String names = ele.text();
			nameList = strWithNewLines.split("\n");
			for (int i = 0; i < nameList.length; i++) {
				nameList[i] = nameList[i].strip();
			}
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
