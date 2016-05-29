package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class AgilityTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptionsDialogue("Agility Teleports", "<shad=00FF00>Gnome Agility", "<shad=FD3EDA>Barbarian Agility");
			stage = 1; 
		}
	}
	
	@SuppressWarnings("unused")
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
	    int option;
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2480, 3437, 0));
			end();
		}
		if(componentId == OPTION_2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2552, 3557, 0));
			end();
		}
	  }
	}

	@Override
	public void finish() {

	}

}