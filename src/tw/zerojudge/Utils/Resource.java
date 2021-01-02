package tw.zerojudge.Utils;

public class Resource {
	public static enum MESSAGE {
		Privilege_UNKNOWN("Message.UNKNOWN"), 
		Privilege_FORBIDDEN("Message.FORBIDDEN"), 
		Privilege_NOTDEFINE("Message.NotDefine"), 
		Privilege_ALLOWED("Message.Allow"); 
		private String key;

		private MESSAGE(String key) {
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}
	}

}
