package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;

public class MTHighLevelBosses extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Bosses", "Queen Black Dragon", "Corporeal Beast", "Godwars", "Wildy Wyrm", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1197, 6499, 0));
					end();
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2969, 4383, 2));
			} else if (componentId == OPTION_3) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2916, 3746, 0));
			end();
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3171, 3871, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Bosses  - Pg 2", "Yk'Lagor The Thunderous","Blink","Madara","Sunfreet","More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2523, 5232, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1370, 6621, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(200, 5400, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(217, 5380, 0));
			}else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bosses  - Pg 3", "Korasi", "Trio Boss", "King Black Dragon", "Bork", "More");
			stage = 2;
			}
		}else if (stage == 2) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(216, 5400, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(200, 5380, 0));
			}if (componentId == OPTION_3 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3067, 10254, 0));
			} else if (componentId == OPTION_4) {
				player.getControlerManager().startControler("BorkControler", 0, null);
			} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bosses", "Kalphite Queen", "Dagannoth Kings", "Tormented Demons", "Leeuni", "More");
				stage = 3;
			}
		}else if (stage == 3) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3226, 3109, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2544, 10143, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2564, 5739, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3148, 3874, 0));
			} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bosses", "Sea Troll Queen", "Hope Devourer", "Har'lakk", "Merc Mage", "More");
				stage = 4;
			}
		}else if (stage == 4) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3226, 3109, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3823, 4767, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(234, 5515, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(215, 5514, 0));
			} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bosses", "Lucien", "Nomad", "blink");
				stage = 5;
			}
		}else if (stage == 5) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(211, 5528, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3361, 5849, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(200, 5509, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(215, 5514, 0));
			} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bosses", "Lucien", "Nomad");
				stage = 5;
			}
		}
		 
	}

	@Override
	public void finish() {

	}
	
	private void teleportPlayer(int x, int y, int z) {
	}
}
