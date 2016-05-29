package com.rs.game.player.dialogues;

import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;

public class Sharingan extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Sharingan!" );
		player.applyHit(new Hit(player, player.getHitpoints(),
				HitLook.REGULAR_DAMAGE));
	}

	@Override
	public void run(int interfaceId, int componentId) {
		
	}

	@Override
	public void finish() {

	}

}
