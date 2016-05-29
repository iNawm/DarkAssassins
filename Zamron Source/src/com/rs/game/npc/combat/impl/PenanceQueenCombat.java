package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class PenanceQueenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5247 };
	}
	
	/*
	 * Attack ID: 5411
	 * Magic Attack ID: 5413
	 * Death ID: 5412
	 * Block ID: 5408
	 */

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		final int hit = Utils.getRandom(300) + 20;
		if (Utils.getRandom(10) == 0) {
			switch (Utils.getRandom(2)) { //Vorago Taunts
			case 0:
				npc.setNextForceTalk(new ForceTalk("Hsssssssssss!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Blurgh!"));
				break;
			}
		}
		if (Utils.getRandom(6) == 0) {
				npc.setNextAnimation(new Animation(5413));
				World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
				delayHit(npc, 1, target, new Hit[] {
						getMagicHit(npc, hit)
				});
				if (hit > 0) {
					target.setNextGraphics(new Graphics(2712));
				} else {
					target.setNextGraphics(new Graphics(85));
				}
			}
		else if (Utils.getRandom(6) == 0) {
			npc.setNextAnimation(new Animation(5413));
			World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
			delayHit(npc, 1, target, new Hit[] {
					getRangeHit(npc, hit)
			});
		} else {
			npc.setNextAnimation(new Animation(5411));
			delayHit(npc, 0, target, getMeleeHit( npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}