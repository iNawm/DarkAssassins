package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;

public class FlambeedCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3494 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		@SuppressWarnings("unused")
		final Player player = (Player) target;
		npc.setNextAnimation(new Animation(1750));
		delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 250, NPCCombatDefinitions.MELEE, target)));
		return defs.getAttackDelay();
	}
	
}