package nusegbot;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import nusegbot.functions.CreateEvent;
import nusegbot.functions.Giveaway;
import net.dv8tion.jda.api.AccountType;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter{
	
	
	public static DBConnection db = new DBConnection();
	
	public static void main(String[] arguments) throws LoginException, ClassNotFoundException
	{
		//JDA Bot
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		String token = "NzE4ODk4ODUyMDk4MjExODYw.Xtvtww.pGDa4bkJ0TJybSeRyFnI8r3yM3I";
		builder.setToken(token);
		builder.addEventListeners(new Main());
		builder.build();
		
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		//debug
		if(event.getAuthor().isBot())
		{
			return;
		}
		else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();
			System.out.println("["+ formatter.format(date) +"] Author: " + event.getAuthor().getName() + " | Message: " + event.getMessage().getContentDisplay());
			
			String msg = event.getMessage().getContentRaw();
			
			if (msg.startsWith("/")) {
				if (msg.startsWith("/ping"))
				{
					event.getChannel().sendMessage("Pong!").queue();
				}
				else if (msg.startsWith("/help"))
				{
					
				}
				else if (msg.startsWith("/createevent"))
				{
					CreateEvent events = new CreateEvent(db);
//					String test = events.selectTest();
//					event.getChannel().sendMessage(test).queue();
				}
				else if (msg.startsWith("/giveaway ") && PermissionUtil.checkPermission(event.getMember(), Permission.ADMINISTRATOR))
				{
					msg = msg.replace("/giveaway ", "");
					if (msg.startsWith("create ")) {
						msg = msg.replace("create ", "");
						if (msg.contains(" ")) {
							String[] msgParams;
							msgParams = msg.split(" ");
							boolean nameConflict = false;
							ArrayList<String> giveawayNames = new ArrayList<String>();
							try {
								db.connect();
								ResultSet rs = db.select("SELECT DISTINCT name FROM giveaways");
								while (rs.next()) {
									giveawayNames.add(rs.getString("name"));
								}
								db.close();
							}
							catch (Exception e) {
								System.out.println(e);
							}
							for (int i = 0; i < giveawayNames.size(); i++)
							{
								if(giveawayNames.get(i).equals(msgParams[0])) {
									nameConflict = true;
								}
							}
							if (nameConflict == false) {
								Giveaway tempGiveaway = new Giveaway(db, msgParams[0], msgParams[1]);
								tempGiveaway.populateDatabase();
								event.getChannel().sendMessage("populating database.").queue();
							}
							else {
								event.getChannel().sendMessage("There is already an event with the same name.").queue();
							}
							
						}
						else {
							event.getChannel().sendMessage("You have entered an invalid command.").queue();
						}
					}
					else if (msg.startsWith("roll ")) {
						msg = msg.replace("roll ", "");
						boolean nameConflict = false;
						ArrayList<String> giveawayNames = new ArrayList<String>();
						try {
							db.connect();
							ResultSet rs = db.select("SELECT DISTINCT name FROM giveaways WHERE active = TRUE");
							while (rs.next()) {
								giveawayNames.add(rs.getString("name"));
							}
							db.close();
						}
						catch (Exception e) {
							System.out.println(e);
						}
						for (int i = 0; i < giveawayNames.size(); i++)
						{
							if(giveawayNames.get(i).equals(msg)) {
								nameConflict = true;
							}
						}
						if (nameConflict == true) {
							ArrayList<String> ignList = new ArrayList<String>();
							try {
								db.connect();
								ResultSet rs = db.select("SELECT ign FROM participants WHERE giveawayName = '"+msg+"' AND joinedVoice = TRUE");
								while (rs.next()) {
									ignList.add(rs.getString("ign"));
								}
								db.close();
							}
							catch (Exception e)
							{
								event.getChannel().sendMessage("There is currently no eligible participants.").queue();
							}
							if (ignList.size() > 1) {
								Random rand = new Random();
								int upperbound = ignList.size() + 1;
								int int_random = rand.nextInt(upperbound);
								String winner = ignList.get(int_random);
								event.getChannel().sendMessage("The winner is " + winner+ "!").queue();
								try {
									db.connect();
									db.update("DELETE FROM participants WHERE ign = '"+winner+"' AND giveawayName = '"+msg+"'");
									db.close();
								}
								catch(Exception e){
									
								}
							}
							else if(ignList.size() == 1) {
								String winner = ignList.get(0);
								event.getChannel().sendMessage(":tada:The winner is " + winner+ "!:data:").queue();
								try {
									db.connect();
									db.update("DELETE FROM participants WHERE ign = '"+winner+"' AND giveawayName = '"+msg+"'");
									db.close();
								}
								catch(Exception e){
									
								}
							}
							else {
								event.getChannel().sendMessage("There are currently no eligible participants.").queue();
							}
						}
						else {
							event.getChannel().sendMessage("There is no giveaway with that name or the giveaway is not active!").queue();
						}
					}
					else if (msg.startsWith("close ")) {
						msg = msg.replace("close ", "");
						boolean nameConflict = false;
						ArrayList<String> giveawayNames = new ArrayList<String>();
						try {
							db.connect();
							ResultSet rs = db.select("SELECT DISTINCT name FROM giveaways WHERE active = TRUE");
							while (rs.next()) {
								giveawayNames.add(rs.getString("name"));
							}
							db.close();
						}
						catch (Exception e) {
							System.out.println(e);
						}
						for (int i = 0; i < giveawayNames.size(); i++)
						{
							if(giveawayNames.get(i).equals(msg)) {
								nameConflict = true;
							}
						}
						if (nameConflict == true) {
							try {
								db.connect();
								db.update("UPDATE giveaways SET active = FALSE WHERE name = '"+msg+"'");
								db.close();
							}
							catch(Exception e) {
							
							}
							event.getChannel().sendMessage("Giveaway '" + msg + "' closed.").queue();
						}
						else {
							event.getChannel().sendMessage("There is no such giveaway.").queue();
						}
					}
					else {
						event.getChannel().sendMessage("You have entered an invalid command.").queue();
					}
				}
				else {
					event.getChannel().sendMessage("There is no such command.").queue();
				}
			}
		}
		
	}
	//onguildvoicejoin
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		String ign = event.getMember().getNickname();
		try {
			db.connect();
			db.update("UPDATE participants SET joinedVoice = TRUE WHERE ign = '"+ign+"'");
			db.close();
		}
		catch (Exception e)
		{
			
		}
	}
}
