package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class WaterFiendCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5361 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
			if (Utils.getRandom(1) == 0) {
				if (distanceX > size || distanceX < -1 || distanceY > size
						|| distanceY < -1) {
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
					npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			 	}
			}
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (Utils.getRandom(1) == 0) {
				if (distanceX > size || distanceX < -1 || distanceY > size
						|| distanceY < -1) {
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
					npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			 	}
			}
		}
		return defs.getAttackDelay();
	}
}