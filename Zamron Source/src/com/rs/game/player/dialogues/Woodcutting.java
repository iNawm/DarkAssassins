package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class Woodcutting extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", where would you like to teleport to?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Mining teleport", "<shad=00FF00>Lumby", "<shad=FD3EDA>Draynor", "<shad=FFCD05>Seers", "<shad=0066CC>Magics", "<shad=FF66CC>Ivy");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3194, 3229, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3091, 3235, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2723, 3468, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2702, 3392, 0));
			end();
        }
		if(componentId == OPTION_5) {
		 Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3216, 3501, 0));
			end();
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