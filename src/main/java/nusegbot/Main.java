package nusegbot;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;  
import java.util.Date;  

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nusegbot.functions.CreateEvent;
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
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();
		System.out.println("["+ formatter.format(date) +"] Author: " + event.getAuthor().getName() + " | Message: " + event.getMessage().getContentDisplay());
		
		if (event.getMessage().getContentRaw().startsWith("/ping"))
		{
			event.getChannel().sendMessage("Pong!").queue();
		}
		else if (event.getMessage().getContentRaw().startsWith("/help"))
		{
			
		}
		else if (event.getMessage().getContentRaw().startsWith("/createevent"))
		{
			CreateEvent events = new CreateEvent(db);
//			String test = events.selectTest();
//			event.getChannel().sendMessage(test).queue();
		}
		
	}
}
