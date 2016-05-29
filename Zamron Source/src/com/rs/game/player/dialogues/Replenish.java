package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class Replenish extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Food Shop", "<shad=FD3EDA>Potion Shop");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 6);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 11);
			end();
		}
		} else if (stage == 3) {
			sendOptionsDialogue("Shop Manager", "<shad=00FF00>Herblore shop", "<shad=FD3EDA>Crafting shop", "<shad=05F7FF>Fletching shop", "<shad=FFCD05>All-in-one shop");
			stage = 2;
	  }
	}

	@Override
	public void finish() {

	}

}