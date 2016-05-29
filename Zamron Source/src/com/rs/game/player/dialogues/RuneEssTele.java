package com.rs.game.player.dialogues;

import com.rs.game.Graphics;
import com.rs.game.WorldTile;

public class RuneEssTele extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to travel to?",
				"Teleport to Rune Essence",
				"Nevermind.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.setNextGraphics(new Graphics(111));
				player.setNextGraphics(new Graphics(1829));
				player.setNextWorldTile(new WorldTile(2911, 4832, 0));
			} else {
				end();
			} 
		}

	}

	@Override
	public void finish() {

	}

}
