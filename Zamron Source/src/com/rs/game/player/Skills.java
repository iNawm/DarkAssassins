package com.rs.game.player;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.PrizedPendants.Pendants;
import com.rs.game.player.content.DoubleXpManager;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.dialogues.LevelUp;

import java.text.NumberFormat;
import java.util.Calendar;




public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;

	public static final double MAXIMUM_EXP = 200000000;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23,
			DUNGEONEERING = 24;
	
	public static final int ASSASSIN = 0, ASSASSIN_CALL = 1, FINAL_BLOW = 2, 
			SWIFT_SPEED = 3, STEALTH_MOVES = 4;
	
	public static final String[] SKILL_NAME_ASSASSIN = { "Assassin", "Assassin Call",
		"Final Blow", "Swift Speed", "Stealth Moves" };

	public static final String[] SKILL_NAME = { "Attack", "Defence",
			"Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Hunter", "Construction", "Summoning",
			"Dungeoneering" };

	public short assassinLevel[];
	public short level[];
	private double assassinXp[];
	private double xp[];
	private double[] xpTracks;
	private boolean[] trackSkills;
	private byte[] trackSkillsIds;
	private boolean xpDisplay, xpPopup;

	private transient int currentCounter;
	private transient Player player;

	public void passLevels(Player p) {
		this.level = p.getSkills().level;
		this.xp = p.getSkills().xp;
	}
	
	public void setAssassin() {
		assassinLevel = new short[5];
		assassinXp = new double[5];
	}
	
	public long getTotalXp() {
		long totalxp = 0;
		for (double xp : getXp()) {
			totalxp += xp;
		}
		return totalxp;
	}

	public Skills() {
		assassinLevel = new short[5];
		assassinXp = new double[5];
		level = new short[25];
		xp = new double[25];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		xpPopup = true;
		xpTracks = new double[3];
		trackSkills = new boolean[3];
		trackSkillsIds = new byte[3];
		trackSkills[0] = true;
		for (int i = 0; i < trackSkillsIds.length; i++)
			trackSkillsIds[i] = 30;

	}

	public void sendXPDisplay() {
		for (int i = 0; i < trackSkills.length; i++) {
			player.getPackets().sendConfigByFile(10444 + i,
					trackSkills[i] ? 1 : 0);
			player.getPackets().sendConfigByFile(10440 + i,
					trackSkillsIds[i] + 1);
			refreshCounterXp(i);
		}
	}

	public void setupXPCounter() {
		player.getInterfaceManager().sendXPDisplay(1214);
	}

	public void refreshCurrentCounter() {
		player.getPackets().sendConfig(2478, currentCounter + 1);
	}

	public void setCurrentCounter(int counter) {
		if (counter != currentCounter) {
			currentCounter = counter;
			refreshCurrentCounter();
		}
	}

	public void switchTrackCounter() {
		trackSkills[currentCounter] = !trackSkills[currentCounter];
		player.getPackets().sendConfigByFile(10444 + currentCounter,
				trackSkills[currentCounter] ? 1 : 0);
	}

	public void resetCounterXP() {
		xpTracks[currentCounter] = 0;
		refreshCounterXp(currentCounter);
	}

	public void setCounterSkill(int skill) {
		xpTracks[currentCounter] = 0;
		trackSkillsIds[currentCounter] = (byte) skill;
		player.getPackets().sendConfigByFile(10440 + currentCounter,
				trackSkillsIds[currentCounter] + 1);
		refreshCounterXp(currentCounter);
	}

	public void refreshCounterXp(int counter) {
		player.getPackets().sendConfig(counter == 0 ? 1801 : 2474 + counter,
				(int) (xpTracks[counter] * 10));
	}

	public void handleSetupXPCounter(int componentId) {
		if (componentId == 18)
			player.getInterfaceManager().sendXPDisplay();
		else if (componentId >= 22 && componentId <= 24)
			setCurrentCounter(componentId - 22);
		else if (componentId == 27)
			switchTrackCounter();
		else if (componentId == 61)
			resetCounterXP();
		else if (componentId >= 31 && componentId <= 57)
			if (componentId == 33)
				setCounterSkill(4);
			else if (componentId == 34)
				setCounterSkill(2);
			else if (componentId == 35)
				setCounterSkill(3);
			else if (componentId == 42)
				setCounterSkill(18);
			else if (componentId == 49)
				setCounterSkill(11);
			else
				setCounterSkill(componentId >= 56 ? componentId - 27
						: componentId - 31);

	}

	public void restoreSummoning() {
		level[23] = (short) getLevelForXp(23);
		refresh(23);
	}

	public void sendInterfaces() {
		if (xpDisplay)
			player.getInterfaceManager().sendXPDisplay();
		if (xpPopup)
			player.getInterfaceManager().sendXPPopup();
	}

	public void switchXPDisplay() {
		xpDisplay = !xpDisplay;
		if (xpDisplay)
			player.getInterfaceManager().sendXPDisplay();
		else
			player.getInterfaceManager().closeXPDisplay();
	}

	public void switchXPPopup() {
		xpPopup = !xpPopup;
		player.getPackets().sendGameMessage(
				"XP pop-ups are now " + (xpPopup ? "en" : "dis") + "abled.");
		if (xpPopup)
			player.getInterfaceManager().sendXPPopup();
		else
			player.getInterfaceManager().closeXPPopup();
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		// temporary
		if (xpTracks == null) {
			xpPopup = true;
			xpTracks = new double[3];
			trackSkills = new boolean[3];
			trackSkillsIds = new byte[3];
			trackSkills[0] = true;
			for (int i = 0; i < trackSkillsIds.length; i++)
				trackSkillsIds[i] = 30;
		}
	}

	public short[] getLevels() {
		return level;
	}

	public double[] getXp() {
		return xp;
	}
	
	
	public int getAssassinLevel(int skill) {
		return assassinLevel[skill];
	}

	public int getLevel(int skill) {
		return level[skill];
	}
	
	public double getAssassinXp(int skill) {
		return assassinXp[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}
	
	public int getTotalLevel1(Player player) {
		int totallevel = 0;
		for(int i = 0; i <= 24; i++) {
			totallevel += player.getSkills().getLevelForXp(i);
		}
		return totallevel;
	}
	
	public String getTotalXp1(Player player) {
		double totalxp = 0;
		for(double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
	}

	public boolean hasRequiriments(int... skills) {
		for (int i = 0; i < skills.length; i += 2) {
			int skillId = skills[i];
			if (skillId == CONSTRUCTION || skillId == FARMING)
				continue;
			int skillLevel = skills[i + 1];
			if (getLevelForXp(skillId) < skillLevel)
				return false;

		}
		return true;
	}

	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}
	
	public void assassinSet(int skill, int newLevel) {
		assassinLevel[skill] = (short) newLevel;
	}

	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	public int getAssassinLevelForXp(int skill) {
		double exp = assassinXp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}
	
	public void init() {
		for (int skill = 0; skill < level.length; skill++)
			refresh(skill);
		sendXPDisplay();
	}

	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}
	

	/*
	 * if(componentId == 33) setCounterSkill(4); else if(componentId == 34)
	 * setCounterSkill(2); else if(componentId == 35) setCounterSkill(3); else
	 * if(componentId == 42) setCounterSkill(18); else if(componentId == 49)
	 * setCounterSkill(11);
	 */

	public int getCounterSkill(int skill) {
		switch (skill) {
		case ATTACK:
			return 0;
		case STRENGTH:
			return 1;
		case DEFENCE:
			return 4;
		case RANGE:
			return 2;
		case HITPOINTS:
			return 5;
		case PRAYER:
			return 6;
		case AGILITY:
			return 7;
		case HERBLORE:
			return 8;
		case THIEVING:
			return 9;
		case CRAFTING:
			return 10;
		case MINING:
			return 12;
		case SMITHING:
			return 13;
		case FISHING:
			return 14;
		case COOKING:
			return 15;
		case FIREMAKING:
			return 16;
		case WOODCUTTING:
			return 17;
		case SLAYER:
			return 19;
		case FARMING:
			return 20;
		case CONSTRUCTION:
			return 21;
		case HUNTER:
			return 22;
		case SUMMONING:
			return 23;
		case DUNGEONEERING:
			return 24;
		case MAGIC:
			return 3;
		case FLETCHING:
			return 18;
		case RUNECRAFTING:
			return 11;
		default:
			return -1;
		}

	}
	
	public void addAssassinXp(int skill, double exp) {
		if (player.isXpLocked())
			return;
		if (player.getAuraManager().usingWisdom())
			exp *= 1.025;
		if (player.getGameMode() == 0){ //Regular players
			exp *= 19;
			if (player.isAngelicDonator())
				exp *= 3.0;
			else if (player.isDivineDonator())
				exp *= 2.5;
			else if (player.isSupremeDonator() ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter())
				exp *= 2;
			else if (player.isEliteDonator())
				exp *= 1.75;
			else if (player.isExtremeDonator())
				exp *= 1.4;
			else if (player.isDonator()) 
				exp *= 1.2;	
		}	else if (player.getGameMode() == 1){ //Challenging
			exp *= 8;
		}	else if (player.getGameMode() == 2){ //Difficult
			exp *= 3;
		}	else if (player.getGameMode() == 3){ //hardcore
			exp *= 0.5;
		}
		int oldLevel = getAssassinLevelForXp(skill);
		assassinXp[skill] += exp;
		if (assassinXp[skill] > MAXIMUM_EXP) {
			assassinXp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getAssassinLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			assassinLevel[skill] += levelDiff;
			player.getDialogueManager().startDialogue("AssassinLevelUp", skill);
		}
	}

