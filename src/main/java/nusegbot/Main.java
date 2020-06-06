package nusegbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;

public class Main {
	public static void main(String[] arguments) throws LoginException
	{
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		String token = "NzE4ODk4ODUyMDk4MjExODYw.Xtvtww.pGDa4bkJ0TJybSeRyFnI8r3yM3I";
		builder.setToken(token);
		builder.build();
	}
}
