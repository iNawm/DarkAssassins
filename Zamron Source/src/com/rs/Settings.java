package com.rs;

import java.math.BigInteger;

import com.rs.game.WorldTile;

public final class Settings {
	
	/**
	 * MYSQL Database settings.
	 */
	public static final String DB_HOST = "";
	public static final String DB_USER = "";
	public static final String DB_PASS = "";
	public static final String DB_NAME = "";
	
    public static final boolean SQUEAL_OF_FORTUNE_ENABLED = true; // if not, people will be able to spin but not claim
    public static final String BUY_SPINS_LINK = "www.zamron.net/donate";
    
    public static final double[] SOF_CHANCES = new double[] { 1.0D, 0.35D, 0.0089D, 0.0001D };

    public static final int[] SOF_COMMON_CASH_AMOUNTS = new int[] { 100, 250, 500, 1000, 5000 };
    public static final int[] SOF_UNCOMMON_CASH_AMOUNTS = new int[] { 10000, 25000, 50000, 100000, 500000 };
    public static final int[] SOF_RARE_CASH_AMOUNTS = new int[] { 1000000, 2500000, 5000000, 10000000, 25000000 };
    public static final int[] SOF_JACKPOT_CASH_AMOUNTS = new int[] { 50 * 1000000, 50 * 1000000, 75 * 1000000, 100 * 1000000 };
    public static final int[] SOF_COMMON_LAMPS = new int[] { 23713, 23717, 23721, 23725, 23729, 23737, 23733, 23741, 23745, 23749, 23753, 23757, 23761, 23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790, 23802, 23798, 23810, 23806, 23814 };
    public static final int[] SOF_UNCOMMON_LAMPS = new int[] { 23714, 23718, 23722, 23726, 23730, 23738, 23734, 23742, 23746, 23750, 23754, 23758, 23762, 23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791, 23803, 23799, 23811, 23807, 23815 };
    public static final int[] SOF_RARE_LAMPS = new int[] { 23715, 23719, 23723, 23727, 23731, 23739, 23735, 23743, 23747, 23751, 23755, 23759, 23763, 23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792, 23804, 23800, 23812, 23808, 23816 };
    public static final int[] SOF_JACKPOT_LAMPS = new int[] { 23716, 23720, 23724, 23728, 23732, 23740, 23736, 23744, 23748, 23752, 23756, 23760, 23764, 23768, 23773, 23781, 23777, 23789, 23785, 23797, 23793, 23805, 23801, 23813, 23809, 23817 };
    public static final int[] SOF_COMMON_OTHERS = new int[] { 1965, 1511, 1205, 438, 327, 555, 556, 882, 1925, 314, 313, 436 };
    public static final int[] SOF_UNCOMMON_OTHERS = new int[] { 24154, 24154, 24155, 24155, 1119, 1125, 1121, 1123, 1127, 1131, 1133, 6322, 1135, 12971, 4091, 1295, 1297, 1299, 1303, 1301, 1327, 1325, 1331, 1329, 1311, 1333, 1315, 1313, 1319, 1317, 1367, 1365, 1371, 1369, 1273, 1373, 1361, 1271, 1275, 843, 849, 1355, 1357, 9174, 9177, 853, 857, 9183, 9181, 9179 };
    public static final int[] SOF_RARE_OTHERS = new int[] { 995, 995, 995, 995, 23665, 23666, 23667, 23668, 23669, 23670, 23671, 23672, 23673, 23674, 23675, 23676, 23677, 23678, 23679, 23680, 23681, 23682, 23691, 23692, 23693, 23694, 23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683, 23698 };
    public static final int[] SOF_JACKPOT_OTHERS = new int[] { 995, 995, 995, 995, 20929, 23671, 23672, 25202 };


	/**
	 * General client and server settings.
	 */
	
	public static final String SERVER_NAME = "Zamron";
	public static final String MASTER_PASSWORD = "R268711637";
	public static final int PORT_ID = 43594;
	public static final String LASTEST_UPDATE = "<col=7E2217>Check the forums for the latest updates!";
	public static final String CACHE_PATH = "data/cache/";
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 742;
	public static final int CUSTOM_CLIENT_BUILD = 45;
	public static final String LOG_PATH = "data/logs/";
	public static final int MAX_STARTER_COUNT = 1;
	
	/**
	 * Link settings
	 */
	
