package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.magic.Magic;
import com.rs.game.player.dialogues.Dialogue;

/**
 * @author Justin
 */


public class PickOne extends Dialogue {

	public PickOne() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Choose an Option", "Appearance", "Settings", "None" );
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			player.getDialogueManager().startDialogue("Looks");
		} else if(componentId == OPTION_2) {
			player.getDialogueManager().startDialogue("News");
		} else if(componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
		}
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}