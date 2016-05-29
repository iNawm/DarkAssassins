package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class SkillingShops extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Herbs", "<shad=FD3EDA>Herblore items", "<shad=FFCD05>Smithing", "<shad=0066CC>Crafting", "Nex page");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 8);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 7);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 25);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 33);
			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Mining", "<shad=FD3EDA>Woodcutting", "<shad=05F7FF>RuneCrafting", "<shad=FFCD05>Construction Shop", "<shad=FFF7FF>Next");
        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 28);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 27);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 29);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 18);
			end();
        }
		if(componentId == OPTION_5) {
			stage = 4;
			sendOptionsDialogue("Shop Manager", "<shad=FFF7FF>Hunter", "<shad=05F7FF>Fishing", "<shad=FFF7FF>Cooking", "<shad=00F7F0>Prayer", "<shad=FCF7FF>Next");
        }
	  }else if (stage == 4) {
			if(componentId == OPTION_1) {
        	ShopsHandler.openShop(player, 30);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 38);
			end();
		}if(componentId == OPTION_3) {
			ShopsHandler.openShop(player, 39);
			end();
		}if(componentId == OPTION_4) {
			ShopsHandler.openShop(player, 40);
			end();
		}if(componentId == OPTION_5) {
			stage = 5;
			sendOptionsDialogue("Shop Manager", "<shad=FFF7FF>Firemaking & Bow making", "<shad=05F7FF>Arrow Making", "<shad=FFF7FF>Bolt Making", "<shad=00F7F0>Dart making", "<shad=FCF7FF>Materials");
		}
	  }else if (stage == 5) {
			if(componentId == OPTION_1) {
        	ShopsHandler.openShop(player, 20);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 21);
			end();
		}if(componentId == OPTION_3) {
			ShopsHandler.openShop(player, 22);
			end();
		}if(componentId == OPTION_4) {
			ShopsHandler.openShop(player, 23);
			end();
		}if(componentId == OPTION_5) {
			ShopsHandler.openShop(player, 24);
			end();
		}
	  }
	}

	@Override
	public void finish() {

	}

}