	public static final String WEBSITE_LINK = "http://Zamron.net";
	public static final String ITEMLIST_LINK = "http://zamron.net/itemList.txt";
	public static final String ITEMDB_LINK = "http://itemdb.biz";
	public static final String VOTE_LINK = "http://Zamron.net/vote";
	public static final String RAGE_SKYPE = "Zamron Group";
	public static final String DONATE = "http://zamron.net/donate";
	
	/**
	 * Rankings by Leighton - Retro.
	 */
	
	public static final String BETA[] = { "Leighton" };
	public static final String OWNER[] = { "Leighton" };
	
	public static boolean DEBUG = true;
	public static boolean GUIMODE;
	public static boolean HOSTED;
	public static boolean ECONOMY;
	
	/**
	 * If the use of the managment server is enabled.
	 */
	
	public static boolean MANAGMENT_SERVER_ENABLED = true;

	/**
	 * Graphical User Interface settings
	 */
	
	public static final String GUI_SIGN = "Matrix GUI";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
    /**
     * XP well related.
     */
    public static final int WELL_MAX_AMOUNT = 250000000;
    
	/**
	 * Player settings
	 */
	
	public static final int START_PLAYER_HITPOINTS = 100;
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(2601, 5592, 1);
	public static final WorldTile RESPAWN_DEATH_LOCATION = new WorldTile(3087, 3491, 0);
	public static final WorldTile[] RESPAWN_PLAYER_LOCATION = {new WorldTile(3087, 3491, 0), new WorldTile(3087, 3491, 0)};
	public static final WorldTile[] HOME_PLAYER_LOCATION = {new WorldTile(3087, 3491, 0), new WorldTile(3087, 3491, 0)};
	public static final WorldTile HOME_PLAYER_LOCATION1 = new WorldTile(3087, 3491, 0);
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000; 
	public static int DROP_RATE = 2;
	public static double LAMP_XP_RATE = 8;
	
	
	/**
	* XP Rates
	*/
	public static int ATTACK_XP_RATE = 75;
	public static int STRENGTH_XP_RATE = 70;
	public static int DEFENCE_XP_RATE = 80;
	public static int HITPOINTS_XP_RATE = 90;
	public static int RANGE_XP_RATE = 80;
	public static int MAGIC_XP_RATE = 80;
	public static int PRAYER_XP_RATE = 100;
	public static int SLAYER_XP_RATE = 50;
	public static int FARMING_XP_RATE = 70;
	public static int AGILITY_XP_RATE = 75;
	public static int HERBLORE_XP_RATE = 50;
	public static int THIEVING_XP_RATE = 90;
	public static int CRAFTING_XP_RATE = 85;
	public static int MINING_XP_RATE = 90;
	public static int SMITHING_XP_RATE = 85;
	public static int FISHING_XP_RATE = 90;
	public static int COOKING_XP_RATE = 80;
	public static int FIREMAKING_XP_RATE = 90;
	public static int WOODCUTTING_XP_RATE = 80;
	public static int CONSTRUCTION_XP_RATE = 60;
	public static int HUNTER_XP_RATE = 60;
	public static int SUMMONING_XP_RATE = 80;
	public static int DUNGEONEERING_XP_RATE = 100;
	public static int FLETCHING_RATE = 90;
	public static int RUNECRAFTING_XP_RATE = 70;
	
	
	/**
	 * World settings
	 */
	
	public static final int WORLD_CYCLE_TIME = 600;
	
	/**
	 * Music & Emote settings
	 */
	
	public static final int AIR_GUITAR_MUSICS_COUNT = 1;
	
	/**
	 * Quest settings
	 */
	
	public static final int QUESTS = 183;
	
	/**
	 * Goblin Raids
	 */
	public static boolean GOBLINRAID;
	
	/**
	 * Memory settings
	 */
	
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 250;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000;
	
	/**
	 * Game constants
	 */
	
