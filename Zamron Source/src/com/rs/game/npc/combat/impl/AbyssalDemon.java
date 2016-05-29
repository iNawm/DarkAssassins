package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class AbyssalDemon extends CombatScript {
	
	/**
	 * Author that little turkish noob Savions Sw
	 */
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 1615, 979 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), -1, target)));
		if (Utils.getRandom(2) == 0) {
			TeleportAway(npc, target);
		}
		return defs.getAttackDelay();
	}
	
	/*
	 * Special attack of the NPC
	 */

	private void TeleportAway(NPC npc, Entity target) {
		if (Utils.getRandom(1) == 0) {
		WorldTile teleTile = target;
		for (int trycount = 0; trycount < 10; trycount++) {
			teleTile = new WorldTile(target, 2);
			if (World.canMoveNPC(target.getPlane(), teleTile.getX(),
					teleTile.getY(), target.getSize()));
			continue;
		}
		//target.setNextWorldTile(teleTile);
		target.setNextGraphics(new Graphics(409));
		} else {
			WorldTile teleTile = npc;
			for (int trycount = 0; trycount < 10; trycount++) {
				teleTile = new WorldTile(npc, 2);
				if (World.canMoveNPC(npc.getPlane(), teleTile.getX(),
						teleTile.getY(), npc.getSize()));
				continue;
			}
			npc.setNextWorldTile(teleTile);
			npc.setNextGraphics(new Graphics(409));
		}
	}

}