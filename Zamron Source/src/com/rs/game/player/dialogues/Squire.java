package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.ForceTalk;
import com.rs.utils.ShopsHandler;

public class Squire extends Dialogue {

	private int npcId = 3801;

	@Override
	public void start() {
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello "
								+ player.getDisplayName()
								+ ". Would you like some information on this minigame?" },
				IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Would would you like to say?", "What is this minigame?",
					"Is this a dangerous minigame?",
					"What are the rewards?",
					"Shop", "How many points do i have?");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				sendNPCDialogue(
						npcId,
						9827,
						"This minigame is similar to the Fight Kiln. We need your help to fight off the invasion of pests and other creatures.");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(
						npcId,
						9827,
						"This is a safe minigame, you will not lose items. It is suggested to gear up and bring many supplies.");
				stage = 2;
			} else if (componentId == OPTION_3) {
				sendNPCDialogue(
						npcId,
						9827,
						"If you can defeat the Pest Queen, we will grant you Elite Void and a Jericho + Pest points!");
				stage = 2;
			}else if (componentId == OPTION_4) {
				end();
				ShopsHandler.openShop(player, 72);
				stage = 2;
			} else if (componentId == OPTION_5) {
				//end();
				sendNPCDialogue(
						npcId,
						9827,
						"You currently have " + player.getPestinvPoints() + " pestinvasion points.");
				stage = 2;
			}
		} else if (stage == 2) {
			sendPlayerDialogue(9827, "Thanks!");
			end();

	}
	}

	@Override
	public void finish() {

	}
}