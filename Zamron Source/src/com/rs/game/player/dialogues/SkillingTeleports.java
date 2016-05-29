package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class SkillingTeleports extends Dialogue {

private int npcId;

	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
			sendOptionsDialogue("Skilling Teleports", "<shad=00FF00>Fishing",
					"<shad=FD3EDA>Mining", "<shad=05F7FF>Agility",
					"<shad=FFCD05>Woodcutting", "More Options...");
			stage = 1;
		}
	}

	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			int option;
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Fishing teleport", "<shad=00FF00>Barbarian", "<shad=FD3EDA>Draynor", "<shad=FFCD05>catherby", "<shad=0066CC>LRC", "<shad=FF66CC>Fishing Guild");
				stage = 33;
			}
			if (componentId == OPTION_2) {
				sendOptionsDialogue("Mining teleport", "<shad=00FF00>Dwarf Mine", "<shad=FD3EDA>Coal", "<shad=FFCD05>LRC", "<shad=0066CC>Lava flow", "more");
				stage = 44;

			}
			if (componentId == OPTION_3) {
				sendOptionsDialogue("Agility Teleports", "Gnome Agility", "Barbarian Outpost", "Agility Pyramid", "Wilderness Agility");
				stage = 3;
			}
			if (componentId == OPTION_4) {
				sendOptionsDialogue("Mining teleport", "<shad=00FF00>Lumby", "<shad=FD3EDA>Draynor", "<shad=FFCD05>Seers", "<shad=0066CC>Magics", "<shad=FF66CC>Ivy");
				stage = 55;
			}
			if (componentId == OPTION_5) {
				stage = 2;
				sendOptionsDialogue("Skilling Teleports",
						"<shad=00FF00>Runecrafting", "<shad=FD3EDA>Summoning",
						"<shad=663300>Hunter", "PuroPuro" ,"More");
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("RC teleport", "<shad=00FF00>Air", "<shad=FD3EDA>Mind", "<shad=FFCD05>Water", "<shad=0066CC>Earth", "<shad=FF66CC>More");
				stage = 66;
						
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2209,
						5343, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2526,
						2916, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2419,
						4446, 0));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Skilling Teleports", "Falconry",
						"<shad=00FF00>Back");
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2470,
						3436, 0));
				end();
			} if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2552, 3563, 0));
				end();
			} if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3354, 2828, 0));
				end();
			} if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2998, 3915, 0));
				end();
			}
		}else if (stage == 5) {
			if (componentId == OPTION_1) {
				player.getControlerManager().startControler("Falconry");
				end();
			}
			if (componentId == OPTION_2) {
				stage = 1;
				sendOptionsDialogue("Skilling Teleports",
						"<shad=00FF00>Fishing", "<shad=FD3EDA>Mining",
						"<shad=05F7FF>Agility", "<shad=FFCD05>Woodcutting",
						"More Options...");
			}
		} else if (stage == 33) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3105, 3430, 0));
				end();
			}
			if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3091, 3235, 0));
				end();
			}
	        if(componentId == OPTION_3) {
			    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2855, 3427, 0));
				end();
	        }
			if(componentId == OPTION_4) {
	        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3655, 5114, 0));
				end();
	        }
			if(componentId == OPTION_5) {
			 Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2587, 3422, 0));
				end();
				      
			}
			}else if (stage == 44) {
				if(componentId == OPTION_1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3020, 9847, 0));
					end();
				}
				if(componentId == OPTION_2) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2585, 3478, 0));
					end();
				}
		        if(componentId == OPTION_3) {
				    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3655, 5114, 0));
					end();
		        }
				if(componentId == OPTION_4) {
		        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2179, 5663, 0));
					end();
		        }
				if(componentId == OPTION_5) {
					        }
				}else if (stage == 55) {
					if(componentId == OPTION_1) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3194, 3229, 0));
						end();
					}
					if(componentId == OPTION_2) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3091, 3235, 0));
						end();
					}
			        if(componentId == OPTION_3) {
					    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2723, 3468, 0));
						end();
			        }
					if(componentId == OPTION_4) {
			        	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2702, 3392, 0));
						end();
			        }
					if(componentId == OPTION_5) {
					 Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3216, 3501, 0));
						end();
								        }
					}else if (stage == 66) {
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
						stage = 118;
						sendOptionsDialogue("RC teleport", "<shad=00FF00>Fire", "<shad=FD3EDA>Cosmic", "<shad=FFCD05>Chaos", "<shad=0066CC>Astral", "<shad=FF66CC>More");        }
						} else if (stage == 118) {
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
						stage = 123;
						sendOptionsDialogue("RC teleport", "<shad=00FF00>Nature", "<shad=FD3EDA>Law", "<shad=FFCD05>Death", "<shad=0066CC>Blood", "<shad=FF66CC>Next");        }
				        }else if (stage == 123) {
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
						stage = 89;
						sendOptionsDialogue("RC teleport", "<shad=00FF00>Soul", "<shad=FD3EDA>Runespan");        }
				        }else if (stage == 89) {
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
	
		private void teleportPlayer2(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

	@Override
	public void finish() {

	}

}