package com.rs.game.player.dialogues;

import java.sql.SQLException;

import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
//import com.rs.utils.AdventureLog;

public final class LevelUp extends Dialogue {

	/*
	 * public static final int[] SKILL_ICON = { 100000000, 400000000, 200000000,
	 * 450000000, 250000000, 500000000, 300000000, 1100000000, 1250000000,
	 * 1300000000, 1050000000, 1200000000, 800000000, 1000000000, 900000000,
	 * 650000000, 600000000, 700000000, 1400000000, 1450000000, 850000000,
	 * 1500000000, 1600000000, 1650000000, 1700000000 };
	 */

	public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 37, 37, 37, 37,
			37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1,
			-1, -1, 300, 417 };

	private static int skill;

	@Override
	public void start() {
		skill = (Integer) parameters[0];
		int level = player.getSkills().getLevelForXp(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp[" + skill + "]",
				Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		if (level == 99 || level == 120)
			player.setNextGraphics(new Graphics(1765));
		player.getInterfaceManager().sendChatBoxInterface(740);
		String name = Skills.SKILL_NAME[skill];
		player.getPackets().sendIComponentText(
				740,
				0,
				"Congratulations, you have just advanced a"
						+ (name.startsWith("A") ? "n" : "") + " " + name
						+ " level!");
		player.getPackets().sendIComponentText(740, 1,
				"You have now reached level " + level + ".");
		player.getPackets().sendGameMessage(
				"You've just advanced a" + (name.startsWith("A") ? "n" : "")
						+ " " + name + " level! You have reached level "
						+ level + ".");
		player.getPackets().sendConfigByFile(4757, getIconValue(skill));
		switchFlash(player, skill, true);
		int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
		if (musicEffect != -1)
			player.getPackets().sendMusicEffect(musicEffect);
		if (level == 99 || level == 120) {
			sendNews(player, skill, level);
			String moof1 = "has achieved " +level+ " in"; /*
				try {
					AdventureLog.createConnection();
					AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"', '"+Skills.SKILL_NAME[skill]+"');");
					AdventureLog.destroyConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("[SQLMANAGER] Query Executed.");
				AdventureLog.destroyConnection(); */
		}
	}

	public static void sendNews(Player player, int skill, int level) {
		boolean reachedAll = true;
		for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
			if (player.getSkills().getLevelForXp(i) < 99) {
				reachedAll = false;
				break;
			}
		}
		if (!reachedAll) {
			if (player.gameMode == 3) {
				player.getBank().addItem(19467, 10, true);
				player.getPackets().sendGameMessage("<col=008000>You have recieved 10 Biscuits for playing for achieving a 99 skill!");
			} else if (player.gameMode == 2) {
				player.getBank().addItem(19467, 1, true);
				player.getPackets().sendGameMessage("<col=008000>You have recieved 1 Biscuits for playing for achieving a 99 skill!");
			}
			player.getSquealOfFortune().giveEarnedSpins(2);
			player.sm("<col=008000>You have been awarded two Squeal of Fortune spins!");
			if (player.inform99s == true) {
			String gameMode = "";
			if (player.gameMode == 3) {
				gameMode = "Veteran";
			} else if (player.gameMode == 2) {
				gameMode = "Difficult";
			} else if (player.gameMode == 1) {
				gameMode = "Challenging";
			} else if (player.gameMode == 0) {
				gameMode = "Regular";
			//} else if (player.isIronMan) {
				//gameMode = "Iron Man";
			}
			World.sendWorldMessage("<img=6><col=ff8c38>News: " + player.getDisplayName() + " has achieved " + level + " " + Skills.SKILL_NAME[skill] + " using the game mode "+gameMode+".", false);
		
			}
			return;
	}
		player.getSquealOfFortune().giveEarnedSpins(2);
		player.getBank().addItem(20767, 1, true);
		player.getBank().addItem(20768, 1, true);
		player.isMaxed = 1;
		player.sendMessage("<col=008000>You have been awarded two Squeal of Fortune spins!");
		player.sendMessage("<col=008000>Your Max Cape and Hood awaits you in your bank!");
		if (player.inform99s == true) {
		World.sendWorldMessage("<img=6><col=ff0000>News: " + player.getDisplayName() + " has just achieved at least level 99 in all skills!", false);
		String moof1 = "has achieved 99 in";
		String moof2 = "all skills"; /*
		try {
			AdventureLog.createConnection();
			AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"', '"+moof2+"');");
			AdventureLog.destroyConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[SQLMANAGER] Query Executed."); */
		}
}
	
