package com.rs.game.npc.combat.impl;

import com.npcn.NulledNpc;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class RosDharokCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { new NulledNpc(2026).GetNulledId() };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		int attackStyle = Utils.getRandom(5);
		if (attackStyle == 0) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, 400, NPCCombatDefinitions.MELEE, target)));
			npc.setNextAnimation(new Animation(2066));
		}
		else if (attackStyle == 1) {
			int damage = 400;// normal
			for (Entity e : npc.getPossibleTargets()) {
				if (e instanceof Player
						&& (((Player) e).getPrayer().usingPrayer(0, 19) || ((Player) e)
								.getPrayer().usingPrayer(1, 9))) {
					damage = 450;
					npc.setNextAnimation(new Animation(2067));
					npc.setNextForceTalk(new ForceTalk("PETTY MORTAL!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.setPrayerDelay(Utils.getRandom(5) + 5);
					delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 380, NPCCombatDefinitions.MELEE, target)));
					player.getPackets().sendGameMessage("Dharok manages to turn off your prayers, leaving you feeling drained.");
				}
			}
		}
		return 6;
	}
	
	
}