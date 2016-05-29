package com.rs.game.player.dialogues;

import com.rs.game.player.content.WellOfFortune;
import com.rs.game.player.content.XPWell;


public class Well extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Well", "<shad=00FF00>XP Well", "<shad=FD3EDA>Drop Well");
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if(componentId == OPTION_1) {
			XPWell.give(player);	
				end();
			}
			if(componentId == OPTION_2) {
				WellOfFortune.handleWell(player);
				return;
			}
		}
	}

	@Override
	public void finish() {

	}

}