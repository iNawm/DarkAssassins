package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class IrishToolTeleD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to go?", "Ardougne patches", "Falador patches", "Catherby patches", "Port phasmatys patches", "Herblore Habitat Patches");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			switch(componentId) {
			case OPTION_1:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2674, 3374, 0));
				end();
				break;
			case OPTION_2:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3052, 3304, 0));
				end();
				break;
			case OPTION_3:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2802, 3466, 0));
				end();
				break;
			case OPTION_4:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3603, 3532, 0));
				end();
				break;
			case OPTION_5:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2950, 2912, 0));
				end();
				break;
			}
		}
	}

	@Override
	public void finish() {

	}
}