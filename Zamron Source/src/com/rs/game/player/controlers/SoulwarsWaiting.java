package com.rs.game.player.controlers;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.Soulwars;
import com.rs.game.player.Equipment;

public class SoulwarsWaiting extends Controler {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	// You can't leave just like that!

	public void leave() {
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8);
		//player.getPackets().sendIComponentText(837, 10,"+Soulwars.minutesLeft");
		//Soulwars.removeWaitingPlayerRed(player, team);
		//Soulwars.removeWaitingPlayerBlue(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 837);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387 && componentId == 9) {
			player.getPackets().sendGameMessage("You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE) {
			player.getPackets().sendGameMessage("You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		removeControler();
		leave();
		return true;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(Soulwars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You Cannot teleport from this Area!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You Cannot teleport from this Area!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You Cannot teleport from this Area!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 4389 || id == 4390) {
			removeControler();
			leave();
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
