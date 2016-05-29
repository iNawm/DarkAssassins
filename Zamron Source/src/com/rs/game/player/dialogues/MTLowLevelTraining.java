package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;

public class MTLowLevelTraining extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Training",
				"Cows",
				"Yaks",
				"Rock Crabs",
				"Ghouls",
				"Other");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1 ) {
				sendOptionsDialogue("Which Area?",
						"North",
						"East"); 
				stage = 1;
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2325, 3795, 0));
			} else if (componentId == OPTION_3) {
					sendOptionsDialogue("Which Area?",
							"East",
							"West"); 
					stage = 2;
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Which Area?",
						"North",
						"South"); 
				stage = 3;
			
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Bandit Camp", "Gnome Khazard Battlefield", "", "", "more"); 
				stage = 4;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3175, 3317, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3260, 3264, 0));
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2704, 3718, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2678, 3717, 0));
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3416, 3511, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3436, 3466, 0));
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3170, 2981, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2527, 3202, 0));
			} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Training", "Stronghold of Security", "Stronghold of Player Safety", "Lumbridge Swamp", "Karamja Volcano", "More");
			stage = 75;
			}
		}if (stage == 75) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3081, 3421, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3074, 3456, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3198, 3177, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2855, 3168, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training","Varrock Sewer","Draynor Sewer", "Edgeville Dungeon", "", "More");
				stage = 100;
			}
		} else if (stage == 100) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3237, 3459, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3083, 3272, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3095, 3468, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training","Lumbridge Catacombs","White Wolf Mountain","Experiments","Mountain Trolls","Other");
				stage = 5;
			}
		}if (stage == 5) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3246, 3198, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2846, 3497, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3576, 9927, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2875, 3578, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Chaos Druids",  "Ardougne Training Camp", "Zogre Infestation", "Iron Dragons", "More");
				stage = 6;
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2928, 9844, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2517, 3356, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2462, 3048, 0));
			}else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2911, 3933, 0));
			}else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Bronze Dragons", "Living Rock Caverns", "Tzhaar City", "Elite Knights", "More");
				stage = 7;
			}
		}else if (stage == 7) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2421, 4690, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3653, 5115, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2845, 3170, 0));
			}else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3026, 9953, 1));
			}else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Chaos Battlefield", "Haunted Woods", "Scabaras Swamp", "Tolna's Rift", "More");
				stage = 8;
			}
		}else if (stage == 8) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1518, 4705, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3548, 3512, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3398, 2758, 0));
			}else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3312, 3453, 0));
			}else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Ape Atoll Temple", "Tirannwn Elf Camp", "Evil Chicken's Lair", "Ogre Enclave", "More");
				stage = 9;
			}
		}else if (stage == 9) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2787, 2786, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2203, 3253, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1576, 4363, 0));
				player.sm("Disabled.");
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2589, 9411, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training",
						"Gorak Plane",
						"Ourania Cave",
						"",
						"",
						"More");
				stage = 10;
			}
		} else if (stage == 10) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3038, 5346, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3271, 4861, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Training", "Chickens", "Cows", "Yaks", "Armored zombies", "More Options");
				stage = 12;
			}
		}else if (stage == 12) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3234, 3294, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3258, 3276, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3241, 10000, 0));
			} else if (componentId == OPTION_5) {
				stage = 13;
				sendOptionsDialogue("Where would you like to go?", "Slayer tower", "Frost dragons", "Living rock caverns", "Rune essence mine", "More Options");
			}
		} else if (stage == 13) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3421, 3537, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2931, 3899, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3654, 5115, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2897, 4845, 0));
			} else if (componentId == OPTION_5) {
				stage = 14;
				sendOptionsDialogue("Where would you like to go?", "Polypore dungeon (bottom level)", "Glacors", "Jadinko lair", "Godwars dungeon", "More Options");
			}
		} else if (stage == 14) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4701, 5608, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4183, 5726, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2952, 2954, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2914, 3746, 0));
			} else if (componentId == OPTION_5) {
				stage = 15;
				sendOptionsDialogue("Where would you like to go?", "Taverly dungeon", "Revenants (PVP)", "Cyclops (Defenders)", "Dagannoth Kings", "More Options");
			}
		} else if (stage == 15) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2892, 9784, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3079, 10058, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2843, 3535, 2));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2900, 4449, 0));
			} else if (componentId == OPTION_5) {
				stage = 6;
				sendOptionsDialogue("Where would you like to go?", "Dungeoneering", "Skeletal wyverns", "Kalphite Queen", "Barrelchest", "More Options");
			}
		} else if (stage == 16) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3450, 3728, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3056, 9553, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3508, 9493, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new  WorldTile(3803, 2844, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?", "Dagg Kings", "Coming Soon", "Coming Soon", "Coming Soon", "More Options");
				stage = 17;
			}
		} else if (stage == 17) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new  WorldTile(2900, 4449, 0));
			} else if (componentId == OPTION_2) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_3) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_4) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_5) {
						sendOptionsDialogue("Training",
				"Cows",
				"Yaks",
				"Rock Crabs",
				"Ghouls",
				"Other");
				stage = -1;
			}
		}
		 
	}

	@Override
	public void finish() {

	}
}