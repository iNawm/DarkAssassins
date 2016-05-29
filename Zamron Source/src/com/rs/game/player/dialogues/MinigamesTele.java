package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.minigames.rfd.RecipeforDisaster;
import com.rs.game.player.Skills;
import com.rs.game.player.content.LavaFlowMines;
import com.rs.game.player.content.magic.Magic;
import com.rs.game.player.controlers.*;
import com.rs.game.player.controlers.dung.RuneDungGame;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.ShopsHandler;

public class MinigamesTele extends Dialogue {
	
	

	@Override
	public void start() {
	sendOptionsDialogue("Minigames", "Godwars Dungeon", "The RuneSpan",  "Warriors Guild", "Crucible", "Next Page" );
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
	if (stage == -1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Pick One", "Nex", "Godwars Dungeon");
				stage = 8;
						
			
			/**
			 * The RuneSpan
			 */
			
			
		} else if (componentId == OPTION_2) {
			sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level", 
					"3rd Level" );
			stage = 69;
			
			
			/**
			 * Warriors Guild
			 */
			
			
		} else if (componentId == OPTION_3) {
			teleportPlayer3(2871, 3542, 0);
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("Welcome to Warriors Guild.");
			
		} else if (componentId == OPTION_4) {
			teleportPlayer3(3120, 3519, 0);
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("Welcome to Crucible.");
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Minigames - Page 2", "Fight Caves", "Fight Kiln", 
					"Dominion Tower", "Clan Wars", "More" );
			stage = 13;
		}
			
		} else if(stage == 13) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4612, 5129, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Fight Caves");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4743, 5161, 0));
				player.sm("Welcome to Fight Kiln.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3360, 3082, 0));
				player.sm("Welcome to Dominion Tower.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2993, 9679, 0));
				player.sm("Welcome to Clan Wars.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 3", "Castle Wars", "Troll Invasion", 
						"Dominion Tower", "Duel Arena", "More" );
				stage = 14;
			
			}
			
		} else if(stage == 14) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2442, 3090, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Castle Wars");
				
			} else if(componentId == OPTION_2) {
				player.getControlerManager().startControler("TrollInvasion");
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3360, 3082, 0));
				player.sm("Welcome to Dominion Tower.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3367, 3267, 0));
				player.sm("Welcome to the Duel Arena.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 4", "Sorceress' Garden", "Barrows", 
						"Livid Farm", "Recipe for Disaster", "More" );
				stage = 15;
			
			}
			
		} else if(stage == 15) {
			if(componentId == OPTION_1) {//Slayer Tower
				player.getControlerManager().startControler("SorceressGarden");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3565, 3289, 0));
				player.sm("Welcome to Barrows.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				player.getInventory().addItem(6950, 1);
				player.sm("Here is your Livid Farm Crystal.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
			} else if(componentId == OPTION_4) {
            RecipeforDisaster.enterRfd(player);
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 5", "Zombies", "Dark Invasion", "Lava Flow Mines", "Pest Invasion",
						"Pest Control" );
				stage = 58;
			}
				
			} else if(stage == 58) {
				if(componentId == OPTION_1) { 
		            Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3701, 3461, 0));
					player.sm("Welcome to Zombies.");
					player.getInterfaceManager().closeChatBoxInterface();
					player.getInterfaceManager().closeOverlay(true);
					player.getControlerManager().forceStop();
					player.getControlerManager().removeControlerWithoutCheck();
		} else if(componentId == OPTION_2) {
			player.getControlerManager().startControler("DarkInvasion");
		} else if (componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		LavaFlowMines.Entering(player);
		} else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4520, 5516, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_5) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2658, 2663, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
	}
	}else if(stage == 69) {
			if(componentId == OPTION_1) {
				teleportPlayer2(3993, 6108, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_2) {
				teleportPlayer2(4137, 6089, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_3) {
				teleportPlayer2(4295, 6038, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
			}
			
		}
		 
	}

	@Override
	public void finish() {

	}
	
		private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("GodWars");
	}

	private void teleportPlayer2(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

	private void teleportPlayer3(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("WGuildControler");
	}
}