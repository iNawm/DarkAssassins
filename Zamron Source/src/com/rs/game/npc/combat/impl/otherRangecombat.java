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

public class otherRangecombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15176 };
	}

	@SuppressWarnings("unused")
	@Override
	public int attack(final NPC npc, final Entity target) {
		final Player player = (Player) target;
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(3) == 0) {
			npc.setNextAnimation(new Animation(829)); //eating 2 rocktails at a time xd
			npc.heal(350);
		}if (Utils.getRandom(4) == 0) {
			npc.setNextForceTalk(new ForceTalk("Poison!!"));
			target.getPoison().makePoisoned(175);
			npc.heal(Utils.random(200));
		} else { // Melee - Whip Attack
			npc.setNextGraphics(new Graphics(426));
			World.sendProjectile(npc, target, 1099, 41, 16, 45, 35, 16, 0);
			World.sendProjectile(npc, target, 1099, 41, 16, 32, 35, 21, 0);
			delayHit(npc,0,target,getRangeHit( npc,getRandomMaxHit(npc, Utils.random(370), NPCCombatDefinitions.RANGE, target)));
			delayHit(npc,0,target,getRangeHit( npc,getRandomMaxHit(npc, Utils.random(350), NPCCombatDefinitions.RANGE, target)));

		}
		return defs.getAttackDelay();
	}
}