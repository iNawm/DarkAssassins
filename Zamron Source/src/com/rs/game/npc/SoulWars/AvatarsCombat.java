package com.rs.game.npc.SoulWars;

/*
 * Authors
 * Savions sw/Ozie
 */

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class AvatarsCombat extends CombatScript {
	
	int damage = Utils.getRandom(800);

	@Override
	public Object[] getKeys() {
		return new Object[] { "Avatar of " };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target instanceof Player)
		((Player) target).getPrayer().drainPrayer(damage + 260);
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		delayHit(npc, 1, target, getMeleeHit(npc, Utils.getRandom(960)));
		return defs.getAttackDelay();
     } 
}