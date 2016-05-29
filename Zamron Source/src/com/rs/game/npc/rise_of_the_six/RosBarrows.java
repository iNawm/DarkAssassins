package com.rs.game.npc.rise_of_the_six;

import com.npcn.NulledNpc;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class RosBarrows extends NPC {
	
	private int dharok = new NulledNpc(2026).GetNulledId();
	private int verac = new NulledNpc(2030).GetNulledId();
	private int guthan = new NulledNpc(2027).GetNulledId();
	private int torag = new NulledNpc(2029).GetNulledId();
	private int ahrim = new NulledNpc(2025).GetNulledId();
	private int karil = new NulledNpc(2028).GetNulledId();

	public RosBarrows(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		if (getId() == dharok || getId() == verac || getId() == guthan || getId() == torag || getId() == ahrim
				|| getId() == karil) {
		setHitpoints(4000);
		}
	}
}