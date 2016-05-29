package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class Fishing extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", where would you like to teleport to?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Fishing teleport", "<shad=00FF00>Barbarian", "<shad=FD3EDA>Draynor", "<shad=FFCD05>catherby", "<shad=0066CC>LRC", "<shad=FF66CC>Fishing Guild");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3105, 3430, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3091, 3235, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2855, 3427, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3655, 5114, 0));
			end();
        }
		if(componentId == OPTION_5) {
		 Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2587, 3422, 0));
			end();
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