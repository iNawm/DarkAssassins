package com.rs.game.npc.kalph;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class MiniKalphiteQueen extends NPC {

	public MiniKalphiteQueen(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if(getId() == 14520) {
						setCantInteract(true);
						transformIntoNPC(14538);
						setNextGraphics(new Graphics(1055));
						setNextAnimation(new Animation(6270));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								reset();
								setCantInteract(false);
							}
							
						}, 5);
					}else{
						drop();
						reset();
						setLocation(getRespawnTile());
						finish();
						if (!isSpawned())
							setRespawnTask();
						transformIntoNPC(14520);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
}