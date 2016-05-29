package com.rs.game.npc.combat.impl;

import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;

public class NexMinionCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Fumus", "Umbra", "Cruor", "Glacies" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextGraphics(new Graphics(1549));
		return defs.getDefenceEmote();
	}
	
}