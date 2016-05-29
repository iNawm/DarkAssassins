package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;

public class ForestersArms extends Dialogue {
	
	private int npcId = 820;

	@Override
	public void start() {
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Welcome to the Foresters Arms pub! Home of the famous, Liverbane Ale!"},
				IS_NPC, npcId, 9827);
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Foresters Arms",
					"I'd like to try your signature drink",
					"Could I have a regular beer?",
					"Do you get much business over here?",
					"Good bye");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				sendNPCDialogue(
						npcId,
						9827,
						"One Liverbane Ale, coming up");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(
						npcId,
						9827,
						"Sure thing! Don't worry about the price, It's on the house");
				stage = 3;
			} else if (componentId == OPTION_3) {
				sendNPCDialogue(
						npcId,
						9827,
						"We have regular business, a lot of it is for poison to kill house-hold verman too. ");
				stage = -1;
			} else if (componentId == OPTION_4) {
				
				end();
			}
		} else if (stage == 2) {
			end();
			player.setNextAnimation(new Animation(1327));
			player.applyHit(new Hit(player, 30, HitLook.REGULAR_DAMAGE));
			player.setNextForceTalk(new ForceTalk("*Gulp*"));
			player.getPackets().sendGameMessage("The The poison selling Bartender signs your BarCrawl Card");
			player.ForestersArms = 1;
		} else if (stage == 3) {
			sendPlayerDialogue(9827, "Thanks!");
			player.getInventory().addItem(1917, 1);
			stage = -1;
		}
		 
	}

	@Override
	public void finish() {

	}
}