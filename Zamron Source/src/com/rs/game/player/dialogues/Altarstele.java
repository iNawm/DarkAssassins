package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class Altarstele extends Dialogue {
	
	

	@Override
	public void start() {
	sendOptionsDialogue("Prayers/Spellbooks", "Ancient Magics", "Lunar Spells", "Ancient Curses");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3233, 9315, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Ancient Magics Altar.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2151, 3863, 0));
				player.sm("Welcome to the Lunar Magic Altar.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			
		} else if(componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3182, 5713, 0));
			player.sm("Welcome to the Ancient Curses Altar.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
			
		}
		} 
		 
	}

	@Override
	public void finish() {

	}
}