public static void send200m(Player player, int skill) {
			World.sendWorldMessage("<img=6><col=ff8c38>News: " + player.getDisplayName()+ " has achieved " + "200m experience in" + " "+ Skills.SKILL_NAME[skill] + ".", false);
			String moof1 = "has achieved 200m xp in"; /*
				try {
					AdventureLog.createConnection();
					AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"', '"+Skills.SKILL_NAME[skill]+"');");
					AdventureLog.destroyConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("[SQLMANAGER] Query Executed.");
			return; */
			
	}

	public static int getIconValue(int skill) {
		if (skill == Skills.ATTACK)
			return 1;
		if (skill == Skills.STRENGTH)
			return 2;
		if (skill == Skills.RANGE)
			return 3;
		if (skill == Skills.MAGIC)
			return 4;
		if (skill == Skills.DEFENCE)
			return 5;
		if (skill == Skills.HITPOINTS)
			return 6;
		if (skill == Skills.PRAYER)
			return 7;
		if (skill == Skills.AGILITY)
			return 8;
		if (skill == Skills.HERBLORE)
			return 9;
		if (skill == Skills.THIEVING)
			return 10;
		if (skill == Skills.CRAFTING)
			return 11;
		if (skill == Skills.RUNECRAFTING)
			return 12;
		if (skill == Skills.MINING)
			return 13;
		if (skill == Skills.SMITHING)
			return 14;
		if (skill == Skills.FISHING)
			return 15;
		if (skill == Skills.COOKING)
			return 16;
		if (skill == Skills.FIREMAKING)
			return 17;
		if (skill == Skills.WOODCUTTING)
			return 18;
		if (skill == Skills.FLETCHING)
			return 19;
		if (skill == Skills.SLAYER)
			return 20;
		if (skill == Skills.FARMING)
			return 21;
		if (skill == Skills.CONSTRUCTION)
			return 22;
		if (skill == Skills.SLAYER)
			return 23;
		if (skill == Skills.SUMMONING)
			return 24;
		return 25;
	}

	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		if (skill == Skills.ATTACK)
			id = 4732;
		else if (skill == Skills.STRENGTH)
			id = 4733;
		else if (skill == Skills.DEFENCE)
			id = 4734;
		else if (skill == Skills.RANGE)
			id = 4735;
		else if (skill == Skills.PRAYER)
			id = 4736;
		else if (skill == Skills.MAGIC)
			id = 4737;
		else if (skill == Skills.HITPOINTS)
			id = 4738;
		else if (skill == Skills.AGILITY)
			id = 4739;
		else if (skill == Skills.HERBLORE)
			id = 4740;
		else if (skill == Skills.THIEVING)
			id = 4741;
		else if (skill == Skills.CRAFTING)
			id = 4742;
		else if (skill == Skills.FLETCHING)
			id = 4743;
		else if (skill == Skills.MINING)
			id = 4744;
		else if (skill == Skills.SMITHING)
			id = 4745;
		else if (skill == Skills.FISHING)
			id = 4746;
		else if (skill == Skills.COOKING)
			id = 4747;
		else if (skill == Skills.FIREMAKING)
			id = 4748;
		else if (skill == Skills.WOODCUTTING)
			id = 4749;
		else if (skill == Skills.RUNECRAFTING)
			id = 4750;
		else if (skill == Skills.SLAYER)
			id = 4751;
		else if (skill == Skills.FARMING)
			id = 4752;
		else if (skill == Skills.CONSTRUCTION)
			id = 4753;
		else if (skill == Skills.HUNTER)
			id = 4754;
		else if (skill == Skills.SUMMONING)
			id = 4755;
		else
			id = 7756;
		player.getPackets().sendConfigByFile(id, on ? 1 : 0);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {
		// player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes
		// random flash
	}
}
