package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class HaarlakCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9911 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(1) == 0) { // mage attack
			npc.setCapDamage(1000);
			npc.setNextAnimation(new Animation(14371));
			npc.setNextForceTalk(new ForceTalk("Feel the Pain of Water!"));
			for (Entity t : npc.getPossibleTargets()) {
				if (!t.withinDistance(npc, 18))
					continue;
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, t);
				if (damage > 0) {
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					World.sendProjectile(npc, t, 500, 50, 16, 41, 35, 16, 0);
					t.setNextGraphics(new Graphics(502));
				}
			}

		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			npc.setCapDamage(1000);
			target.setNextGraphics(new Graphics(556));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}