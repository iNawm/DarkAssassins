package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class CowKillerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5210 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			npc.setNextAnimation(new Animation(1979));
			target.setNextGraphics(new Graphics(369));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 300, NPCCombatDefinitions.MAGE, target)));
		}
		else if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(3)) { //Cowkiller taunts
			case 0:
				npc.setNextForceTalk(new ForceTalk("I will kill all the cows!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("How dare you disturb my cow tippin'!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Daisy would make a nice beef burger..."));
				break;
			}
		}
		else if (Utils.getRandom(2) == 0) {
			int damage = 300;// normal
			for (Entity e : npc.getPossibleTargets()) {
				if (e instanceof Player
						&& (((Player) e).getPrayer().usingPrayer(0, 19) || ((Player) e)
								.getPrayer().usingPrayer(1, 9))) {
					damage = 350;
					npc.setNextAnimation(new Animation(2067));
					npc.setNextForceTalk(new ForceTalk("Religion won't save you now!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.setPrayerDelay(Utils.getRandom(5) + 5);
					delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 380, NPCCombatDefinitions.MELEE, target)));
					player.getPackets().sendGameMessage("Cow1337killer uses his atheism to cut through your prayers, leaving you feeling drained.");
				}
			}
		} else if (Utils.getRandom(2) == 0) {
			if (Utils.random(5) == 0) {
				if (npc.getFreezeDelay() == 0) {
				npc.setNextForceTalk(new ForceTalk("It's a bit chilly..."));
				npc.setNextAnimation(new Animation(1979));
				target.setNextGraphics(new Graphics(369));
				target.addFreezeDelay(5000);
				delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 300, NPCCombatDefinitions.MAGE, target)));
				npc.addWalkSteps(target.getX() + Utils.getRandom(2), target.getY() + Utils.getRandom(2), 3, true);
				npc.setRun(true);
				}
			}
		} else {
		npc.setNextAnimation(new Animation(2066));
		delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 380, NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}