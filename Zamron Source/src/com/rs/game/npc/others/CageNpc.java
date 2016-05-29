package com.rs.game.npc.others;

import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Cages;

@SuppressWarnings("serial")
public class CageNpc extends NPC {
	
	private Player target = null;

	public CageNpc(int id, WorldTile tile, Player target) {
		super(id, tile, -1, true, true);
		this.target = target;
		setForceMultiArea(true);
	}
	
	@Override
	public void processNPC() {
		if(!(target.getControlerManager().getControler() instanceof Cages) || target == null || World.getPlayerByDisplayName(target.getDisplayName()) == null)
		   super.sendDeath(this);
		
		super.processNPC();
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
	}
	
}
