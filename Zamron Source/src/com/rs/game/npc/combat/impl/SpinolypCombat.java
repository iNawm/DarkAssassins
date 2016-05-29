package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class SpinolypCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2892 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(2) == 0) {
			npc.setNextAnimation(new Animation(2868));
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 100, NPCCombatDefinitions.MAGE, target)));
				World.sendProjectile(npc, t, 2707, 34, 16, 40, 35, 16, 0);
			}
		} else {
			npc.setNextAnimation(new Animation(2868));
			for (Entity t : npc.getPossibleTargets()) {
			delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 100, NPCCombatDefinitions.RANGE, target)));
			World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0);
			}
		}
		return defs.getAttackDelay();
		}
	}