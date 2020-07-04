package nusegbot.functions;


public class CreateEvent {
	
	public String event;
	
	public CreateEvent(String event) {
		this.event = event;
	}
	
	public String formatEvent() {
		
/* Format for command:
IMG:
/createevent POST FINALS EVENT | We are having a post-finals event! | 23 July 8pm,9pm, 24 July 8pm,9pm | League of Legends, Overwatch | https://www.instagram.com/nusegaming/ | https://i.imgur.com/Vs774wm.gif
NO IMG:
/createevent POST FINALS EVENT | We are having a post-finals event! | 23 July 8pm,9pm, 24 July 8pm,9pm | League of Legends, Overwatch | https://www.instagram.com/nusegaming/
*/
		
		String parsed = event.substring(13);
		
		String[] parsedStr = parsed.split("\\|");
		
		Boolean img = false;

		
		if (parsedStr.length == 6) {
			img = true;
		}
		
		String waveEmoji = new String(Character.toChars(0x1F44B));
		String clockEmoji = new String(Character.toChars(0x23F0));
		String gameEmoji = new String(Character.toChars(0x1F3AE));
		String megaphoneEmoji = new String(Character.toChars(0x1F4E3));
		String memoEmoji = new String(Character.toChars(0x1F4DD));
		String heartEmoji = new String(Character.toChars(0x1F646));
		String speechEmoji = new String(Character.toChars(0x1F4AC));
		String picEmoji = new String(Character.toChars(0x1F4F8));
		String fixEmoji = new String(Character.toChars(0x1F6E0));
		
		StringBuilder finalStr = new StringBuilder(" Hey @everyone! " + waveEmoji + "\n\n");
		
		finalStr.append(megaphoneEmoji + " **" + parsedStr[0]  + "** " + megaphoneEmoji + "\n\n");
		finalStr.append(speechEmoji + " " + parsedStr[1] + "\n");
		finalStr.append(clockEmoji + " " + parsedStr[2] + "\n");
		finalStr.append(gameEmoji + " " + parsedStr[3] + "\n");
		finalStr.append(memoEmoji +  " " + parsedStr[4] + "\n");
		if (img) {
			finalStr.append(picEmoji + " " + parsedStr[5] + "\n");
		}
		finalStr.append("\nSee you there! " + heartEmoji + "\n\n");
		if (img) {
		finalStr.append(fixEmoji + " Not seeing images? Enable \"Display images when posted as links to chat\" under Settings > Text & Images!\n\n");
		}
		return finalStr.toString();
	}
}

