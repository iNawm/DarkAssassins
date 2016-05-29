package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class AsteaFrostweb extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9965, 9966, 9967 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final int npcId = npc.getId();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (Utils.getRandom(20) == 0) {
			if (npc.getFreezeDelay() == 0) {
				 target.setNextGraphics(new Graphics(369));
				 target.addFreezeDelay(10000);
				 npc.addWalkSteps(target.getX() + Utils.getRandom(2), target.getY() + Utils.getRandom(2), 3, true);
				 npc.setRun(true);
			 	}
			}
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
			npc.setNextAnimation(new Animation(422));
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			if (Utils.getRandom(1) == 0) {
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 2, target, getMagicHit(npc, damage));
				if (Utils.getRandom(6) == 0) {
				if (npc.getFreezeDelay() == 0) {
					 target.setNextGraphics(new Graphics(369));
					 target.addFreezeDelay(10000);
					 npc.resetWalkSteps();
					 npc.addWalkSteps(target.getX() + Utils.getRandom(2), target.getY() + Utils.getRandom(2), 3, true);
					 npc.setRun(true);
				 	}
				}
			}
		}
		return defs.getAttackDelay();
	}
}