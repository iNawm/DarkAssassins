package com.rs.game.minigames.hungerGames;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Controler;

public class LobbyController extends Controler {

	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		return false;
	}

	@Override
	public boolean canAttack(Entity target) {
		return false;
	}

	@Override
	public boolean canDropItem(Item item) {
		return false;
	}

	@Override
	public boolean canHit(Entity entity) {
		return false;
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		return false;

	}

	public boolean canPlayerOption2(Player target) {
		return false;
	}

	public boolean canPlayerOption5(Player target) {
		return false;
	}

	@Override
	public boolean canSummonFamiliar() {
		return false;
	}

	@Override
	public void forceClose() {
		leaveLobby(true, false);
	}

	@Override
	public boolean handleItemOnObject(WorldObject object, Item item) {
		return false;
	}
	
	public boolean handlePickUpItem(FloorItem item) {
		return true;
	}

	private void leaveLobby(boolean tele, boolean logout) {
		player.getControlerManager().removeControlerWithoutCheck();
		if (tele)
			player.setNextWorldTile(HungerGames.OUTSIDE);
		if (logout)
			player.setLocation(HungerGames.OUTSIDE);
		Lobby.getLobby().remove(player);
		Lobby.startGame(false);
		player.getInterfaceManager().closeOverlay(false);
	}
	
	/**
	 * return remove controler
	 */
	@Override
	public boolean login() {
		leaveLobby(false, true);
		return true;
	}

	/**
	 * return remove controler
	 */
	@Override
	public boolean logout() {
		leaveLobby(false, true);
		return true;
	}

	@Override
	public void process() {
		sendInterfaces();
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		return true;
	}

	@Override
	public boolean processItemOnNPC(NPC npc, Item item) {
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		leaveLobby(false, false);
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		leaveLobby(false, false);
		return true;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick1(NPC npc) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick2(NPC npc) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick3(NPC npc) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 52862:
			this.leaveLobby(true, false);
			break;
		}
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick2(WorldObject object) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick3(WorldObject object) {
		return false;
	}

	@Override
	public boolean processObjectClick5(WorldObject object) {
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		leaveLobby(false, false);
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(3);
		player.stopAll();
		player.reset();
		player.setNextAnimation(new Animation(-1));
		player.sm("How the hell did you die?");
		return false;
	}

	@Override
	public void sendInterfaces() {
		String playersNeeded = Lobby.getLobby().size() == 4 ? "None" : ""+(4 - Lobby.getLobby().size()+"");
		player.getInterfaceManager().sendOverlay(1296, false);
		player.getPackets().sendIComponentText(1296, 19, "Players in lobby:");
		player.getPackets().sendIComponentText(1296, 25, ""+Lobby.getLobby().size());
		player.getPackets().sendIComponentText(1296, 20, "Players in active game:");
		player.getPackets().sendIComponentText(1296, 26, ""+HungerGames.getPlayers().size());
		player.getPackets().sendIComponentText(1296, 24, "Players needed:");
		player.getPackets().sendIComponentText(1296, 27, ""+playersNeeded);
	}

	@Override
	public void start() {
		player.getInventory().reset();
		player.getEquipment().reset();
		player.getInventory().refresh();
		player.getEquipment().refresh();
		player.setNextWorldTile(new WorldTile(3498, 3633, 0));
		player.sm("Welcome to the Hunger Games, may the odds be ever in your favor.");
		if (Lobby.getLobby().contains(player))
			Lobby.getLobby().remove(player);
		Lobby.getLobby().add(player);
		sendInterfaces();
		player.sm("Players in lobby: "+Lobby.getLobby().size()+"");
		player.sm("Players in a game currently: "+HungerGames.getPlayers().size());
		Lobby.startGame(false);
	}

}
