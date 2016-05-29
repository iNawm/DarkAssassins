package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

/**
 * @author 'Corey 2010
 */

public class DagannothSupremeCombat extends CombatScript{

	public Object[] getKeys() {
		return new Object[] { 2881 };
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
						npc.setNextAnimation(new Animation(2855));
						World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
						delayHit(npc, 1, target, new Hit[] {
								getRangeHit(npc, hit)
						});
				break;
			case 1:
						npc.setNextAnimation(new Animation(2855));
						World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
						delayHit(npc, 1, target, new Hit[] {
								getRangeHit(npc, hit)
						});
				break;
			}
			}
			return defs.getAttackDelay();

		}
	}