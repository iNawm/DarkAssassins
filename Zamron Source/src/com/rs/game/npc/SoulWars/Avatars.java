package com.rs.game.npc.SoulWars;

/*
 * Author Savions Sw
 */

import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class Avatars extends NPC{

	public Avatars(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setRun(true);
		setForceMultiAttacked(true);
		setLureDelay(0);
	}
}
