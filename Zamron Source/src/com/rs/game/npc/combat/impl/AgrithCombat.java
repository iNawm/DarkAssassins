package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class AgrithCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3493 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		@SuppressWarnings("unused")
		final Player player = (Player) target;
		if (Utils.getRandom(2) == 0) {
			npc.setNextAnimation(new Animation(3502));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 200, NPCCombatDefinitions.MAGE, target)));
		}
		else {
			npc.setNextAnimation(new Animation(3501));
			delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 200, NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}