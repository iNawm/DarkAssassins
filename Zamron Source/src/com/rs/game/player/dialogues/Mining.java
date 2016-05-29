package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class Mining extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", where would you like to teleport to?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Mining teleport", "<shad=00FF00>Dwarf Mine", "<shad=FD3EDA>Coal", "<shad=FFCD05>LRC", "<shad=0066CC>Lava flow", "more");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3020, 9847, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2585, 3478, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3655, 5114, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2179, 5663, 0));
			end();
        }
		if(componentId == OPTION_5) {
		//stage = 3;
		//sendOptionsDialogue("Mining teleport", "<shad=00FF00>Fishing shop", "<shad=FD3EDA>Woodcutting & Mining Shop", "<shad=05F7FF>Farming shop", "<shad=FFCD05>All-in-one shop");
        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {

			end();
		}
		if(componentId == OPTION_2) {

			end();
		}
        if(componentId == OPTION_3) {
 
			end();
        }
		if(componentId == OPTION_4) {

			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Fishing shop", "<shad=FD3EDA>Crafting shop", "<shad=05F7FF>Fletching shop", "<shad=FFCD05>All-in-one shop");
        }
	  }
	}

	@Override
	public void finish() {

	}

}