package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class RC extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", where would you like to teleport to?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("RC teleport", "<shad=00FF00>Air", "<shad=FD3EDA>Mind", "<shad=FFCD05>Water", "<shad=0066CC>Earth", "<shad=FF66CC>More");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2841, 4829, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2791, 4828, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3494, 4832, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2655, 4830, 0));
			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("RC teleport", "<shad=00FF00>Fire", "<shad=FD3EDA>Cosmic", "<shad=FFCD05>Chaos", "<shad=0066CC>Astral", "<shad=FF66CC>More");        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2577, 4844, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2162, 4833, 0));
			end();
		}
        if(componentId == OPTION_3) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2281, 4837, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2153, 3868, 0));
			end();
        }
		if(componentId == OPTION_5) {
		stage = 4;
		sendOptionsDialogue("RC teleport", "<shad=00FF00>Nature", "<shad=FD3EDA>Law", "<shad=FFCD05>Death", "<shad=0066CC>Blood", "<shad=FF66CC>Next");        }
        }else if (stage == 4) {
			if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2400, 4835, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2464, 4818, 0));
			end();
		}
        if(componentId == OPTION_3) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2208, 4830, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2468, 4889, 1));
			end();
        }
		if(componentId == OPTION_5) {
		stage = 5;
		sendOptionsDialogue("RC teleport", "<shad=00FF00>Soul", "<shad=FD3EDA>Runespan");        }
        }else if (stage == 5) {
			if(componentId == OPTION_1) {
	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3302, 4809, 0));
			end();
		}
		if(componentId == OPTION_2) {
		sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level",  "3rd Level" );
			stage = 69;
		}
        } else if(stage == 69) {
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
		private void teleportPlayer2(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

}