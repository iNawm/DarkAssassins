package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class DagannothPrimeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2882 };
	}
	
	private transient long stopDelay;
	
	public long getStopDelay() {
		return stopDelay;
	}

	public void setInfiniteStopDelay() {
		stopDelay = Long.MAX_VALUE;
	}

	public void resetStopDelay() {
		stopDelay = 0;
	}

	public void addStopDelay(long delay) {
		stopDelay = Utils.currentTimeMillis() + (delay * 600);
	}

	public boolean spawnedMinions = false;
	public int doneSpecAttack = 0;
	
	private transient WorldTile lastWorldTile;
	private transient WorldTile nextWorldTile;
	
	public WorldTile getNpcCurrentTile(final NPC npc) {
		return new WorldTile(npc.getX(), npc.getY(), npc.getPlane());
	}

	
	@Override
	public int attack(final NPC npc, final Entity target) {
		final int hit = Utils.getRandom(300) + 20;//294
		//final int specialHit = player.getHitpoints() / 2;//half hp
		int attackStyle = Utils.getRandom(1);
        //int distanceX = target.getX() - npc.getX();
        //int distanceY = target.getY() - npc.getY();
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		
		if (npc.withinDistance(target, 20)) {
			switch(attackStyle){
			case 0:
						npc.setNextAnimation(new Animation(2854));
						World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
						delayHit(npc, 1, target, new Hit[] {
								getMagicHit(npc, hit)
						});
						if (hit > 0) {
							target.setNextGraphics(new Graphics(2712));
						} else {
							target.setNextGraphics(new Graphics(85));
						}
				break;
			case 1:
						npc.setNextAnimation(new Animation(2854));
						World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0); 
						delayHit(npc, 1, target, new Hit[] {
								getMagicHit(npc, hit)
						});
						if (hit > 0) {
							target.setNextGraphics(new Graphics(2712));
						} else {
							target.setNextGraphics(new Graphics(85));
						}
				break;
			}
			}
			return defs.getAttackDelay();

		}
}