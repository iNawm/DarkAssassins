package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class Skillcapeshop extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	@SuppressWarnings("unused")
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("SkillCapes", "Skillcapes" ,"Skillcapes (t)", "Skill hoods", "Master Capes");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 2);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 3);
			end();
		}
		if(componentId == OPTION_3) {
			ShopsHandler.openShop(player, 4);
			end();
		}
		if(componentId == OPTION_4) {
			ShopsHandler.openShop(player, 5);
			end();
		}
		} else if (stage == 3) {
			sendOptionsDialogue("SkillCapes", "Skillcapes", "Skillcape hoods");
			stage = 2;
	  }
	}

	@Override
	public void finish() {

	}

}