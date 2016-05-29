package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class PkerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15174 };
	}

	@SuppressWarnings("unused")
	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		
		if (Utils.getRandom(3) == 0) {
			npc.setNextAnimation(new Animation(829));
			npc.heal(260);
		}
		if (Utils.getRandom(4) == 0) {
			npc.setNextForceTalk(new ForceTalk("Prayer drain!"));
			player.getPrayer().drainPrayer(375);
		}
		if (Utils.getRandom(2) == 0) { 
			npc.setNextAnimation(new Animation(1062));
			npc.setNextGraphics(new Graphics(252, 0, 100));
			npc.playSound(2537, 1);
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc,1,target,getMeleeHit(npc,getRandomMaxHit(npc, Utils.random(400),NPCCombatDefinitions.MELEE, target)),getMeleeHit(npc,getRandomMaxHit(npc, Utils.random(300),NPCCombatDefinitions.MELEE, target)));
			}
		} else { // Melee - Whip Attack
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc,0,target,getMeleeHit( npc,getRandomMaxHit(npc, Utils.random(800), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}