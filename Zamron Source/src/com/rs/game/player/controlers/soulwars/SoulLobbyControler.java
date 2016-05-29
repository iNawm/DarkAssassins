package com.rs.game.player.controlers.soulwars;

import com.rs.game.WorldTile;
import com.rs.game.minigames.soulwars.SoulLobby;
import com.rs.game.player.controlers.Controler;

/**
 * 
 * @author Adam
 *
 */

public class SoulLobbyControler extends Controler{
	@Override
	public void start() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.rs.game.player.controlers.Controler#login()
	 */
	
	
	@Override
	public boolean login() {
		if(player.didPassBlue) {
			SoulLobby.removeBlue(player);
		}else if(player.didPassRed) {
			SoulLobby.removeRed(player);
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.rs.game.player.controlers.Controler#logout()
	 */
	@Override
	public boolean logout() {
		if(player.didPassBlue) {
			SoulLobby.removeBlue(player);
		} if(player.didPassRed) {
			SoulLobby.removeRed(player);
		}
		
		return false;
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.rs.game.player.controlers.Controler#processMagicTeleport(com.rs.game.WorldTile)
	 */
	
	
	//TODO object click for exit

		@Override
		public boolean processMagicTeleport(WorldTile toTile) {
			player.getDialogueManager().startDialogue("SimpleMessage", "A magical force prevents you from teleporting from the arena.");
			return false;
		}

		@Override
		public boolean processItemTeleport(WorldTile toTile) {
			player.getDialogueManager().startDialogue("SimpleMessage","A magical force prevents you from teleporting from the arena.");
			return false;
		}

		@Override
		public void magicTeleported(int type) {
			player.getControlerManager().forceStop();
		}

	   @Override
		public boolean processButtonClick(int interfaceId, int componentId,
				int slotId, int packetId) {
			if (interfaceId == 193 && componentId == 48) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You cant teleport during SoulWars.");
				return false;
			}
			if (interfaceId == 192 && componentId == 24) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You cant teleport during SoulWars.");
				return false;
 			}
			if (interfaceId == 182 && componentId == 13) {
				player.getDialogueManager().startDialogue("SimpleMessage", "Please leave SoulWars before doing this.");
				return false;
			}
			if (interfaceId == 182 && componentId == 6) {
				player.getDialogueManager().startDialogue("SimpleMessage", "Please leave SoulWars before doing this.");
				return false;
			}
			return true;
		}
	  
}

