package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class BossTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptionsDialogue("Pvm Teleports", "<shad=00FF00>Godwars", "<shad=FD3EDA>Skeletal Wyvern ", "<shad=05F7FF>Frost Dragons", "<shad=FFCD05>Null", "More Options...");
			stage = 1; 
		}
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
	    int option;
		if(componentId == OPTION_1) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2916, 3746, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3035, 9554, 0));
			end();
		}
        if(componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2894, 3895, 0));
			end();
		}
		if(componentId == OPTION_4) {

		}
		if(componentId == OPTION_5) {
		    stage = 2;
		    sendOptionsDialogue("Pvm Teleports", "<shad=00FF00>King Black Dragon", "<shad=FD3EDA>Tormented Demons", "<shad=05F7FF>Queen Black Dragon", "<shad=FFCD05>Corporeal Beast", "Back...");
		}
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3067, 10254, 0));
			player.getPackets().sendGameMessage("Careful, make sure to have an anti-dragon shield, you're going to need it!");
			end();
		}
		if(componentId == OPTION_2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562, 5739, 0));
			end();
		}
        	if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1197, 6499, 0));
			end();
		}
		if(componentId == OPTION_4) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2966, 4383, 2));
			end();
		}
		if(componentId == OPTION_5) {
		    stage = 1; 
		   sendOptionsDialogue("Pvm Teleports", "<shad=00FF00>Godwars", "<shad=FD3EDA>Skeletal Wyvern ", "<shad=05F7FF>Frost Dragons", "<shad=FFCD05>Null", "More Options...");
		}
	  }
	}

	@Override
	public void finish() {

	}

}