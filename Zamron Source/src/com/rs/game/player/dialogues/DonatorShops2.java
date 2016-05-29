package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class DonatorShops2 extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Flasks (G)", "<shad=FD3EDA>Cosmetics(G)", "<shad=FD3EDA>Essentials(G)", "<shad=FD3EDA>Steel Titan(G)", "More...");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 63);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 62);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 61);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 60);
			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Rare Shop(T)", "<shad=FD3EDA>Weapon Shop(T)", "<shad=FD3EDA>Armour Shop (T)", "<shad=05F7FF>Pet shop(G)");
        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 66);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 67);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 68);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 69);
			end();
        }
		/*if(componentId == OPTION_5) {
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
		}*/
	  }
	}

	@Override
	public void finish() {

	}

}