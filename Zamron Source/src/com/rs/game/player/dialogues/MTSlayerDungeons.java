package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class MTSlayerDungeons extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Slayer Locations",
				"Slayer Tower",
				"Polypore Dungeon",
				"Glacor Cave",
				"Jadinko Lair",
				"More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3428, 3534, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4624, 5459, 3));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4180, 5731, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3025, 9224, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Strykwyrms",
						"Asgarnian ice dungeon",
						"Fremmenik Slayer Dungeon",
						"Kuradel's Slayer Dungeon",
						"More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getDialogueManager().startDialogue("Stryke");
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3006, 9549, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2806, 10004, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1738, 5313, 1));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Temple Of Light",
						"Smoke Dungeon",
						"Taverley Slayer Dungeon",
						"Brine Rat Cavern",
						"More");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2009, 4639, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3308, 2962, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2218, 4532, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2696, 10120, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Desert Lizards",
						"Meiyerditch Dungeon",
						"Poison Waste Slayer Dungeon",
						"Mudskipper Point",
						"More");
				stage = 3;
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3418, 3039, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3627, 9741, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2321, 3100, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2992, 3112, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Tarn's Lair", "Mos Le'Harmless Jungle", "Chaos alter dungeon","Sea trollqueen" , "More");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3185, 4598, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3739, 2989, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3227, 10011, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1312, 4528, 0));
			}else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
				"God Wars Dungeon",
				"Ancient Cavern",
				"Forinthry Dungeon",
				"Frost Dragons",
				"More");
				stage = 5;
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2916, 3746, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2512, 3511, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3080, 10057, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1204, 6370, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
				"Taverly Dungeon",
				"Brimhaven Dungeon",
				"Waterfall Dungeon",
				"Grotworm Lair",
				"More");
				stage = 6;
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2886, 9797, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2744, 3152, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2574, 9864, 0));
			} else if (componentId == OPTION_4) {
				//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile());
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Lava Dungeon", "Brimheaven dungeon", "Baxtorian Dungeon","Jadinko Lair", "More");
				stage = 7;
			}
		} else if (stage == 7 ) {
		if (componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2722, 9774, 0));
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3613, 3370, 0));
		} else if (componentId == OPTION_3) {
			//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile());
		} else if (componentId == OPTION_4) {
			//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile());
		} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Legends Guild Dungeon", "Vyrewatch's", "","", "");
				stage = 7;
		}
		
	}
		 
	}

	@Override
	public void finish() {

	}
}