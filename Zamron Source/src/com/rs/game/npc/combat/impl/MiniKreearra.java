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

public class MiniKreearra extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9571 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (!npc.isUnderCombat()) {
			npc.setNextAnimation(new Animation(17396));
			delayHit(
					npc,
					1,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, 260,
									NPCCombatDefinitions.MELEE, target)));
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(17397));
			if (Utils.getRandom(2) == 0)
				sendMagicAttack(npc, target);
			else {
				delayHit(
						npc,
						1,
						target,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, 720,
										NPCCombatDefinitions.RANGE, target)));
				World.sendProjectile(npc, target, 1197, 41, 16, 41, 35, 16, 0);
				WorldTile teleTile = target;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new WorldTile(target, 2);
					if (World.canMoveNPC(target.getPlane(), teleTile.getX(),
							teleTile.getY(), target.getSize()))
						break;
				}
				target.setNextWorldTile(teleTile);
			}
		
		return defs.getAttackDelay();
	}

	private void sendMagicAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(17397));

			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 210,
									NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
	}
}
