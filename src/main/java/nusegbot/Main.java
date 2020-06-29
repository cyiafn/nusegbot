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
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
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
				else if(msg.startsWith("/systemscheck") && (event.getAuthor().getId().equals("132420435294814208") || event.getAuthor().getId().equals("185836347536179200")))
				{
					event.getChannel().sendMessage("Command Succeeded...\nChecking Database...").queue();
					try {
						db.connect();
						db.close();
						Thread.sleep(2000);
						event.getChannel().sendMessage("DB@35.240.223.254 is up...\nSystems looking green!").queue();
					}
					catch (Exception e)
					{
						event.getChannel().sendMessage("DB@35.240.223.254 is down!").queue();;
					}
					
				}
				else if (msg.startsWith("/help") && PermissionUtil.checkPermission(event.getMember(), Permission.ADMINISTRATOR))
				{
					event.getChannel().sendMessage(":hammer: Admin Help Panel :hammer:\n\n:exclamation: **General Information** :exclamation:\n1. Please remove all <> in commands and replace what is within it with the variables you want.\n\n\n:tada: **Giveaway Commands** :tada:\n1. /giveaway create <giveaway name> <pastebin raw url for igns> - Create a giveaway of that name and enters the igns of all participants into the giveaway via the url. [Please use this command in an admin channel.]\n\t - Select all the IGNs of participants of giveaway in Google Sheets/Excel and paste it into the New Paste section of https://pastebin.com. \n\t - Press the Create New Paste button. \n\t - Press the raw button.\n\t - Copy the URL of the page and use that URL in this command.\n2. /giveaway roll <giveaway name> - Rolls for a winner from the eligible list. The person rolled will no longer be eligible for this giveaway. (Can roll multiple times) [The command will be deleted. Please use this command where participants can see the roll happening.]\n3. /giveaway close <giveaway name> - Closes the giveaway forever (USE IT AT YOUR OWN RISK) [Please use this command in an admin channel].\n\n:mega: **Event Commands** :mega:\n1. /createevent <event name> | <event description> | <dates & time> | <games> | <signup link> - Create a new event announcement with the respective details. If a picture is required, upload the picture to discord first, then paste the command under 'Add a comment'. [Use this command under an event announcement channel where potential participants can view it. The original command will be deleted.]\n\n\n:person_tipping_hand: **Contact Us** :person_tipping_hand:\nIf you have any issues or require help, please contact: \ncira#2725 id:<132420435294814208> or \nCloey#7090 id:<185836347536179200>").queue();
				}
				else if (msg.startsWith("/createevent"))
				{
					
					CreateEvent newEvent = new CreateEvent(msg);
					String eventStr = newEvent.formatEvent();
					
					String url = "";
					
				    if (!event.getMessage().getAttachments().isEmpty() && event.getMessage().getAttachments().get(0).isImage()) 
				    {
				        url = event.getMessage().getAttachments().get(0).getUrl();
				        eventStr += url;
				    }
					
					event.getChannel().sendMessage(eventStr).queue();
					event.getMessage().delete().queue();
					
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
								event.getChannel().sendMessage(":tada:The winner is " + winner+ "!:tata:").queue();
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
						event.getMessage().delete().queue();
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
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		Guild guild = event.getGuild();
		if (event.getChannel().getName().equals("role-assign"))
		{ 
			if (event.getReactionEmote().getId().equals("727188821128773744") ) {
				Role role = event.getGuild().getRolesByName("Dota 2", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190275817996350"))
			{
				Role role = event.getGuild().getRolesByName("Hearthstone", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190373772034099"))
			{
				Role role = event.getGuild().getRolesByName("LoL", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190520086003753"))
			{
				Role role = event.getGuild().getRolesByName("MLBB", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190400271646791"))
			{
				Role role = event.getGuild().getRolesByName("OW", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getName().equals("727190232440766466"))
			{
				Role role = event.getGuild().getRolesByName("Smash", false).get(0);
				guild.addRoleToMember(event.getMember(), role).queue();
			}
		}
	}
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		Guild guild = event.getGuild();
		if (event.getChannel().getName().equals("role-assign"))
		{ 
			if (event.getReactionEmote().getId().equals("727188821128773744") ) {
				Role role = event.getGuild().getRolesByName("Dota 2", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190275817996350"))
			{
				Role role = event.getGuild().getRolesByName("Hearthstone", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190373772034099"))
			{
				Role role = event.getGuild().getRolesByName("LoL", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190520086003753"))
			{
				Role role = event.getGuild().getRolesByName("MLBB", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getId().equals("727190400271646791"))
			{
				Role role = event.getGuild().getRolesByName("OW", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
			}
			else if (event.getReactionEmote().getName().equals("727190232440766466"))
			{
				Role role = event.getGuild().getRolesByName("Smash", false).get(0);
				guild.removeRoleFromMember(event.getMember(), role).queue();
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