	public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72 };
	
	public static final String GRAB_SERVER_TOKEN = "hAJWGrsaETglRjuwxMwnlA/d5W6EgYWx";
	public static final int[] GRAB_SERVER_KEYS = {1441,78700,44880,39771,363186,44375,0,16140,7316
		,271148,810710,216189,379672,454149,933950,21006,25367,17247,1244,1,14856,1494,119,882901,1818764,3963,3618};
	
	
	public static final BigInteger GRAB_SERVER_PRIVATE_EXPONENT = new BigInteger("95776340111155337321344029627634178888626101791582245228586750697996713454019354716577077577558156976177994479837760989691356438974879647293064177555518187567327659793331431421153203931914933858526857396428052266926507860603166705084302845740310178306001400777670591958466653637275131498866778592148380588481");
	public static final BigInteger GRAB_SERVER_MODULUS =  new BigInteger("119555331260995530494627322191654816613155476612603817103079689925995402263457895890829148093414135342420807287820032417458428763496565605970163936696811485500553506743979521465489801746973392901885588777462023165252483988431877411021816445058706597607453280166045122971960003629860919338852061972113876035333");
	
	public static final BigInteger PRIVATE_EXPONENT = new BigInteger("90587072701551327129007891668787349509347630408215045082807628285770049664232156776755654198505412956586289981306433146503308411067358680117206732091608088418458220580479081111360656446804397560752455367862620370537461050334224448167071367743407184852057833323917170323302797356352672118595769338616589092625");
	public static final BigInteger MODULUS = new BigInteger("102876637271116124732338500663639643113504464789339249490399312659674772039314875904176809267475033772367707882873773291786014475222178654932442254125731622781524413208523465520758537060408541610254619166907142593731337618490879831401461945679478046811438574041131738117063340726565226753787565780501845348613");
	public static final String NON_WALKING_NPCS = null;

	
	public static String[] DONATOR_ITEMS = {"(i)" };
	
	public static String[] EXTREME_DONATOR_ITEMS = {"donator", "red donator cape", "purple donator cape"};
	
	public static String[] ELITE_DONATOR_ITEMS = {"primal", "torva full helm (fire)", "torva platebody (fire)", "torva platelegs (fire)", "pernix cowl (ice)", "pernix body (ice)",
	"pernix chaps (ice)", "virtus mask (ice)", "virtus robe top (ice)", "torva full helm (ice)", "torva platelegs (ice)", "torva platebody (ice)", "virtus mask (fire)", "virtus robe top (fire)", "virtus robe legs (fire)",  };
	
	public static String[] SUPREME_DONATOR_ITEMS = {"supreme", "elite pernix cowl", "elite pernix body", "elite pernix chaps", "elite torva full helm", "elite torva platebody", 
	"elite torva platelegs", "elite virtus mask", "elite virtus robe top", "elite virtus robe legs"};
	
	 public static String[] UNTRADEABLE_ITEMS = { "(i)", "(deg", "(class", "sacred clay", "null", "flaming skull", "overload", "extreme", "dreadnip", "max hood", "max cape",
			"dungeoneering", "tokhaar-kal", "culinaromancer", "spin ticket", "tokkul", "fighter", "fire cape", "lamp", "completion", "magical blastbox", "cape (t)", "penance", "defender", "charm", "keenblade", "quickbow", "mindspike", "baby troll", "aura ", " scroll", "completionist",
			"max cape", "skill cape", " defender", "fire cape", "tokhaar-kal", "lucky", "dominion"};
			
	public static String[] RARE_DROPS = { "torva", "pernix", "virtus", "sigil",  "bandos", "armadyl", "vesta", "visage", "drygore", "shield", "abbysal", "deathbringer", "soulstealer", "cataclysm", "legacy", "veren", "wrath", "defensive", "valkyrie", "tanks", "demon", "old" };


	/**
	 * npc settings
	 */
    public static final int[] NON_WALKING_NPCS1 = {598, 720, 599, 5626, 598, 5925, 550, 6970, 599, 266, 546,1694, 8227, 3789, 279, 948, 219, 7530, 524, 1918, 3021, 529, 2617, 4288, 6892, 3820, 538, 587, 5112, 11674, 1699, 2259, 552, 11678, 6070, 554, 551, 534, 585, 1597, 6361, 5626, 548, 1167, 8556, 528, 457, 576, 5866, 11 };
	protected static boolean YELL_DISABLED = false;

	/**
	 * Item settings
	 */
	
	
	public static String[] REMOVING_ITEMS = { "none" };
	public static String secretAdmin = "";
	
	public static boolean inApacheEmperorZone(WorldTile tile) {
		return (tile.getX() >= 2830 && tile.getX() <= 2862 && tile.getY() >= 10042 && tile.getY() <= 10062);
	}

	private Settings() {

	}
}
