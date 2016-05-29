
package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.QuestManager.Quests;

public class ItemConstants {

	public static int[] charmIds = new int[] { 12158, 12159, 12160, 12163 };
	public static int getDegradeItemWhenWear(int id) {
		// pvp armors
		if ( id == 13908 || id == 13911 || id == 13914
				|| id == 13917 || id == 13920 || id == 13923 || id == 13926
				|| id == 13929 || id == 13932 || id == 13935 || id == 13938
				|| id == 13941 || id == 13944 || id == 13947 || id == 13950)
			return id + 2; // if you wear it it becomes corrupted LOL
		return -1;
	}
	
	public static boolean isDungItem(int itemId) {
		if (itemId >= 15750 && itemId <= 18329)
			return true;
		return false;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		if (id == 4708 || id == 4856 || id == 4857 || id == 4858 || id == 4859 || id == 4712 || id == 4868 || id == 4869 || 
		    id == 4870 || id == 4871 || id == 4714 || id == 4874 || id == 4875 || id == 4876 || id == 4877 )
		return 100;
		// pvp armors //Leighton has disabled this for pvp armours 
		/* if (id == 13910 || id == 13913 || id == 13916 || id == 13919
				|| id == 13922 || id == 13925 || id == 13928 || id == 13931
				|| id == 13934 || id == 13937 || id == 13940 || id == 13943
				|| id == 13946 || id == 13949 || id == 13952)
			return 1500;
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969
				|| id == 13972 || id == 13975)
			return 3000;
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869
				|| id == 13872 || id == 13875 || id == 13878 || id == 13886
				|| id == 13889 || id == 13892 || id == 13895 || id == 13898
				|| id == 13901 || id == 13904 || id == 13907 || id == 13960 || id == 13958 || id == 13961 || id == 13964 || id == 13967
				|| id == 13970 || id == 13973 || id == 13858 || id == 13861
				|| id == 13864 || id == 13867 || id == 13870 || id == 13873
				|| id == 13876 || id == 13884 || id == 13887 || id == 13890
				|| id == 13893 || id == 13896 || id == 13899 || id == 13902
				|| id == 13905)
			return 12000; // 1hour */
				//chaotics
		/*
		if (id == 18349 || id == 18351 || id == 18353 || id == 18355 || id == 18357 || id == 18359 || id == 18361 || id == 18363)
			return 60000;
			*/
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169 || id == 20173)
			return 60000;
		return -1;
	}

	// return what id it degrades to, -1 for disappear which is default so we
	// don't add -1
	public static int getItemDegrade(int id) {
		if (id == 11285) // DFS
			return 11283;
		if (id == 18349)
			return 18350;
		if (id == 18351)
			return 18352;
		if (id == 18353)
			return 18354;
		if (id == 18355)
			return 18356;
		if (id == 18357)
			return 18358;
		if (id == 18359)
			return 18360;
		if (id == 18361)
			return 18362;
		if (id == 18363)
			return 18364;
		if (id == 2859)
			return + 1;
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169 || id == 20173)
			return id + 1;
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		// nex armors
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147
				|| id == 20151 || id == 20155 || id == 20159 || id == 20163
				|| id == 20167 || id == 20171)
			return id + 2;
		if (id == 4708 )
		return 4856;
		if (id == 4856)
		return 4857;
		if (id == 4857)
		return 4858;
		if (id == 4858)
		return 4859;
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967
				|| id == 13970 || id == 13973 || id == 13858 || id == 13861
				|| id == 13864 || id == 13867 || id == 13870 || id == 13873
				|| id == 13876 || id == 13884 || id == 13887 || id == 13890
				|| id == 13893 || id == 13896 || id == 13899 || id == 13902
				|| id == 13905)
		return id + 2; // if you wear it it becomes corrupted LOL
		if (id == 18349)
			return 18350;
		if (id == 18351)
			return 18352;
		if (id == 18353)
			return 18354;
		if (id == 18355)
			return 18356;
		if (id == 18357)
			return 18358;
		if (id == 18359)
			return 18360;
		if (id == 18361)
			return 18362;
		if (id == 18363)
			return 18364;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550)
			return true;
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		if (name.contains("c. dragon") || name.contains("corrupt dragon") || name.contains("corrupt"))
			return true;
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		// nex armors
		if (name.contains("torva") || name.contains("pernix") || name.contains("ahrim") || name.contains("guthan") || name.contains("dharok") || name.contains("verac") || name.contains("torag") || name.contains("karil")|| name.contains("virtus") || name.contains("Zaryte") || name.contains("vesta's") || name.contains("statius'") || name.contains("morrigan's") || name.contains("zuriel's"))
			return true;
		return false;
	}

	public static boolean canWear(Item item, Player player) {
		String itemName = item.getName();
		if (item.getId() == 14640 || item.getId() == 14645 || item.getId() == 15433 || item.getId() == 15430
				|| item.getId() == 14639
				|| item.getId() == 15432
				|| item.getId() == 15434) {
				player.getPackets().sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape.");
				return false;
			}
			
			for (String strings : Settings.DONATOR_ITEMS) {
			if (itemName.contains(strings) && !player.isDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be a donator to equip " + itemName + ".");
				return true;
			}
		}
		for (String strings : Settings.EXTREME_DONATOR_ITEMS) {
			if (itemName.contains(strings) && !player.isExtremeDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be Extreme Donator+ to equip " + itemName + ".");
				return false;
			}
		}
		if (item.getId() == 20767 || item.getId() == 20768) {
			if (player.getSkills().getTotalLevel(player) <= 2475) {
				player.getPackets().sendGameMessage("You need a total level of 2475 to wear this cape.");
				return false;
		}
		}
		/*if (item.getId() == 28872 || item.getId() == 28873 || item.getId() == 28874 || item.getId() == 28852 || 
			item.getId() == 29053 || item.getId() == 29054 || item.getId() == 29055 || item.getId() == 29056 || item.getId() == 29057
			|| item.getId() == 29058 || item.getId() == 29059 || item.getId() == 29060 || item.getId() == 28801 || item.getId() == 28802 
			|| item.getId() == 28803 || item.getId() == 28804 || item.getId() == 28805 || item.getId() == 29012 || item.getId() == 29013
			|| item.getId() == 29014 || item.getId() == 29015) {
			if (player.prestigeNumber !=20) {
				player.getPackets().sendGameMessage("You need to be prestige 20 to wear this!");
				return false;
			}
			
		}
		
		if (item.getId() == 29026 || item.getId() == 29027 || item.getId() == 28953 || item.getId() == 28955 || 
			item.getId() == 28824 || item.getId() == 28825 || item.getId() == 28827 || item.getId() == 28828 || item.getId() == 28865) {
			if (player.prestigeNumber <= 14) {
					player.getPackets().sendGameMessage("You need to be prestige 15 to wear this!");
				return false;
			}
			
		}
		
		if (item.getId() == 28997 || item.getId() == 28998 || item.getId() == 28999 || item.getId() == 29000 || 
			item.getId() == 29001 || item.getId() == 29002 || item.getId() == 29003 || item.getId() ==  29004 || 
			item.getId() == 29005 || item.getId() == 29006 || item.getId() == 29007 || item.getId() == 29085 || 
			item.getId() == 29086 || item.getId() == 29087 || item.getId() == 29088 ) {
			if (player.prestigeNumber <= 4) {
				player.getPackets().sendGameMessage("You need to be prestige 5 to wear this!");
				return false;
			}
		
		}
		
		if (item.getId() == 28869 || item.getId() == 28870 || item.getId() == 28880 || item.getId() == 29030 || 
			item.getId() == 29045 || item.getId() == 28821 || item.getId() == 29079 || item.getId() == 29080 || 
			item.getId() == 29081 || item.getId() == 29082) {
			if (player.prestigeNumber <= 9) {
								player.getPackets().sendGameMessage("You need to be prestige 10 to wear this!");
				return false;
			}
			
		}*/
			
		else if (item.getId() == 28883) {
			if (player.getSkills().getXp(Skills.MINING) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m mining XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28884) {
			if (player.getSkills().getXp(Skills.AGILITY) != 200000000) {
				player.getPackets().sendGameMessage("You need 200M agility XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28885) {
			if (player.getSkills().getXp(Skills.ATTACK) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m attack XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28886) {
			if (player.getSkills().getXp(Skills.CONSTRUCTION) != 200000000) {
				player.getPackets().sendGameMessage("You need 200M construction XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28887) {
			if (player.getSkills().getXp(Skills.COOKING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200M cooking XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28888) {
			if ( player.getSkills().getXp(Skills.CRAFTING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m crafting XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28889) {
			if ( player.getSkills().getXp(Skills.DEFENCE) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m defence xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28890) {
			if (player.getSkills().getXp(Skills.FARMING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m farming xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28891) {
			if (player.getSkills().getXp(Skills.FIREMAKING) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m firemaking XP to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28892) {
			if (player.getSkills().getXp(Skills.FISHING) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m fishing xp to wear this cape.");
				return false;
			}
		}
		
	else if(item.getId() >= 20769 && item.getId() <= 20772) {
			if (!player.hasRequirements()) {
				player.getPackets().sendGameMessage("You do not meet all the requirements");
			return false;
			}
		}
		else if (item.getId() == 28893) {
			if (player.getSkills().getXp(Skills.FLETCHING) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m fletching xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28894) {
			if (player.getSkills().getXp(Skills.HERBLORE) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m herblore xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28895) {
			if (player.getSkills().getXp(Skills.HITPOINTS) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200M constitution xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28896) {
			if (player.getSkills().getXp(Skills.HUNTER) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m hunter xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28897) {
			if (player.getSkills().getXp(Skills.MAGIC) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m magic xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28898) {
			if (player.getSkills().getXp(Skills.PRAYER) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m prayer xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28999) {
			if  (player.getSkills().getXp(Skills.RANGE) != 200000000) {
				player.getPackets().sendGameMessage("You need 200m range xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28900) {
			if (player.getSkills().getXp(Skills.RUNECRAFTING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m runecrafting xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28901) {
			if (player.getSkills().getXp(Skills.SLAYER) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m slayer xp to wear this cape.");
				return false;
			}
		}
		if(item.getId() >= 20769 && item.getId() <= 20772) {
			if (!player.isCompletedComp()) {
				player.getPackets().sendGameMessage("You do not meet all the requirements, for a list of them use ::requirements.");
			return false;
			}
		}
		else if (item.getId() == 28902) {
			if (player.getSkills().getXp(Skills.SMITHING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m smithing xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28903) {
			if (player.getSkills().getXp(Skills.STRENGTH) != 200000000  ) {
				player.getPackets().sendGameMessage("You need 200m strength xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28904) {
			if (player.getSkills().getXp(Skills.SUMMONING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m summoning xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28905) {
			if (player.getSkills().getXp(Skills.THIEVING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m thieving xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28906) {
			if (player.getSkills().getXp(Skills.WOODCUTTING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m woodcutting xp to wear this cape.");
				return false;
			}
		}
		else if (item.getId() == 28907) {
			if (player.getSkills().getXp(Skills.THIEVING) != 200000000 ) {
				player.getPackets().sendGameMessage("You need 200m thieving xp to wear this cape.");
				return false;
			}
		}
		
		return true;
}
	public static boolean isTradeable(Item item) {
		if (item.getDefinitions().getName().toLowerCase().contains("flaming skull"))
			return false;
		 String name = ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase();
		if (name.contains("flaming skull") || item.getDefinitions().getName().toLowerCase().equals("top hat") || item.getDefinitions().getName().toLowerCase().equals("bunny ears") || name.contains("gravite")
				|| name.contains("arcane stream") || name.contains("arcane blast") || name.contains("arcane pulse") || name.contains("veteran cape") || name.contains("twisted bird skull")
				|| name.contains("bonecrusher") || name.contains("frozen key") || name.contains("herbicide") || name.contains("split dragontooth") || name.contains("demon horn necklace") || name.contains("baron shark") || name.contains("(i)") ||name.contains("(deg") || name.contains("(clas") || name.contains("null") || name.contains("overload") || name.contains("extreme") || name.contains("jericho") || name.contains("dreadnip") || name.contains("max hood") || name.contains("max cape") || name.contains("dungeoneering") || name.contains("vine whip") || name.contains("tokhaar-ka") || name.contains("culinaromancer") || name.contains("spin ticket") || name.contains("tokkul") || name.contains("fighter") || name.contains("fire cape") || name.contains("lamp") || name.contains("completion") || name.contains("arcane stream") || name.contains("arcane pulse") || name.contains("arcane blast") || name.contains("magical blastbox") || name.contains("cape (t)") || name.contains("penance") || name.contains("defender") || name.contains("charm") || name.contains("keenblade") || name.contains("quickbow") || name.contains("mindspike") || name.contains("baby troll") || name.contains("aura") || name.contains("scroll") || name.contains("token") || name.contains("completionist") || name.contains("max cape") || name.contains("skill cape") || name.contains("defender") || name.contains("tokhaar-kal"))
			return false;
		switch (item.getId()) {
		case 6529: //tokkul
		case 24155:
		case 24154:
		case 15098:
		case 15099: 
		case 23679: 
		case 23680:
		case 23681:
		case 28872:
		case 28873:
		case 28874:
		case 29052:
		case 29053:
		case 29054:
		case 29055:
		case 29056:
		case 29057:
		case 29058:
		case 29059:
		case 29060:
		case 29026:
		case 29027:
		case 28953:
		case 28955:
		case 28824:
		case 28825: 
		case 28827: 
		case 28828: 
		case 28865:
		case 28801:
		case 28802:
		case 28803:
		case 28804:
		case 28805:
		case 23682:
		case 23683:
		case 23684:
		case 23685:
		case 23686:
		case 23687:
		case 20786:
		case 23688:
		case 23689:
		case 23690:
		case 23691:
		case 23692:
		case 23693:
		case 23694:
		case 23695:
		case 23696:
		case 23697:
		case 23698:
		case 23699:
		case 23700:
			return false;
		default:
			return true;
		}
	}
	
	public static int getAssassinModels(int slot, boolean male) {
		switch (slot) {
			case Equipment.SLOT_HANDS:
				return male ? 71809 : 71864;
			case Equipment.SLOT_FEET:
				return male ? 71804 : 71862;
			case Equipment.SLOT_LEGS:
				return male ? 71821 : 71876;
			case Equipment.SLOT_CHEST:
				return male ? 71832 : 71893;
			case Equipment.SLOT_HAT:
				return male ? 71816 : 75135;
			case Equipment.SLOT_CAPE:
				return male ? 58 : 58;
			case Equipment.SLOT_WEAPON:
				return male ? 71853 : 71853;
			case Equipment.SLOT_SHIELD:
				return male ? 71853 : 71853;
		}
		return -1;
	}
		
		public static int getDemonFleshModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_HANDS:
				return male ? 82777 : 82777;
			case Equipment.SLOT_FEET:
				return male ? 82854 : 82778;
			case Equipment.SLOT_LEGS:
				return male ? 82853 : 82852;
			case Equipment.SLOT_CHEST:
				return male ? 82801 : 82800;
			case Equipment.SLOT_HAT:
				return male ? 82892 : 82892;
			case Equipment.SLOT_CAPE:
				return male ? 82756 : 82756;
			}
			return -1;
		}

		public static int getSwashbuclerModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_FEET:
				return male ? 71803 : 71861;
			case Equipment.SLOT_LEGS:
				return male ? 71819 : 71875;
			case Equipment.SLOT_CHEST:
				return male ? 71831 : 71890;
			case Equipment.SLOT_HAT:
				return male ? 71814 : 71872;
			case Equipment.SLOT_CAPE:
				return male ? 245 : 245;
			}
			return -1;
		}

		public static int getColoseumModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_FEET:
				return male ? 71802 : 71860;
			case Equipment.SLOT_LEGS:
				return male ? 71818 : 71879;
			case Equipment.SLOT_CHEST:
				return male ? 71835 : 71892;
			case Equipment.SLOT_HAT:
				return male ? 71813 : 71873;
			}
			return -1;
		}

		public static int getKalphiteSentimentelModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_HANDS:
				return male ? 82915 : 82915;
			case Equipment.SLOT_FEET:
				return male ? 82818 : 82818;
			case Equipment.SLOT_LEGS:
				return male ? 82912 : 82911;
			case Equipment.SLOT_CHEST:
				return male ? 82910 : 82917;
			case Equipment.SLOT_HAT:
				return male ? 82918 : 82918;
			case Equipment.SLOT_CAPE:
				return male ? 82829 : 82830;
			}
			return -1;
		}

		public static int getTokhaarWarlordModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_HANDS:
				return male ? 81106 : 81106;
			case Equipment.SLOT_FEET:
				return male ? 81099 : 81099;
			case Equipment.SLOT_LEGS:
				return male ? 81124 : 81160;
			case Equipment.SLOT_CHEST:
				return male ? 81137 : 81174;
			case Equipment.SLOT_HAT:
				return male ? 81115 : 81153;
			}
			return -1;
		}

		public static int getKrilGoldcrusherModels(int slot, boolean male) {
			switch (slot) {
			case Equipment.SLOT_HANDS:
				return male ? 78586 : 78586;
			case Equipment.SLOT_FEET:
				return male ? 78584 : 78584;
			case Equipment.SLOT_LEGS:
				return male ? 78593 : 78621;
			case Equipment.SLOT_CHEST:
				return male ? 78601 : 78622;
			case Equipment.SLOT_HAT:
				return male ? 78592 : 78617;
			}
			return -1;
		}


    public static boolean isDestroy(Item item) {
    	return item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended();
    }
}
