package nusegbot.functions;


public class CreateEvent {
	
	public String event;
	
	public CreateEvent(String event) {
		this.event = event;
	}
	
	public String formatEvent() {
		
		/* Format for command:

		/createevent POST FINALS EVENT | We are having a post-finals event! | 23 July 8pm,9pm, 24 July 8pm,9pm | League of Legends, Overwatch | https://www.instagram.com/nusegaming/
		
		*/
		
		String parsed = event.substring(13);
		
		String[] parsedStr = parsed.split("\\|");
		
		System.out.println("0 " + parsedStr[0]);
		
		String waveEmoji = new String(Character.toChars(0x1F44B));
		String clockEmoji = new String(Character.toChars(0x23F0));
		String gameEmoji = new String(Character.toChars(0x1F3AE));
		String megaphoneEmoji = new String(Character.toChars(0x1F4E3));
		String memoEmoji = new String(Character.toChars(0x1F4DD));
		String heartEmoji = new String(Character.toChars(0x1F646));
		String speechEmoji = new String(Character.toChars(0x1F4AC));
		
		StringBuilder finalStr = new StringBuilder(" Hey @everyone! " + waveEmoji + "\n\n");
		
		/*
		finalStr.append(megaphoneEmoji + " " + parsedStr[0] + megaphoneEmoji + "\n");
		finalStr.append(speechEmoji + " Description: " + parsedStr[1] + "\n");
		finalStr.append(clockEmoji + " Dates & Times: " + parsedStr[2] + "\n");
		finalStr.append(gameEmoji + " Games: " + parsedStr[3] + "\n");
		finalStr.append(memoEmoji +  " Sign-up Link: " + parsedStr[4] + "\n");
		finalStr.append("See you there! " + heartEmoji);
		*/
		finalStr.append(megaphoneEmoji + " **" + parsedStr[0]  + "** " + megaphoneEmoji + "\n\n");
		finalStr.append(speechEmoji + " " + parsedStr[1] + "\n");
		finalStr.append(clockEmoji + " " + parsedStr[2] + "\n");
		finalStr.append(gameEmoji + " " + parsedStr[3] + "\n");
		finalStr.append(memoEmoji +  " " + parsedStr[4] + "\n\n");
		finalStr.append("See you there! " + heartEmoji + "\n\n");
		
		return finalStr.toString();
	}
}

