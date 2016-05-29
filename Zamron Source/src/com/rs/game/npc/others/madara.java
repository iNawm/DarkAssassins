package com.rs.game.npc.others;

import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

@SuppressWarnings("serial")
public class madara extends NPC {

	public static long deadTime;
	
	public madara(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setRun(true);
		setForceFollowClose(true);
		setForceMultiAttacked(true);
		setForceAgressive(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 100;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		Player player = (Player) hit.getSource();
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			player.getPackets().sendGameMessage("Madara laughs at your pathetic attempt to use magic!.");
			hit.setDamage(0);
			}
		}
		
			public static String convertToTime() {
		String time = "You have to wait "+(getTime() == 0 ? "few more seconds" : getTime()+" mins")+" to kill Madara again.";
		return time;
	}
	
	public static int getTime() {
		return (int) (deadTime-System.currentTimeMillis()/60000);
	}
	
	public static boolean atBork(WorldTile tile) {
		if ((tile.getX() >= 3083 && tile.getX() <= 3120) && (tile.getY() >= 5522 && tile.getY() <= 5550))
			return true;
		return false;
	}
	

}