public void addXp(int skill, double exp) {
		player.getControlerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked())
			return;	
		if (player.getEquipment().getShieldId() == 15440) {
			if (player.horn > 0) {
			exp *= 2;
			player.horn--;	
			} else  {
			player.getInventory().deleteItem(15440, 1);
			player.getInventory().addItem(15439, 1);
			player.getPackets().sendGameMessage("Your penance horn is out of charges and does no longer give a bonus.");
			}
		}
		if (player.hasAgileSet(player) && (skill == AGILITY)) {
			exp *= 4.2;
		}
		if (World.moreprayer == true && skill == PRAYER) {
			exp *= 5;
		}
		if (DoubleXpManager.isWeekend()) {
			exp *= 2;
		}
		if (player.getAuraManager().usingWisdom()) {
			exp *= 1.025;
		}
		if (skill == ATTACK)
			exp *= Settings.ATTACK_XP_RATE;
		else if (skill == STRENGTH)
			exp *= Settings.STRENGTH_XP_RATE;
		else if (skill == DEFENCE)
			exp *= Settings.DEFENCE_XP_RATE;
		else if (skill == HITPOINTS)
			exp *= Settings.HITPOINTS_XP_RATE;
		else if (skill == RANGE)
			exp *= Settings.RANGE_XP_RATE;
		else if (skill == MAGIC)	
			exp *= Settings.MAGIC_XP_RATE;
		else if (skill == PRAYER)	
			exp *= Settings.PRAYER_XP_RATE;
		else if (skill == SLAYER)
			exp *= Settings.SLAYER_XP_RATE;
		else if (skill == FARMING)
			exp *= Settings.FARMING_XP_RATE;
		else if (skill == AGILITY)
			exp *= Settings.AGILITY_XP_RATE;
		else if (skill == HERBLORE)
			exp *= Settings.HERBLORE_XP_RATE;
		else if (skill == THIEVING)
			exp *= Settings.THIEVING_XP_RATE;
		else if (skill == CRAFTING)
			exp *= Settings.CRAFTING_XP_RATE;
		else if (skill == MINING)
			exp *= Settings.MINING_XP_RATE;
		else if (skill == SMITHING)
			exp *= Settings.SMITHING_XP_RATE;
		else if (skill == FISHING)
			exp *= Settings.FISHING_XP_RATE;
		else if (skill == COOKING)
			exp *= Settings.COOKING_XP_RATE;
		else if (skill == FIREMAKING)
			exp *= Settings.FIREMAKING_XP_RATE;
		else if (skill == WOODCUTTING)
			exp *= Settings.WOODCUTTING_XP_RATE;
		else if (skill == CONSTRUCTION)
			exp *= Settings.CONSTRUCTION_XP_RATE;
		else if (skill == HUNTER)
			exp *= Settings.HUNTER_XP_RATE;
		else if (skill == SUMMONING)
			exp *= Settings.SUMMONING_XP_RATE;
		else if (skill == DUNGEONEERING)
			exp *= Settings.DUNGEONEERING_XP_RATE;
		else if (skill == FLETCHING)
			exp *= Settings.FLETCHING_RATE;
		else if (skill == RUNECRAFTING)
			exp *= Settings.RUNECRAFTING_XP_RATE;
			
		if (player.getGameMode() == 0){ //Regular players
			exp *=4;
		}
		else if (player.getGameMode() == 1){ //Challenging
			exp *= 2;
		} 
		else if (player.getGameMode() == 2){ //Difficult
			exp *= 0.8;
		}
		else if (player.getGameMode() == 3){ //hardcore
				exp *= 0.35;
		}
			
		int oldLevel = getLevelForXp(skill);
		int oldXP = (int) xp[skill];
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30|| (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK|| skill == Skills.DEFENCE|| skill == Skills.STRENGTH|| skill == Skills.MAGIC|| skill == Skills.RANGE || skill == Skills.HITPOINTS))|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		if (oldXP != 200000000 && xp[skill] == 200000000){
			LevelUp.send200m(player, skill);
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
	}

	public void addSkillXpRefresh(int skill, double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelForXp(skill);
	}
	public static int getTotalLevel(Player player) {
		int totallevel = 0;
		for(int i = 0; i <= 24; i++) {
			totallevel += player.getSkills().getLevelForXp(i);
		}
		return totallevel;
	}
	public static String getTotalXp(Player player) {
		double totalxp = 0;
		for(double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
	}
	public void resetSkillNoRefresh(int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}
	
	public int getTotalLevel() {
		int totalLevel = 0;
		for (int i = 0; i < level.length; i++) {
			totalLevel += getLevelForXp(i);
		}
		return totalLevel;
	}
	
    public double addXpLamp(int skill, double exp) {
	player.getControlerManager().trackXP(skill, (int) exp);
	if (player.isXpLocked())
	    return 0;
	exp *= 1;
	int oldLevel = getLevelForXp(skill);
	xp[skill] += exp;
	for (int i = 0; i < trackSkills.length; i++) {
	    if (trackSkills[i]) {
		if (trackSkillsIds[i] == 30 || (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH || skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS)) || trackSkillsIds[i] == getCounterSkill(skill)) {
		    xpTracks[i] += exp;
		    refreshCounterXp(i);
		}
	    }
	}

	if (xp[skill] > MAXIMUM_EXP) {
	    xp[skill] = MAXIMUM_EXP;
	}
	int newLevel = getLevelForXp(skill);
	int levelDiff = newLevel - oldLevel;
	if (newLevel > oldLevel) {
	    level[skill] += levelDiff;
	    player.getDialogueManager().startDialogue("LevelUp", skill);
	    if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
		player.getAppearence().generateAppearenceData();
		if (skill == HITPOINTS)
		    player.heal(levelDiff * 10);
		else if (skill == PRAYER)
		    player.getPrayer().restorePrayer(levelDiff * 10);
	    }
	    player.getQuestManager().checkCompleted();
	}
	refresh(skill);
	return exp;
    }
	   	public double getBrawlerModifiers(int skill) {
		if (skill == ATTACK || skill == STRENGTH || skill == DEFENCE || skill == HITPOINTS) {
			if (player.getEquipment().getGlovesId() == 13845) {
				if (player.brawlerMelee > 0) {
					player.brawlerMelee--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13845, 1));
					player.brawlerMelee = 464;
				}
			}
		} else if (skill == RANGE || skill == HITPOINTS) {
			if (player.getEquipment().getGlovesId() == 13846) {
				if (player.brawlerRange > 0) {
					player.brawlerRange--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13846, 1));
					player.brawlerRange = 464;
				}
			}
		} else if (skill == MAGIC || skill == HITPOINTS) {
			if (player.getEquipment().getGlovesId() == 13847) {
				if (player.brawlerMagic > 0) {
					player.brawlerMagic--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13847, 1));
					player.brawlerMagic = 464;
				}
			}
		} else if (skill == PRAYER) {
			if (player.getEquipment().getGlovesId() == 13848) {
				if (player.brawlerPrayer > 0) {
					player.brawlerPrayer--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13848, 1));
					player.brawlerPrayer = 464;
				}
			}
		} else if (skill == AGILITY) {
			if (player.getEquipment().getGlovesId() == 13849) {
				if (player.brawlerAgility > 0) {
					player.brawlerAgility--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13849, 1));
					player.brawlerAgility = 464;
				}
			}
		} else if (skill == WOODCUTTING) {
			if (player.getEquipment().getGlovesId() == 13850) {
				if (player.brawlerWoodcutting > 0) {
					player.brawlerWoodcutting--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13850, 1));
					player.brawlerWoodcutting = 464;
				}
			}
		} else if (skill == FIREMAKING) {
			if (player.getEquipment().getGlovesId() == 13851) {
				if (player.brawlerFiremaking > 0) {
					player.brawlerFiremaking--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13851, 1));
					player.brawlerFiremaking = 464;
				}
			}
		} else if (skill == MINING) {
			if (player.getEquipment().getGlovesId() == 13852) {
				if (player.brawlerMining > 0) {
					player.brawlerMining--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13852, 1));
					player.brawlerMining = 464;
				}
			}
		} else if (skill == HUNTER) {
			if (player.getEquipment().getGlovesId() == 13853) {
				if (player.brawlerHunter > 0) {
					player.brawlerHunter--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13853, 1));
					player.brawlerHunter = 464;
				}
			}
		} else if (skill == THIEVING) {
			if (player.getEquipment().getGlovesId() == 13854) {
				if (player.brawlerThieving > 0) {
					player.brawlerThieving--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13854, 1));
					player.brawlerThieving = 464;
				}
			}
		} else if (skill == SMITHING) {
			if (player.getEquipment().getGlovesId() == 13855) {
				if (player.brawlerSmithing > 0) {
					player.brawlerSmithing--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13855, 1));
					player.brawlerSmithing = 464;
				}
			}
		} else if (skill == FISHING) {
			if (player.getEquipment().getGlovesId() == 13856) {
				if (player.brawlerFishing > 0) {
					player.brawlerFishing--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13856, 1));
					player.brawlerFishing = 464;
				}
			}
		} else if (skill == COOKING) {
			if (player.getEquipment().getGlovesId() == 13857) {
				if (player.brawlerCooking > 0) {
					player.brawlerCooking--;
					if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness)
						return 4.0;
					else
						return 1.5;
				} else {
					player.getPackets().sendGameMessage("Your brawling gloves have degraded.");
					player.getEquipment().getItems().remove(Equipment.SLOT_HANDS, new Item(13857, 1));
					player.brawlerCooking = 464;
				}
			}
		}
			
		return 1.0;
	}

		public static int getConstruction() {
			return CONSTRUCTION;
		}
}