package com.rs.game.player.content.citadels;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Controler;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class Vistor extends Controler {

	static Player owner;

	public static WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile((owner.boundChunks[0] << 3) + mapX,
				(owner.boundChunks[1] << 3) + mapY, 0);
	}

	@Override
	public void start() {
		owner = (Player) getArguments()[0];
		if (owner != null) {
			// owner.vistors.add(player);
			if (owner.getCitTypeX() != 0)
				player.setNextWorldTile(getWorldTile(113, 10));
			else {
				player.sm("This person doesn't own a citadel!");
				removeControler();
			}
		} else {
			player.sm("The Citadel owner isn't online or doesn't exist.");
			removeControler();
			return;
		}

	}

	private void leave(boolean logout) {
		if (logout)
			player.setLocation(Settings.HOME_PLAYER_LOCATION1);
		else
			player.setNextWorldTile(Settings.HOME_PLAYER_LOCATION1);
		player.setCanPvp(false);
		removeControler();

	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.setCanPvp(false);
		removeControler();
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.setCanPvp(false);
		removeControler();
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.setCanPvp(false);
		removeControler();
		return false;
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.setNextAnimation(new Animation(-1));
					player.setNextWorldTile(getWorldTile(14, 12));
					player.inBattleField = false;
					player.setCanPvp(false);
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void process() {
		if (World.getPlayer(owner.getUsername()) == null) {
			leave(false);
			player.sm("This Citadel owner is no longer online.");
		} else if (!owner.citadelOpen) {
			leave(false);
			player.sm("The owner left his citadel.");
		} else if (player.inBattleField && !owner.citadelBattleGround) {
			if (owner != null) {
				player.setNextWorldTile(getWorldTile(14, 12));
				player.setCanPvp(false);
				player.inBattleField = false;
			}
		}

	}

	@Override
	public void forceClose() {
		leave(false);
	}

	@Override
	public boolean logout() {
		leave(true);
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 31297)
			player.sm("You must own the citadel to do this!");
		else if (object.getId() == 42425) {
			player.setNextWorldTile(getWorldTile(14, 12));
			player.setCanPvp(false);
			player.inBattleField = false;
		} else if (object.getId() == 59462) {
			leave(false);
		}
		return false;
	}

	@Override
	public boolean processNPCClick1(final NPC npc) {
		if (npc.getId() == 13932)
			player.getDialogueManager().startDialogue("citadelbattle", owner);

		return true;
	}

}
