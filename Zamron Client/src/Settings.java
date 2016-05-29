

public class Settings {
	
	/**
	 * @author Chopper
	 */

	static String title = "servn";
	private static String local = "localhost";
	
	/**
	 * Connection settings
	 */
	public static final String IP = local;
	public static final String LOBBY_IP = local;
	public static final int BUILD = 742;
	
	public static int SUB_BUILD = 45;
	
	/**
	 * Link settings
	 */
	public static final String RECOVER_PASS_LINK = "http://.com/forums/index.php?app=tickets&module=tickets&section=post&do=new_ticket";
	
	/**
	 * General settings
	 */
	public static boolean LOBBY_ENABLED = false;
	public static final boolean LOBBY_TOGGLING = false;
	public static boolean DISABLE_XTEA_CRASH = true;
	public static boolean DISABLE_USELESS_PACKETS = true;
	public static boolean DISABLE_RSA = false;
	public static boolean useIsaac = false;
	
	/**
	 * Console settings
	 */
	public static final int CONSOLE_COLOUR = 0xFFFC36;
	public static final int SCROLL_BAR_COLOUR = 0x33E85D;
	public static boolean CONSOLE_SCROLL_BAR_ENABLED = false;
	public static boolean CONSOLE_COLOUR_ENABLED = false;
	
	/**
	 * Java arguments for client start
	 */
	public static final String JVM_ARGS = "-Xmx1024m";

	/**
	 * Are we using a custom icon?
	 */
	public static final boolean CUSTOM_IMAGE = true;
	public static final String ICON_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/NYCS-bull-trans-G.svg/2000px-NYCS-bull-trans-G.svg.png";
	
	/**
	 * Cache and lobby settings
	 */
	public static final Object ABSOLOUTE_CACHE_NAME_1 = "gggg";
	public static final String ABSOLOUTE_CACHE_NAME_2 = "ggggg";
	public static final Object CACHE_NAME = "Ggge";
	public static final String CACHE_SUB_NAME = "Ggggg";
	public static final CharSequence LOBBY_TEXT = "You last logged in from " + Loader.getLocalIP();


}
