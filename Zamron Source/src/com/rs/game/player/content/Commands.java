package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

import javax.swing.SwingWorker;

import com.rs.Launcher;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.EntityList;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.hungerGames.HungerGames;
import com.rs.game.npc.NPC;
import com.rs.game.npc.corp.CorporealBeast;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.godwars.zaros.Nex;
import com.rs.game.player.Equipment;
import com.rs.game.player.InterfaceManager;
import com.rs.game.player.LendingManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Summoning.Pouches;
import com.rs.game.player.content.ItemSearch;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.SquealOfFortune;
import com.rs.game.player.content.citadels.Citadel;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.farming.Patch;
import com.rs.game.player.content.farming.PatchConstants;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.JailControler;
import com.rs.game.player.controlers.PestInvasion;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.darkinvasion.DarkInvasion;
import com.rs.game.player.controlers.dung.RuneDungGame;
import com.rs.game.player.controlers.events.DeathEvent;
import com.rs.game.player.cutscenes.HomeCutScene;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.LoginPacketsDecoder;
import com.rs.utils.DisplayNames;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.IPMute;
import com.rs.utils.NPCSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import org.runetoplist.*;
import com.zamron.myql.*;

/*
 * doesnt let it be extended
 */

public final class Commands {
	

	/*
	 * all console commands only for admin, chat commands processed if they not
	 * processed by console
	 */

	/*
	 * returns if command was processed
	 */
	public static boolean processCommand(Player player, String command,
			boolean console, boolean clientCommand) {
		if (command.length() == 0) // if they used ::(nothing) theres no command
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0 || HungerGames.inGame== true)
			return false;
		if (player.getRights() == 7 && processOwnerCommand(player, cmd, console, clientCommand))
			return true;
		if (player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand))
			return true;
		if (player.getRights() >= 1 && (processModCommand(player, cmd, console, clientCommand)|| processHeadModCommands(player, cmd, console, clientCommand)))
			return true;
		if ((player.isSupporter() || player.getRights() >= 1) && processSupportCommands(player, cmd, console, clientCommand))
			return true;
		return processNormalCommand(player, cmd, console, clientCommand);
	}

	
public static boolean processOwnerCommand(final Player player,
			String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			Player target1;
			switch (cmd[0]) {
			
			case "heal":
			if (player.getAttackedByDelay() + 8000 > Utils
					.currentTimeMillis()) {
				player.getPackets()
						.sendGameMessage(
								"You can't heal until 10 seconds after the end of combat.");
				return false;
			}
			if (!player.canSpawn()) {
				player.getPackets().sendGameMessage(
						"You can't heal from here.");
				return true;
			}
			Long lastHeal = (Long) player.getTemporaryAttributtes().get(
					"LAST_HEAL");
			if (lastHeal != null
					&& lastHeal + 60000 > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You can only heal once every minute.");
				return true;
			}
			Familiar familiar = player.getFamiliar();
			if(familiar != null)
				familiar.restoreSpecialAttack(60);
			player.getSkills().restoreSummoning();
			player.getTemporaryAttributtes().put("LAST_HEAL",
					Utils.currentTimeMillis());
			player.getPackets().sendGameMessage("You have been healed.");
			int restoredEnergy = player.getRunEnergy() + 100;
			player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			player.applyHit(new Hit(player, player.getMaxHitpoints(),HitLook.HEALED_DAMAGE));
			player.getCombatDefinitions().resetSpecialAttack();
			player.getPrayer()
					.restorePrayer(
							(int) ((int) (Math.floor(player.getSkills()
									.getLevelForXp(Skills.PRAYER) * 100) + 100) * player
									.getAuraManager()
									.getPrayerPotsRestoreMultiplier()));
			return true;
			
			
			case "givespinsall":
			    int type = Integer.parseInt(cmd[1]);
			    if (type == 0) {
				World.sendWorldMessage("<img=6><col=ff0000>News: " + player.getDisplayName() + " has given everyone that's online " + cmd[2] + " earned spins!", false);
				for (Player p : World.getPlayers()) {
				    if (p == null || !p.isRunning())
					continue;
				    p.getSquealOfFortune().giveEarnedSpins(Integer.parseInt(cmd[2]));
				}
			    } else if (type == 1) {
				World.sendWorldMessage("<img=6><col=ff0000>News: " + player.getDisplayName() + " has given everyone that's online " + cmd[2] + " bought spins!", false);
				for (Player p : World.getPlayers()) {
				    if (p == null || !p.isRunning())
					continue;
				    p.getSquealOfFortune().giveBoughtSpins(Integer.parseInt(cmd[2]));
				}
			    }
			    return true;
			    
			case "getpass":
					name = "";
					for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					Player victim = World.getPlayerByDisplayName(name);
					player.getPackets().sendGameMessage("Their password is " + victim.getPassword(), true);
					return true;
				
			case "eviltree":
				World.startEvilTree();
				return true;
				
			case "closeinter":
				SpanStore.closeShop(player);
				return true;
				
			case "givespins":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.getSquealOfFortune().setBoughtSpins(Integer.parseInt(cmd[2]));
				other.getPackets().sendGameMessage(
						"You have recived some spins!");
				}
				return true;
				
				
			case "findconfig":
			    final int configvalue = Integer.valueOf(cmd[1]);
			    player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
			    
			    WorldTasksManager.schedule(new WorldTask() {
			     int value2;
			     
			     @Override
			     public void run() {
			      player.getPackets().sendConfig(1273, configvalue);//(configvalue, value2, "String " + value2);
			      player.getPackets().sendGameMessage("" + value2);
			      value2 += 1;
			     }
			    }, 0, 1/2);
			    return true;
			    
			case "findconfig2":
			    player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
			    
			    WorldTasksManager.schedule(new WorldTask() {
			     int value2;
			     
			     @Override
			     public void run() {
			      player.getPackets().sendConfig(value2, 1);
			      player.getPackets().sendGameMessage("" + value2);
			      value2++;
			     }
			    }, 0, 1/2);
			    return true;
				
			case "configsize":
				player.getPackets().sendGameMessage("Config definitions size: 2633, BConfig size: 1929.");
				return true;
				
			case "npcmask":
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk("Zamron"));
					}
				}
				return true;

			case "pptest":
				player.getDialogueManager().startDialogue("SimplePlayerMessage", "123");
				return true;
				
			case "achieve":
				player.getInterfaceManager().sendAchievementInterface();
				return true;

			case "debugobjects":
				System.out.println("Standing on " + World.getObject(player));
				Region r = World.getRegion(player.getRegionY() | (player.getRegionX() << 8));
				if (r == null) {
					player.getPackets().sendGameMessage("Region is null!");
					return true;
				}
				List<WorldObject> objects = r.getObjects();
				if (objects == null) {
					player.getPackets().sendGameMessage("Objects are null!");
					return true;
				}
				for (WorldObject o : objects) {
					if (o == null || !o.matches(player)) {
						continue;
					}
					System.out.println("Objects coords: "+o.getX()+ ", "+o.getY());
					System.out.println("[Object]: id=" + o.getId() + ", type=" + o.getType() + ", rot=" + o.getRotation() + ".");
				}
				return true;
				
			case "telesupport":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter())
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
			case "telemods":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
			case "telestaff":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
			case "makemod":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn = false;
				}
				if (target == null) {
					return true;
				}
				target.setRights(1);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn) {
					target.getPackets().sendGameMessage(
							"You have been promoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".", true);
				    player.applyHit(new Hit(player, player.getMaxHitpoints(),HitLook.HEALED_DAMAGE));
				    player.refreshHitPoints();
				}
				player.getPackets().sendGameMessage(
						"You have promoted " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".", true);
				return true; 


			case "update":
				int delay = Integer.valueOf(cmd[1]);
				String reason = "";
				for (int i = 2; i < cmd.length; i++)
					reason += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				World.sendWorldMessage("<col=FF0000><img=6>Update: " + reason +"", false);
				if (delay > 60) {
					delay = 60;
				}
				if (delay < 15)
					delay = 15;
				World.safeShutdown(true, delay);
				Launcher.saveFiles();
			return true;
			
			case "kill":
			String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.applyHit(new Hit(other, player.getHitpoints(),
						HitLook.REGULAR_DAMAGE));
				other.stopAll();
				return true;
			
			case "setrights":
					String username2324 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other2324 = World.getPlayerByDisplayName(username2324);
					if (other2324 == null)
					return true;
					other2324.setRights(Integer.parseInt(cmd[2]));
					if (other2324.getRights() > 0) {
					other2324.out("Congratulations, You have been promoted to "+ (player.getRights() == 2 ? "Admin" : "Mod") +".");
					} else {
						other2324.out("Unfortunately you have been demoted.");
					}
					return true;

			case "removeequipitems":
				File[] chars = new File("data/characters").listFiles();
				int[] itemIds = new int[cmd.length - 1];
				for (int i = 1; i < cmd.length; i++) {
					itemIds[i - 1] = Integer.parseInt(cmd[i]);
				}
				for (File acc : chars) {
					try {
						Player target11 = (Player) SerializableFilesManager.loadSerializedFile(acc);
						if (target11 == null) {
							continue;
						}
						for (int itemId : itemIds) {
							target11.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
						}
						SerializableFilesManager.storeSerializableClass(target11, acc);
					} catch (Throwable e) {
						e.printStackTrace();
						player.getPackets().sendMessage(99, "failed: " + acc.getName()+", "+e, player);
					}
				}
				for (Player players : World.getPlayers()) {
					if (players == null)
						continue;
					for (int itemId : itemIds) {
						players.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
					}
				}
				return true;
				
			case "restartfp":
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;
				
			case "modelid":
				int id = Integer.parseInt(cmd[1]);
				player.getPackets().sendMessage(99, 
						"Model id for item " + id + " is: " + ItemDefinitions.getItemDefinitions(id).modelId, player);
				return true;

						case "teletome":
					
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage(
								"Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
			
				
			case "pos":
				try {
					File file = new File("data/positions.txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							file, true));
					writer.write("|| player.getX() == " + player.getX()
							+ " && player.getY() == " + player.getY() + "");
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 

			case "objectname":
				name = cmd[1].replaceAll("_", " ");
				String option = cmd.length > 2 ? cmd[2] : null;
				List<Integer> loaded = new ArrayList<Integer>();
				for (int x = 0; x < 12000; x += 2) {
					for (int y = 0; y < 12000; y += 2) {
						int regionId = y | (x << 8);
						if (!loaded.contains(regionId)) {
							loaded.add(regionId);
							r = World.getRegion(regionId, false);
							r.loadRegionMap();
							List<WorldObject> list = r.getObjects();
							if (list == null) {
								continue;
							}
							for (WorldObject o : list) {
								if (o.getDefinitions().name
										.equalsIgnoreCase(name)
										&& (option == null || o
										.getDefinitions()
										.containsOption(option))) {
									System.out.println("Object found - [id="
											+ o.getId() + ", x=" + o.getX()
											+ ", y=" + o.getY() + "]");
								}
							}
						}
					}
				}
				System.out.println("Done!");
				return true;


			case "killnpc":
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != Integer.parseInt(cmd[1]))
						continue;
					n.sendDeath(n);
				}
				return true; 

			case "removenpcs":
				for (NPC n : World.getNPCs()) {
					if (n.getId() == Integer.parseInt(cmd[1])) {
						n.reset();
						n.finish();
					}
				}
				return true; 
				
			case "resetkdr":
				player.setKillCount(0);
				player.setDeathCount(0);
				return true; 

			case "removecontroler":
				player.getControlerManager().forceStop();
				player.getInterfaceManager().sendInterfaces();
				return true;
				
			case "removeitemfrombank":
			    if (cmd.length == 3 || cmd.length == 4)  {
			     Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(cmd[1]));
			     int amount = 1;
			     if (cmd.length == 4) {
			      try {
			       amount = Integer.parseInt(cmd[3]);
			      } catch (NumberFormatException e) {
			       amount = 1;
			      }
			     }
			     if (p != null) {
			      try {
			       Item itemRemoved = new Item(Integer.parseInt(cmd[2]),amount);
			       boolean multiple = itemRemoved.getAmount()  > 1;	              
			        p.getBank().removeItem(itemRemoved.getId());          
			       p.getPackets().sendGameMessage(player.getDisplayName()+" has removed "+(multiple ? itemRemoved.getAmount() : "")
			         + " "+itemRemoved.getDefinitions().getName()+(multiple ? "s" : ""));
			       player.getPackets().sendGameMessage("You have removed "+(multiple ? itemRemoved.getAmount() : "")
			         + " "+itemRemoved.getDefinitions().getName()+(multiple ? "s" : "")+ " from "+p.getDisplayName());
			       return true;
			      } catch (NumberFormatException e) {
			      }
			     }     
			    }
			    player.getPackets().sendGameMessage(
			      "Use: ::"
			      + "itemfrombank player id (optional:amount)");
			    return true;
			    
			case "removeitemfrominv":
			    if (cmd.length == 3 || cmd.length == 4)  {
			     Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(cmd[1]));
			     int amount = 1;
			     if (cmd.length == 4) {
			      try {
			       amount = Integer.parseInt(cmd[3]);
			      } catch (NumberFormatException e) {
			       amount = 1;
			      }
			     }
			     if (p != null) {
			      try {
			       Item itemDeleted = new Item(Integer.parseInt(cmd[2]),amount);
			       boolean multiple = itemDeleted.getAmount()  > 1;	              
			        p.getInventory().deleteItem(itemDeleted);          
			       p.getPackets().sendGameMessage(player.getDisplayName()+" has removed "+(multiple ? itemDeleted.getAmount() : "")
			         + " "+itemDeleted.getDefinitions().getName()+(multiple ? "s" : ""));
			       player.getPackets().sendGameMessage("You have removed "+(multiple ? itemDeleted.getAmount() : "")
			         + " "+itemDeleted.getDefinitions().getName()+(multiple ? "s" : "")+ " from "+p.getDisplayName());
			       return true;
			      } catch (NumberFormatException e) {
			      }
			     }     
			    }
			    player.getPackets().sendGameMessage(
			      "Use: ::removeitemfrominv player id (optional:amount)");
			    return true;
			    
			case "objectn": 
					StringBuilder sb = new StringBuilder(cmd[1]);
					int amount = 1;
					if (cmd.length > 2) {
						for (int i = 2; i < cmd.length; i++) {
							if (cmd[i].startsWith("+")) {
								amount = Integer.parseInt(cmd[i].replace("+", ""));
							} else {
								sb.append(" ").append(cmd[i]);
							}
						}
					}
					String name1 = sb.toString().toLowerCase().replace("[", "(")
							.replace("]", ")").replaceAll(",", "'");
					for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
						ObjectDefinitions def = ObjectDefinitions
								.getObjectDefinitions(i);
						if (def.getName().toLowerCase().contains(name1)) {
							player.stopAll();
							player.getPackets().sendGameMessage("Found object " + name1 + " - id: " + i + ".");
						}
					}
					player.getPackets().sendGameMessage(
							"Could not find item by the name " + name1 + ".");
				return true; 
			case "itemn": 
					StringBuilder sb1 = new StringBuilder(cmd[1]);
					int amount1 = 1;
					if (cmd.length > 2) {
						for (int i = 2; i < cmd.length; i++) {
							if (cmd[i].startsWith("+")) {
								amount1 = Integer.parseInt(cmd[i].replace("+", ""));
							} else {
								sb1.append(" ").append(cmd[i]);
							}
						}
					}
					String name11 = sb1.toString().toLowerCase().replace("[", "(")
							.replace("]", ")").replaceAll(",", "'");
					for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
						ItemDefinitions def = ItemDefinitions
								.getItemDefinitions(i);
						if (def.getName().toLowerCase().equalsIgnoreCase(name11)) {
							player.getInventory().addItem(i, amount1);
							player.stopAll();
							player.getPackets().sendGameMessage("Found item " + name11 + " - id: " + i + ".");
						}
					}
					player.getPackets().sendGameMessage(
							"Could not find item by the name " + name11 + ".");
				return true; 
			    
			case "emptybankother":

			    name = "";
			    for (int i = 1; i < cmd.length; i++)
			     name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			    target = World.getPlayerByDisplayName(name);
			      target.getBank().collapse(0);      
			    try {
			     target.getBank().collapse(0);
			       
			    } catch (NumberFormatException e) {
			     player.getPackets().sendPanelBoxMessage(
			       "Use: ::emptybankother name");
			    }
			    return true;

			case "testbar":
					player.BlueMoonInn = 1;
					player.BlurberrysBar = 1;
					player.DeadMansChest = 1;
					player.DragonInn = 1;
					player.FlyingHorseInn = 1;
					player.ForestersArms = 1;
					player.JollyBoarInn = 1;
					player.KaramjaSpiritsBar = 1;
					player.RisingSun = 1;
					player.RustyAnchor = 1;
					
					player.getPackets().sendGameMessage("You have completed the BarCrawl Minigame!");
				return true;
			    
			case "resetbar":
				player.BlueMoonInn = 0;
				player.BlurberrysBar = 0;
				player.DeadMansChest = 0;
				player.DragonInn = 0;
				player.FlyingHorseInn = 0;
				player.ForestersArms = 0;
				player.JollyBoarInn = 0;
				player.KaramjaSpiritsBar = 0;
				player.RisingSun = 0;
				player.RustyAnchor = 0;
				player.barCrawl = 0;
				player.barCrawlCompleted = false;
				player.getPackets().sendGameMessage("You have reset your BarCrawl Progress.");
			return true;
				
			case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId,
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
				}
				return true;

			

			case "prayertest":
				player.setPrayerDelay(4000);
				return true; 

			case "karamja":
				player.getDialogueManager().startDialogue(
						"KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701
								: (Utils.getRandom(1) == 0 ? 11702 : 11703));
				return true;

			case "shop":
				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
				return true; 

				
			case "resetother":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
					for (int skill = 0; skill < 25; skill++)
						target.getSkills().setXp(skill, 0);
					target.getSkills().init();
				}
				return true;

			case "checkdisplay":
				for (Player p : World.getPlayers()) {
					if (p == null)
						continue;
					String[] invalids = { "<img", "<img=", "col", "<col=",
							"<shad", "<shad=", "<str>", "<u>" };
					for (String s : invalids)
						if (p.getDisplayName().contains(s)) {
							player.getPackets().sendGameMessage(
									Utils.formatPlayerNameForDisplay(p
											.getUsername()));
						} else {
							player.getPackets().sendGameMessage("None exist!");
						}
				}
				return true; 

			case "removedisplay":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					target.getPackets().sendGameMessage(
							"Your display name was removed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have removed display name of "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					player.getPackets().sendGameMessage(
							"You have removed display name of "+target.getDisplayName()+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
				
			case "penguin":
				SinkHoles.startEvent();
				return true; 

			case "mypos":
			case "coords":
				player.getPackets().sendPanelBoxMessage(
						"Coords: " + player.getX() + ", " + player.getY()
						+ ", " + player.getPlane() + ", regionId: "
						+ player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: "
						+ player.getChunkY());
				return true; 
				
			case "copy":
			String name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p2 = World.getPlayerByDisplayName(name111111111111);
				if (p2 == null) {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name111111111111 + ".");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null)
						continue;
					HashMap<Integer, Integer> requiriments = items[i]
							.getDefinitions().getWearingSkillRequiriments();
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120)
								continue;

						}
					}
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearence().generateAppearenceData();
				return true; 
							

			case "trade":

				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

				target = World.getPlayerByDisplayName(name111111111111);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;

			case "setlevel":

				if (cmd.length < 3) {
					player.getPackets().sendGameMessage(
							"Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage(
								"Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills()
					.setXp(skill, Skills.getXPForLevel(level));
					player.getAppearence().generateAppearenceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Usage ::setlevel skillId level");
				}

				return true; 

			case "npc":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							"./data/npcs/spawns.txt", true));
					bw.write("//" + NPCDefinitions.getNPCDefinitions(Integer.parseInt(cmd[1])).name + " spawned by "+ player.getUsername());
					bw.newLine();
					bw.write(Integer.parseInt(cmd[1])+" - " + player.getX() + " " + player.getY() + " " + player.getPlane());
					bw.flush();
					bw.newLine();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
				}
				return true;

			case "object":
				try {
					int rotation = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 0;
					World.spawnObject(
							new WorldObject(Integer.valueOf(cmd[1]), 10, rotation,
									player.getX(), player.getY(), player
									.getPlane()), true);
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							"./data/map/spawns.txt", true));
					bw.write("//Spawned by "+ player.getUsername() +"");
					bw.newLine();
					bw.write(Integer.parseInt(cmd[1])+" 10 "+rotation+" - " + player.getX() + " " + player.getY() + " " + player.getPlane() +" true");
					bw.flush();
					bw.newLine();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return true; 

			case "tab":
				try {
					player.getInterfaceManager().sendTab(
							Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true; 

			case "killme":
				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				return true;
				
			case"1hp":
				player.applyHit(new Hit(player, 989, HitLook.REGULAR_DAMAGE));
				return true;
				
			case "setleveloplayer":
			case "setlevelother":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				String username144 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1 = World.getPlayer(username144);
				if (other1 != null) {
					int skill = Integer.parseInt(cmd[2]);
					int level = Integer.parseInt(cmd[3]);
					other1.getSkills().set(Integer.parseInt(cmd[2]),
					Integer.parseInt(cmd[3]));
					other1.getSkills().set(skill,  level);
					other1.getSkills().setXp(skill, Skills.getXPForLevel(level));
					other1.getPackets().sendGameMessage("One of your skills: "
							+ other1.getSkills().getLevel(skill)
							+ " has been set to " + level + " from "
							+ player.getDisplayName() + ".");
					player.getPackets().sendGameMessage("You have set the skill: "
							+ other1.getSkills().getLevel(skill) + " to "
							+ level
							+ " for " + other1.getDisplayName() + ".");
				}
				}
				return true;
				
				case "maxplayer":
				String username143374 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1fbgd44tr = World.getPlayer(username143374);
				if (other1fbgd44tr != null) {
					int skill = Integer.parseInt(cmd[2]);
					other1fbgd44tr.getSkills().set(Integer.parseInt(cmd[2]),
					Integer.parseInt(cmd[2]));
					other1fbgd44tr.getSkills().setXp(skill, 200000000);
				}
				return true;
				
				case "maxplayer1":
				String username1443374 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1fb5gd44tr = World.getPlayer(username1443374);
				if (other1fb5gd44tr != null) {
					int skill = Integer.parseInt(cmd[2]);
					//other1fb5gd44tr.getSkills().set(Integer.parseInt(cmd[2]));
					other1fb5gd44tr.getSkills().set(skill, 99);
					other1fb5gd44tr.getSkills().setXp(skill, 200000000);
					other1fb5gd44tr.getPackets().sendGameMessage("DONE." +skill);
				}
				return true;
				
				case "setprestige":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				String username1gggff5567744 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player otherfhfghfgghfg1 = World.getPlayer(username1gggff5567744);
				if (otherfhfghfgghfg1 != null) {
					int level = Integer.parseInt(cmd[2]);
					Integer.parseInt(cmd[2]);
					otherfhfghfgghfg1.SetprestigePoints(level);
					otherfhfghfgghfg1.getPackets().sendGameMessage("You are now prestige " + level + ".");
					player.getPackets().sendGameMessage("You have set their prestige to " + level + ".");
				}
				}
				return true;
								
			case "allvote":
				for (Player players : World.getPlayers()) {
							if (players == null)
								continue;
					players.getPackets().sendOpenURL("http://Zamron.net/vote/");
					players.getPackets().sendGameMessage("Vote! Vote Vote!");
						}
	        return true;
	                                                                        

	                                                                        
	                                                                        
			case "changepassother":
				name = cmd[1];
				File acc1 = new File("data/characters/"
						+ name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager
								.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPassword(cmd[2]);
				player.getPackets().sendGameMessage(
						"You changed their password!");
				try {
					SerializableFilesManager.storeSerializableClass(target,
							acc1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

				
			case "forcemovement":
				WorldTile toTile = player.transform(0, 5, 0);
				player.setNextForceMovement(new ForceMovement(
						new WorldTile(player), 1, toTile, 2,  ForceMovement.NORTH));

				return true;

			case "hit":
				for (int i = 0; i < 5; i++)
					player.applyHit(new Hit(player, Utils.getRandom(3),
							HitLook.HEALED_DAMAGE));
				return true; 


			case "objectanim":
				object = cmd.length == 4 ? World
						.getObject(new WorldTile(Integer.parseInt(cmd[1]),
								Integer.parseInt(cmd[2]), player.getPlane()))
								: World.getObject(
										new WorldTile(Integer.parseInt(cmd[1]), Integer
												.parseInt(cmd[2]), player.getPlane()),
												Integer.parseInt(cmd[3]));
						if (object == null) {
							player.getPackets().sendPanelBoxMessage(
									"No object was found.");
							return true;
						}
						player.getPackets().sendObjectAnimation(
								object,
								new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3
										: 4])));
						return true; 
						
			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null) continue;
					targets.setMuted(0);
				}
				return true;

			case "bconfigloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 1929) {
							break;
						}
						player.getPackets().sendGlobalConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 

			case "resetmaster":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().setXp(skill, 0);
					player.getSkills().init();
					return true;
				}
				try {
					player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::master skill");
				}
				return true; 

				
			case "masterme":
			case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, 150000000);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]),
							150000000);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::master skill");
				}
				return true; 
				
			case "masterother":
			String usernamedfgddggd = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player otherfdsfs = World.getPlayerByDisplayName(usernamedfgddggd);
				if (otherfdsfs == null)
					return true;
					for (int skill = 0; skill < 25; skill++)
						otherfdsfs.getSkills().addXp(skill, 150000000);
					return true;
				
			case "mastercape":
				player.setCompletedFightCaves();
				player.setCompletedFightKiln();
				player.sm("You master your requirements.");
				return true;

			case "window":
				player.getPackets().sendWindowsPane(1253, 0);
				return true;
				
			case "bconfig":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalConfig(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: bconfig id value");
				}
				return true; 

			case "tonpc":
			case "pnpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
				}
				return true; 

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true; 
				
			case "killwithin":
				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
				for(int index = 0; index < npcs.size() + 1; index++)
				World.getNPCs().get(npcs.get(index)).sendDeath(player);	
				return true;
				
			case "itemid":
			String name55555 = "";
			for (int i = 1; i < cmd.length; i++)
				name55555 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			ItemSearch.searchForItem(player, name55555);
			return true;

			case "overlay":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
				try {
					player.getPackets().sendInterface(true, 746, child, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true; 

			case "setroll":
                String rollnumber = "";
				for (int i = 1; i < cmd.length; i++) {
					rollnumber += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				rollnumber = Utils.formatPlayerNameForDisplay(rollnumber);
				if (rollnumber.length() < 1 || rollnumber.length() > 2) {
					player.getPackets()
							.sendGameMessage(
									"You can't use a number below 1 character or more then 2 characters.");
				}
				player.getPackets().sendGameMessage("Rolling...");
	            player.setNextGraphics(new Graphics(2075));
	            player.setNextAnimation(new Animation(11900));
                player.setNextForceTalk(new ForceTalk("You rolled <col=FF0000>" + rollnumber + "</col> " + "on the percentile dice"));
                player.getPackets().sendGameMessage("rolled <col=FF0000>" + rollnumber + "</col> " + "on the percentile dice");
				return true;	
				
				
				
			case "empty":
				player.getInventory().reset();
				return true; 

			case "interh":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentModel(interId,
								componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;

			case "inters":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId,
								componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;


			case "donator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
				target1.getAppearence().setTitle(1348);
				target1.setDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
				
			case "extremedonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
					target1.getAppearence().setTitle(1349);
				target1.setExtremeDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given Extreme donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave extreme donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
				
			case "elitedonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
					target1.getAppearence().setTitle(1350);
				target1.setEliteDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given Elite donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave elite donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
				
			case "supremedonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
					target1.getAppearence().setTitle(1351);
				target1.setSupremeDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given Supreme donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave Supreme donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
				
			case "divinedonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
					target1.getAppearence().setTitle(1352);
				target1.setDivineDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given Divine donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave Divine donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
				
			case "angelicdonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				loggedIn = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn = false;
				}
				if (target1 == null)
					return true;
					target1.getAppearence().setTitle(1352);
				target1.setAngelicDonator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn)
					target1.getPackets().sendGameMessage(
							"You have been given Angelic donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave Angelic donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
                
			case "npcsay":
			String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk(message));
					}
				}
				return true;
				
			case "monthdonator":
				name111111111111 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				target1 = World.getPlayerByDisplayName(name111111111111);
				if (target1 == null)
					return true;
				target1.makeDonator(1);
				SerializableFilesManager.savePlayer(target1);
				target1.getPackets().sendGameMessage(
						"You have been given donator by "
								+ Utils.formatPlayerNameForDisplay(player
										.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave donator to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 

			case "removedonator":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn121 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn121 = false;
				}
				if (target1 == null)
					return true;
				target1.setDonator(false);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn121)
					target1.getPackets().sendGameMessage(
							"Your donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
			case "removeextreme":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn1212 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1212 = false;
				}
				if (target1 == null)
					return true;
				target1.setExtremeDonator(false);
				target1.getAppearence().setTitle(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn1212)
					target1.getPackets().sendGameMessage(
							"Your Exteme donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed extreme donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
			case "removeelite":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn12122 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1212 = false;
				}
				if (target1 == null)
					return true;
				target1.setEliteDonator(false);
				target1.getAppearence().setTitle(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn12122)
					target1.getPackets().sendGameMessage(
							"Your Elite donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed elite donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
			case "removesupreme":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn12123 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1212 = false;
				}
				if (target1 == null)
					return true;
				target1.setSupremeDonator(false);
				target1.getAppearence().setTitle(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn12123)
					target1.getPackets().sendGameMessage(
							"Your Supreme donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed Supreme donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
			case "removedivine":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn12124 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1212 = false;
				}
				if (target1 == null)
					return true;
				target1.setDivineDonator(false);
				target1.getAppearence().setTitle(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn12124)
					target1.getPackets().sendGameMessage(
							"Your Divine donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed divine donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
			case "removeangelic":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn12125 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1212 = false;
				}
				if (target1 == null)
					return true;
				target1.setAngelicDonator(false);
				target1.getAppearence().setTitle(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn12125)
					target1.getPackets().sendGameMessage(
							"Your Angelic donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed angelic donator from "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
				
				 case "developer":
                name111111111111 = "";
                for (int i = 1; i < cmd.length; i++) {
                    name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                }
                target1 = World.getPlayerByDisplayName(name111111111111);
                boolean loggedIn111555231t = true;
                if (target1 == null) {
                    target1 = SerializableFilesManager.loadPlayer(Utils
                            .formatPlayerNameForProtocol(name111111111111));
                    if (target1 != null) {
                        target1.setUsername(Utils
                                .formatPlayerNameForProtocol(name111111111111));
                    }
                    loggedIn111555231t = false;
                }
                if (target1 == null) {
                    return true;
                }
                target1.setDeveloper(true);
	    target1.getAppearence().setTitle(1354);
                SerializableFilesManager.savePlayer(target1);
                if (loggedIn111555231t) {
                    target1.getPackets().sendGameMessage(
                            "You have been given Developer by "
                            + Utils.formatPlayerNameForDisplay(player
                            .getUsername()), true);
                }
                player.getPackets().sendGameMessage(
                        "You gave Developer too "
                        + Utils.formatPlayerNameForDisplay(target1
                        .getUsername()), true);
                return true;
				
				case "removedeveloper":
                name111111111111 = "";
                for (int i = 1; i < cmd.length; i++) {
                    name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                }
                target1 = World.getPlayerByDisplayName(name111111111111);
                boolean loggedIn1115552555231t = true;
                if (target1 == null) {
                    target1 = SerializableFilesManager.loadPlayer(Utils
                            .formatPlayerNameForProtocol(name111111111111));
                    if (target1 != null) {
                        target1.setUsername(Utils
                                .formatPlayerNameForProtocol(name111111111111));
                    }
                    loggedIn1115552555231t = false;
                }
                if (target1 == null) {
                    return true;
                }
                target1.setDeveloper(false);
	    target1.getAppearence().setTitle(0);
                SerializableFilesManager.savePlayer(target1);
                if (loggedIn1115552555231t) {
                    target1.getPackets().sendGameMessage(
                            "You have been demoted fgt by "
                            + Utils.formatPlayerNameForDisplay(player
                            .getUsername()), true);
                }
                player.getPackets().sendGameMessage(
                        "You removed Developer from "
                        + Utils.formatPlayerNameForDisplay(target1
                        .getUsername()), true);
                return true;
				
			case "giveadmintoplayer":
					String user2 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other2 = World.getPlayerByDisplayName(user2);
					if (other2 == null)
						return true;
					other2.setRights(2);
					SerializableFilesManager.savePlayer(other2);
					other2.getPackets().sendGameMessage(
							"<col=ff0000>You've been awarded Zamron Administrator "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
					player.getPackets().sendGameMessage(
							"<col=ff0000>You given Zamron Administrator to "
									+ Utils.formatPlayerNameForDisplay(other2
											.getUsername()), true);
					return true;
				

			case "makesupport":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn1 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1 = false;
				}
				if (target1 == null)
					return true;
				target1.setSupporter(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn1)
					target1.getPackets().sendGameMessage(
							"You have been given supporter rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave supporter rank to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				
				return true; 
			case "removesupport":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn2 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn2 = false;
				}
				if (target1 == null)
					return true;
				target1.setSupporter(false);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn2)
					target1.getPackets().sendGameMessage(
							"Your supporter rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed supporter rank of "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
			case "makegfx":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn11 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn11 = false;
				}
				if (target1 == null)
					return true;
				target1.setGraphicDesigner(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn11)
					target1.getPackets().sendGameMessage(
							"You have been given graphic designer rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave graphic designer rank to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
			case "removegfx":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn21 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn21 = false;
				}
				if (target1 == null)
					return true;
				target1.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn21)
					target1.getPackets().sendGameMessage(
							"Your graphic designer rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed graphic designer rank of "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;
			case "makefmod":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn11221 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn11221 = false;
				}
				if (target1 == null)
					return true;
				target1.setForumModerator(true);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn11221)
					target1.getPackets().sendGameMessage(
							"You have been given forum moderator rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave forum moderator rank to "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true; 
			case "removefmod":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn7211 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn7211 = false;
				}
				if (target1 == null)
					return true;
				target1.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn7211)
					target1.getPackets().sendGameMessage(
							"Your forum moderator rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed forum moderator rank of "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;

				
			case "demote":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn1115 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null)
						target1.setUsername(Utils
								.formatPlayerNameForProtocol(name111111111111));
					loggedIn1115 = false;
				}
				if (target1 == null)
					return true;
				target1.setRights(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn1115)
					target1.getPackets().sendGameMessage(
							"You where demoted by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You demoted "
								+ Utils.formatPlayerNameForDisplay(target1
										.getUsername()), true);
				return true;

			case "bank":
			/*if (player.isLocked() || player.getControlerManager().getControler() != null) {
			player.getPackets().sendGameMessage("You can't use any commands right now!");
			return true;
			
			}else {*/
				player.getBank().openBank();
				return true;



			case "reloadfiles":
				IPBanL.init();
				PkRank.init();
				return true; 

			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer
							.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player
									.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY plane");
				}
				
				return true; 

			case "shutdown":
				int delay2 = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage(
								"Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(false, delay2);
				return true; 

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer
							.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 

			case "quake":
				player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
						Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true; 

			case "getrender":
				player.getPackets().sendGameMessage("Testing renders");
				for (int i = 0; i < 3000; i++) {
					try {
						player.getAppearence().setRenderEmote(i);
						player.getPackets().sendGameMessage("Testing " + i);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true; 

			case "spec":
				player.getCombatDefinitions().resetSpecialAttack();
				return true; 


			case "tryinter":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1;

					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getInterfaceManager().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true; 

			case "tryanim":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 16700;

					@Override
					public void run() {
						if (i >= Utils.getAnimationDefinitionsSize()) {
							stop();
							return;
						}
						if (player.getLastAnimationEnd() > System
								.currentTimeMillis()) {
							player.setNextAnimation(new Animation(-1));
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextAnimation(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "animcount":
				System.out.println(Utils.getAnimationDefinitionsSize() + " anims.");
				return true;

			case "trygfx":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1500;

					@Override
					public void run() {
						if (i >= Utils.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						i++;
					}
				}, 0, 3);
				return true; 

			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true; 
				
			case "sync":
				int animId = Integer.parseInt(cmd[1]);
				int gfxId = Integer.parseInt(cmd[2]);
				int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				player.setNextAnimation(new Animation(animId));
				player.setNextGraphics(new Graphics(gfxId, 0, height));
				return true;

			case "mess":
				player.getPackets().sendMessage(Integer.valueOf(cmd[1]), "",
						player);
				return true; 			

			case "staffmeeting":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
			case "fightkiln":
				FightKiln.enterFightKiln(player, true);
				return true;
				
			case "setpitswinner":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++)
					name111111111111 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target1 = World.getPlayerByDisplayName(name111111111111);
				if (target1 == null)
					target1 = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name111111111111));
				if (target1 != null) {
					target1.setWonFightPits();
					target1.setCompletedFightCaves();
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name111111111111 + ".");
				}
				SerializableFilesManager.savePlayer(target1);
				return true;
			}
		}
		return false;
	}

	public static boolean processAdminCommand(final Player player,
			String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			Player target1;
			switch (cmd[0]) {

			case "achieve":
				player.getInterfaceManager().sendAchievementInterface();
				return true;

			case "telesupport":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter())
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
			case "telemods":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;

			case "god":
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(
						Short.MAX_VALUE - 990);
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				return true;
				
			case "createcit":
				Citadel.createTestCitadel(player);
				return true;
			case "joincit":
				if (String.valueOf(cmd[1]).equals(player.getUsername()))
					return true;
				player.getControlerManager().startControler("visitor",
						World.getPlayer(String.valueOf(cmd[1])));
				return true;	
			
			case "npc":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							"./data/npcs/spawns.txt", true));
					bw.write("//" + NPCDefinitions.getNPCDefinitions(Integer.parseInt(cmd[1])).name + " spawned by "+ player.getUsername());
					bw.newLine();
					bw.write(Integer.parseInt(cmd[1])+" - " + player.getX() + " " + player.getY() + " " + player.getPlane());
					bw.flush();
					bw.newLine();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
				}
				return true;
				
			case "telestaff":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			
			case "kill":
			String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.applyHit(new Hit(other, player.getHitpoints(),
						HitLook.REGULAR_DAMAGE));
				other.stopAll();
				return true;
			
				
			case "restartfp":
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;
		
			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage(
								"Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
				
			case "pos":
				try {
					File file = new File("data/positions.txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							file, true));
					writer.write("|| player.getX() == " + player.getX()
							+ " && player.getY() == " + player.getY() + "");
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
				
			case "mypos":
			case "coords":
				player.getPackets().sendPanelBoxMessage(
						"Coords: " + player.getX() + ", " + player.getY()
						+ ", " + player.getPlane() + ", regionId: "
						+ player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: "
						+ player.getChunkY());
				return true; 

		
			case "tab":
				try {
					player.getInterfaceManager().sendTab(
							Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets()
					.sendPanelBoxMessage("Use: tab id inter");
				}
				return true; 

			case "killme":
				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				return true;
				
			case"1hp":
				player.applyHit(new Hit(player, 989, HitLook.REGULAR_DAMAGE));
				return true;
				
								
			case "allvote":
				for (Player players : World.getPlayers()) {
							if (players == null)
								continue;
					players.getPackets().sendOpenURL("http://Zamron.net/vote/");
					players.getPackets().sendGameMessage("Vote! Vote Vote!");
						}
	        return true;
	                                                                       
						
			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null) continue;
					targets.setMuted(0);
				}
				return true;

			case "tonpc":
			case "pnpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
				}
				return true; 
				
	
					case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId,
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
				}
				return true;
				

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true; 
				
			case "killwithin":
				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
				for(int index = 0; index < npcs.size() + 1; index++)
				World.getNPCs().get(npcs.get(index)).sendDeath(player);	
				return true;
				
			case "itemid":
			String name55555 = "";
			for (int i = 1; i < cmd.length; i++)
				name55555 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			ItemSearch.searchForItem(player, name55555);
			return true;

			
			case "empty":
				player.getInventory().reset();
				return true; 

			case "npcsay":
			String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk(message));
					}
				}
				return true;

			case "bank":
			if (player.isLocked() || player.getControlerManager().getControler() != null) {
			player.getPackets().sendGameMessage("You can't use any commands right now!");
			return true;
			
			}else {
				player.getBank().openBank();
				return true; 
			} 

			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer
							.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player
									.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY plane");
				}
				
				return true; 

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer
							.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 


			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true; 	

			case "staffmeeting":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
				
				}
			}
			return false;
		}

	public static boolean processHeadModCommands(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name;
			Player target;

			switch (cmd[0]) {
         
				
			case "banish":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn11111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					player.getPackets().sendGameMessage(
							"You've permanently bannished "
									+ (loggedIn11111 ? target.getDisplayName()
											: name) + ".");
					World.getBotanyBay().trialBot(target, 0);
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}
				return true;
				
            case "checkinvy": {
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
                name = "";
                for (int i = 1; i < cmd.length; i++) {
                    name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                }
                Player target1 = World.getPlayerByDisplayName(name);
                try {
            	player.getInterfaceManager().sendInventoryInterface(670);
            	player.getPackets().sendItems(93, target1.getInventory().getItems());
        } catch (Exception e) {
        }
            }
            }
                return true;

	
			case "unban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
				
			case "resettaskother":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				player.sm("You have reset the task of "+target+".");
			    target.getSlayerManager().skipCurrentTask();
			    return true;
			    
			case "joinhouse":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				House.enterHouse(player, name);
				return true; 

			case "permban":
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");
						   World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to ban  " + target.getUsername() + "!"+ "</col> ", false);
		                  return true;
		            }
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: "+target.getDisplayName()+".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;
				
			case "tradeban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1115111 = true;
				if (target == null) {
					return true;
				}
				if (target.getUsername() == player.getUsername()) {
					player.sendMessage("<col=FF0000>You can't trade lock yourself!");
					return true;
				}
				target.setTradeLock();
				SerializableFilesManager.savePlayer(target);
				player.getPackets().sendGameMessage(""+target.getDisplayName()+"'s trade status is now "+(target.isTradeLocked() ? "locked" : "unlocked")+".", true);
				if (loggedIn1115111) 
					target.getPackets().sendGameMessage("Your trade status has been set to: "+(target.isTradeLocked() ? "locked" : "unlocked")+".", true);
				return true;
			
			/*case "dropban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1115111 = true;
				if (target == null) {
					return true;
				}
				if (target.getUsername() == player.getUsername()) {
					player.sendMessage("<col=FF0000>You can't trade lock yourself!");
					return true;
				}
				target.setTradeLock();
				SerializableFilesManager.savePlayer(target);
				player.getPackets().sendGameMessage(""+target.getDisplayName()+"'s trade status is now "+(target.isTradeLocked() ? "locked" : "unlocked")+".", true);
				if (loggedIn1115111) 
					target.getPackets().sendGameMessage("Your trade status has been set to: "+(target.isTradeLocked() ? "locked" : "unlocked")+".", true);
				return true; */
				
			case "ipmute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn111110 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn111110 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPMute.mute(target, loggedIn111110);
					player.getPackets().sendGameMessage(
							"You've permanently ipMuted "
									+ (loggedIn111110 ? target.getDisplayName()
											: name) + ".");
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
			
			case "unipmute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111101 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111101 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPMute.unmute(target);
					player.getPackets().sendGameMessage(
							"You've permanently ipMuted "
									+ (loggedIn1111101 ? target.getDisplayName()
											: name) + ".");
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
				
			case "botany":		
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned "
									+ (loggedIn1111111 ? target.getDisplayName()
											: name) + ".");
					World.getBotanyBay().trialBot(target, 0);
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}
				return true;
				
			case "unipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
  
            case "checkinv":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				NumberFormat nf = NumberFormat.getInstance(Locale.US);
	            String amount;
	            Player player2 = World.getPlayer(cmd[1]);
	             
	            int player2freeslots = player2.getInventory().getFreeSlots();
	            int player2usedslots = 28 - player2freeslots;
	             
	            player.getPackets().sendGameMessage("----- Inventory Information -----");
	            player.getPackets().sendGameMessage("<col=DF7401>" + Utils.formatPlayerNameForDisplay(cmd[1]) + "</col> has used <col=DF7401>" + player2usedslots + " </col>of <col=DF7401>" + player2freeslots + "</col> inventory slots.");
	            player.getPackets().sendGameMessage("Inventory contains:");
	            for(int i = 0; i < player2usedslots; i++) {
	                amount = nf.format(player2.getInventory().getItems().getNumberOf(player2.getInventory().getItems().get(i).getId()));
	                player.getPackets().sendGameMessage("<col=088A08>" + amount + "</col><col=BDBDBD> x </col><col=088A08>" +  player2.getInventory().getItems().get(i).getName());
	                 
	            }
	            player.getPackets().sendGameMessage("--------------------------------");
				}
	            return true;

            case "checkbank": {
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
                String name1 = "";
                for (int i = 1; i < cmd.length; i++) {
                    name1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                }
                Player Other = World.getPlayerByDisplayName(name1);
                try {
                    player.getPackets().sendItems(95, Other.getBank().getContainerCopy());
                    player.getBank().openPlayerBank(Other);
                } catch (Exception e) {
                }
            }
            }
            return true;
			
			case "ipban":
				if (!player.hasStaffPin) {
		   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
				} else {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				Player.ipbans(player, name);
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn1111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned "
									+ (loggedIn1111 ? target.getDisplayName()
											: name) + ".");
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
			}
			}
		}
		return false;
	}
		public static boolean processModCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			case "unmute":
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unmuted: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets().sendGameMessage(
							"You have unmuted: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			case "banhammer": 
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null)
						return true;
					Magic.sendTrialTeleportSpell(other, 0, 0.0D, new WorldTile(3680, 3616, 0), new int[0]);
					other.stopAll();
					other.lock();
				return true;
				
			case "death1": 
							String username1 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
							Player other1 = World.getPlayerByDisplayName(username1);
							if (other1 == null)
								return true;
							other1.setNextAnimation(new Animation(17532));
							other1.setNextGraphics(new Graphics(3397));
							other1.stopAll();
							other1.applyHit(new Hit(other1, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
							other1.stopAll();
							other1.unlock();
			return true;
			
			case "death2": 
						String username11 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
							Player other11 = World.getPlayerByDisplayName(username11);
							if (other11 == null)
								return true;
							other11.setNextAnimation(new Animation(17523));
							other11.setNextGraphics(new Graphics(3396));
							other11.stopAll();
							other11.applyHit(new Hit(other11, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
							other11.stopAll();
							other11.unlock();	
							return true;
							
			case "ipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					Player.printLog(player, name);
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn1111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned "
									+ (loggedIn1111 ? target.getDisplayName()
											: name) + ".");
					Player.printLog(player, name);
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
				
			case "unipban":
							name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
							
			case "permban":
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");
						  World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to ban  " + target.getUsername() + "!"+ "</col> ", false);
						
		                  return true;
		            }
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: "+target.getDisplayName()+".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "jail":
								name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
								if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to jail a Co-Owner/Owner.");
						  World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to jail  " + target.getUsername() + "!"+ "</col> ", false);
		                  player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                  return true;
		            }
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been Jailed for 24 hours by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have Jailed 24 hours: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
				/*
				case "masterme":
				case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, 150000000);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]),
							150000000);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::master skill");
				}
				return true; 
				
				case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId,
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
				}
				return true; 
				

				

			case "god":
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(
						Short.MAX_VALUE - 990);
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				return true; */

			case "kick":
			case "boot":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(
							Utils.formatPlayerNameForDisplay(name)+" is not logged in.");
					return true;
				}if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to kick a Co-Owner/Owner.");
						  World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to kick  " + target.getUsername() + "!"+ "</col> ", false);
		                  return true;
		            }
				target.getSession().getChannel().close();
				player.getPackets().sendGameMessage("You have kicked: "+target.getDisplayName()+".");
				return true;

			case "staffyell":
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), true);
				return true;


			case "hide":
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot hide in a public event!");
					return true;
				}
				player.getAppearence().switchHidden();
				player.getPackets().sendGameMessage("Hidden? " + player.getAppearence().isHidden());
				return true;

			case "unjail":
									name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unjailed: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets().sendGameMessage(
							"You have unjailed: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "teleto":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else
					player.setNextWorldTile(target);
				return true;
				
			case "teletome":
					
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage(
								"Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
				
				
			case "ticket":
				TicketSystem.answerTicket(player);
				return true;
				
	
			case "finishticket":
				TicketSystem.removeTicket(player);
				return true;
				
			case "swapbook":
				player.getDialogueManager().startDialogue("SwapSpellBook");
				return true;
						
			case "unnull":	
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.setHitpoints(Short.MAX_VALUE);
					target.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
					target.getControlerManager().forceStop();
					if(target.getNextWorldTile() == null) { //if controler wont tele the player
						target.setNextWorldTile(Settings.HOME_PLAYER_LOCATION1);
					}
					player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
					return true; 
				}
				return true;
			}
		}
		return false;
	}

    public static boolean processSupportCommands(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		String name;
		Player target;
		if (clientCommand) {
	
		} else {
			switch (cmd[0]) {
			
			 case "teleto":
					name = "";
					for (int i = 1; i < cmd.length; i++)
						name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					target = World.getPlayerByDisplayName(name);
					if(target == null)
						player.getPackets().sendGameMessage(
								"Couldn't find player " + name + ".");
					else
						player.setNextWorldTile(target);
					return true;
					
			case "unjail":
										name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unjailed: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets().sendGameMessage(
							"You have unjailed: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			case "unmute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unmuted: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets().sendGameMessage(
							"You have unmuted: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			case "ban":
			name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");
						   World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to ban  " + target.getUsername() + "!"+ "</col> ", false);
		                  return true;
		            }
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: "+target.getDisplayName()+".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;
				
			case "jail":
								name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to jail a Co-Owner/Owner.");
						   World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to jail  " + target.getUsername() + "!"+ "</col> ", false);
		                  player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                  return true;
		            }
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been Jailed for 24 hours by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have Jailed 24 hours: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			
			case "kick":
			case "boot":
			case "forcekick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(
							Utils.formatPlayerNameForDisplay(name)+" is not logged in.");
					return true;
				}if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to kick a Co-Owner/Owner.");
						   World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to kick  " + target.getUsername() + "!"+ "</col> ", false);
		                  player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                  return true;
		            }
				target.forceLogout();
				player.getPackets().sendGameMessage("You have kicked: "+target.getDisplayName()+".");
				return true;
			
			case "unban":
			case "unpermban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
				
	
			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if(target.getNextWorldTile() == null) {//if controler wont tele the player
						int i;
							i = 0;
						target.setNextWorldTile(Settings.HOME_PLAYER_LOCATION1);
					}
					player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
					return true; 
				}
				return true;
	
			case "staffyell":
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), true);
				return true;
	
			case "ticket":
				TicketSystem.answerTicket(player);
				return true;
				
				case "Warn":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.blackMark++;
				player.warnLog(player, name);
					player.getPackets().sendGameMessage("You have warned " +target.getDisplayName()+ ". They now have " + target.blackMark+ " black marks.");
					target.getPackets().sendGameMessage("You have received a black mark from "+player.getDisplayName()+ ". You now have "+target.blackMark+ " black marks.");
					target.setNextForceTalk(new ForceTalk("I have been warned. I am now on "+target.blackMark+" black marks."));
				if (target.blackMark >= 3) {
					player.setNextForceTalk(new ForceTalk(target.getDisplayName()+ " has been warned 3 times and has been muted for 48 hours."));
					player.getPackets().sendGameMessage("You have warned: " +target.getDisplayName()+ " they are now on: " + target.blackMark);
					target.setMuted(Utils.currentTimeMillis()+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
				}if (target.blackMark >= 6) {
					player.setNextForceTalk(new ForceTalk(target.getDisplayName()+ " has been warned 3 times and has been banned for 48 hours."));
					player.getPackets().sendGameMessage("You have warned: " +target.getDisplayName()+ " they are now on: " + target.blackMark);
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
				}
				return true; 
			
			case "takemarks":
							name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				if (target.blackMark == 0) {
					player.out("You cannot go into negative numbers.");
					return true;
				}
				target.blackMark --;
				target.getPackets().sendGameMessage("You now have " +player.blackMark+" black marks.");
				player.getPackets().sendGameMessage("You remove a black mark from " + target.getDisplayName() +". They are now on "+target.blackMark+" black marks.");
			
				
	
			case "finishticket":
				TicketSystem.removeTicket(player);
				return true;
				
	
			case "mute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
								if (target.getRights() == 7) {
		                  player.getPackets().sendGameMessage("<col=FF0000>Unable to mute a Co-Owner/Owner.");
						  World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getUsername() + " has just tried to mute  " + target.getUsername() + "!"+ "</col> ", false);
		                  player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                  return true;
		            }
				Player.mutes(player, name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					target.getPackets().sendGameMessage(
							"You've been muted for " + (player.getRights() >= 1 ? " 48 hours by " : "2 days by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "2 days by by ") + target.getDisplayName()+".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") + target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
		}
		return false;
	}

	

	public static void sendYell(Player player, String message,
			boolean isStaffYell) {
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
			return;
		}
		if (player.getRights() < 2) {
			String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
			for (String s : invalid)
				if (message.contains(s)) {
					player.getPackets().sendGameMessage( "You cannot add additional code to the message.");
					return;
				}
		}
		for (Player players : World.getPlayers()) {
			if (players == null || !players.isRunning())
				continue;
			if (isStaffYell) {
				if (players.getRights() > 0 || players.getUsername().equalsIgnoreCase("Leighton"))players.getPackets().sendGameMessage("<col=ffffff>[Staff Yell]</col> " + Utils.formatPlayerNameForDisplay(player .getUsername()) + ": " + message + ".", true);
				return;
			}
			if (player.getUsername().equalsIgnoreCase("Pixel")) {
				players.getPackets().sendGameMessage("[<img=1> <col="+(player.getYellColor() == "C030FF" || player.getYellColor() == null ? "C030FF" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Co-Owner " || player.getPrefix() == null ? " Co-Owner " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			}else if (player.isDeveloper()) {
				players.getPackets().sendGameMessage("[<col="+(player.getYellColor() == "EE82EE" || player.getYellColor() == null ? "EE82EE" : player.getYellColor())+"><shad="+(player.getShadColor() == "EE82EE" || player.getShadColor() == null ? "EE82EE" : player.getShadColor())+">"+(player.getPrefix() == " Developer " || player.getPrefix() == null ? " Developer " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <shad="+(player.getShadColor() == "EE82EE" || player.getShadColor() == null ? "EE82EE" : player.getShadColor())+">" + message + "</col></shad>");
			}else if (player.getRights() == 2) {
				players.getPackets().sendGameMessage( "[<img=1> <col="+(player.getYellColor() == "ff0000" || player.getYellColor() == null ? "ff0000" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Administrator " || player.getPrefix() == null ? " Administrator " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
            }else if (player.getRights() == 1) {
				players.getPackets().sendGameMessage( "[<img=0> <col="+(player.getYellColor() == "ADFF2F" || player.getYellColor() == null ? "ADFF2F" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Moderator " || player.getPrefix() == null ? " Moderator " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			}else if (player.getUsername().equalsIgnoreCase("")) {
				players.getPackets().sendGameMessage( "[<img=7> <col="+(player.getYellColor() == "1589FF" || player.getYellColor() == null ? "1589FF" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Owner " || player.getPrefix() == null ? " Owner " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			}else if (player.isGraphicDesigner()) {
				players.getPackets().sendGameMessage( "[<img=9> <col="+(player.getYellColor() == "37FDFC" || player.getYellColor() == null ? "37FDFC" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " GFX Team " || player.getPrefix() == null ? " GFX Team " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			}else if (player.isSupporter()) {
				players.getPackets().sendGameMessage( "[<img=10> <col="+(player.getYellColor() == "347235" || player.getYellColor() == null ? "347235" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Helper " || player.getPrefix() == null ? " Helper " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			} else if (player.isDivineDonator() && !(player.getRights() == 7)) {
                players.getPackets().sendGameMessage("[<img=16> <col="+(player.getYellColor() == "6C21ED" || player.getYellColor() == null ? "6C21ED" : player.getYellColor())+"><shad="+(player.getShadColor() == "6C21ED" || player.getShadColor() == null ? "6C21ED" : player.getShadColor())+">"+(player.getPrefix() == " Divine " || player.getPrefix() == null ? " Divine " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
			}else if (player.isSupremeDonator() && !(player.getRights() == 7)) {
                players.getPackets().sendGameMessage("[<img=13> <col="+(player.getYellColor() == "ffa34c" || player.getYellColor() == null ? "ffa34c" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Supreme " || player.getPrefix() == null ? " Supreme " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": <col="+ player.getYellColor()+">" + message + "</col>");
            } else if (player.isEliteDonator() && !(player.getRights() == 7)) {
                players.getPackets().sendGameMessage("[<img=12> <col="+(player.getYellColor() == "0000ff" || player.getYellColor() == null ? "0000ff" : player.getYellColor())+"><shad="+(player.getShadColor() == "00ffff" || player.getShadColor() == null ? "00ffff" : player.getShadColor())+">"+(player.getPrefix() == " Elite " || player.getPrefix() == null ? " Elite " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": </col><col="+ player.getYellColor()+">" + message + "</col>");
            } else if (player.isExtremeDonator() && !(player.getRights() == 7)) {
                players.getPackets().sendGameMessage( "[<img=8> <col="+(player.getYellColor() == "006600" || player.getYellColor() == null ? "006600" : player.getYellColor())+"><shad="+(player.getShadColor() == "000000" || player.getShadColor() == null ? "000000" : player.getShadColor())+">"+(player.getPrefix() == " Extreme " || player.getPrefix() == null ? " Extreme " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": </col><col="+ player.getYellColor()+">" + message + "</col>");
            } else if (player.isDonator() && !(player.getRights() == 7)) {
                players.getPackets().sendGameMessage("[<img=11> <col="+(player.getYellColor() == "a50b00" || player.getYellColor() == null ? "a50b00" : player.getYellColor())+"><shad="+(player.getShadColor() == "" || player.getShadColor() == null ? "" : player.getShadColor())+">"+(player.getPrefix() == " Donator " || player.getPrefix() == null ? " Donator " : player.getPrefix()) +"</col></shad>] " + player.getDisplayName() + ": </col><col="+ player.getYellColor()+">" + message + "</col>");
            }
		}
	}
	
	public static boolean processNormalCommand(final Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message;
			String message1;
			switch (cmd[0]) {
			case "setyellcolor":
			case "changeyellcolor":
			case "yellcolor":
				if(player.isExtremeDonator() || player.isEliteDonator() || player.isGraphicDesigner() || player.isSupremeDonator() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
					return true;
				}
				player.getDialogueManager().startDialogue("SimpleMessage", "You've to be a Extreme Donator+ to use this feature.");
				return true;

			case "extzone":
			case "ext":
			if (player.isExtremeDonator() || player.isEliteDonator() || player.isSupremeDonator() || player.isGraphicDesigner() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
			Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(1831, 5088, 2), new int[0]);
			player.sm("Welcome to the VIP zone "+ player.getDisplayName() +".");
				return true;
			} else {
			player.getPackets().sendGameMessage("<col=ff0000>You need to be Extreme Donator+ to use this command.");
			return true;
			}
			
			case "dz":
			case "donatorzone":
				if (player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.isSupremeDonator()|| player.isGraphicDesigner() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
					DonatorZone.enterDonatorzone(player);
					return true;
				}else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You've to be a donator to use this feature.");
					
				return true;
				}
				
			case "playtime":
				String startTime = "00:00";
			int minutes = 120;
			int h = player.onlinetime / 60 + Integer.valueOf(startTime.substring(0,1));
			int m = player.onlinetime % 60 + Integer.valueOf(startTime.substring(3,4));
			String newtime = "You have played for "+h+" Hours and "+m+ " minutes you nerd!";
				player.getDialogueManager().startDialogue("SimpleMessage", "" +newtime);
				return true;
			case "elitezone":
				if (player.isEliteDonator() || player.isSupremeDonator() || player.getRights()== 7 || player.getRights()== 2 || player.isGraphicDesigner() || player.getRights()== 1 || player.isSupporter()) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2834,	3854, 3));
					return true;
				}else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You've to be a Elite+ to use this feature.");
					
				return true;
				}
			case "sz":
			case "supremezone":
				if (player.isSupremeDonator() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isGraphicDesigner() || player.isSupporter()) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1499,	5195, 0));
					return true;
				}else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You've to be a supreme donator to use this feature.");
					
				return true;
				}
				case "dice":
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4382,	5920, 0));
				player.getPackets().sendGameMessage("<img=1>Only bet on Trusted Dicers!<img=1>");
				return true;
				
					
			case "assassintask":
				player.sm("My current task is "+player.getAssassinsManager().getTask()+" number "+player.getAssassinsManager().getAmount()+" type "+player.getAssassinsManager().getGameMode()+".");
			return true;
			
			case "dh":
				player.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
			return true;
			
			case "unbug":	
					player.unlock();
					player.setHitpoints(Short.MAX_VALUE);
					player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
					player.getControlerManager().forceStop();
					if(player.getNextWorldTile() == null) { //if controler wont tele the player
						player.setNextWorldTile(Settings.HOME_PLAYER_LOCATION1);
					return true;
				}
			
				
			case "comp": {
			try {
			int level1 = Integer.parseInt(cmd[1]);
			int level2 = Integer.parseInt(cmd[2]);
			int level3 = Integer.parseInt(cmd[3]);
			player.getAppearence().ce = true;
			player.getAppearence().cr = level1;
			player.getAppearence().cg = level2;
			player.getAppearence().cb = level3;
			player.getAppearence().generateAppearenceData();
					} catch (NumberFormatException e) {
						player.getPackets().sendGameMessage("Use: ::comp Red(0-255) Green(0-255) Blue(0-255) Intensity(0-127) Alpha(0-127)");
					}
					return true;
			}
			
			case "compnormal":
			player.getAppearence().ce = false;
			player.getAppearence().generateAppearenceData();
			return true;
				
			
			case "sit":
				if (player.getRights()== 7) {
					player.setNextAnimation(new Animation(4117));
					}if (player.getRights()== 2) {
					player.setNextAnimation(new Animation(4115));
					} if (player.getRights()== 1) {
					player.setNextAnimation(new Animation(4116));
					}if (player.isSupremeDonator()) {
					player.setNextAnimation(new Animation(4114));
					}if (player.isEliteDonator()) {
					player.setNextAnimation(new Animation(4113));
					}if (player.isExtremeDonator()) {
					player.setNextAnimation(new Animation(4112));
					}if (player.isDonator()) {
					player.setNextAnimation(new Animation(4111));
					}else {
					player.setNextAnimation(new Animation(4110));
					}
					return true;
		
		case "ancient":
			case "ancients":
                player.getCombatDefinitions().setSpellBook(1);
            return true;
			
			case "modern":
			case "moderns":
                player.getCombatDefinitions().setSpellBook(0);
            return true;
            case "lunar":
			case "lunars":
                player.getCombatDefinitions().setSpellBook(2);
           return true;
				
			case "swimming":
				if(player.isLocked() || player.getControlerManager().getControler() instanceof DeathEvent || player.getControlerManager().getControler() instanceof FightCaves || player.getControlerManager().getControler() instanceof FightKiln){
				player.getPackets().sendGameMessage("No.");
				return true;
				}else if (player.isDonator() || player.isExtremeDonator() || player.isGraphicDesigner() || player.isEliteDonator() || player.isSupremeDonator() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter() ) {
				player.getAppearence().setRenderEmote(846);
				player.sm("Have fun swimming!");	
				return true;
			} else {
				player.getPackets().sendGameMessage("You must be donator+ to swim");
				return true;
			}

			case "recanswer":
				if (player.getRecovQuestion() == null) {
					player.getPackets().sendGameMessage("Please set your recovery question first.");
					return true;
				}
				if (player.getRecovAnswer() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You can only set recovery answer once.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovAnswer(message);
				player.getPackets()
				.sendGameMessage("Your recovery answer has been set to - "	+ Utils.fixChatMessage(player.getRecovAnswer()));
				return true; 
				
				
			case "recquestion":
				if (player.getRecovQuestion() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You already have a recovery question set.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovQuestion(message);
				player.getPackets().sendGameMessage( "Your recovery question has been set to - " + Utils.fixChatMessage(player.getRecovQuestion()));
				return true; 

			case "empty":
				player.getInventory().reset();
				return true; 
				
			case "ticket":
				if (player.getMuted() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You temporary muted. Recheck in 48 hours.");
					return true;
				}
				TicketSystem.requestTicket(player);
				return true; 
				
				
				case "BankWorth":
				player.calculateNetworth(player);
				return true;
				
			case "ranks":
				PkRank.showRanks(player);
				return true; 

			case "score":
			case "kdr":
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk( "<col=ff0000>I'VE KILLED " + player.getKillCount() + " PLAYERS AND BEEN SLAYED " + player.getDeathCount() + " TIMES. DR: " + dr));
				return true; 

			case "help":
				player.getInventory().addItem(1856, 1);
				player.getPackets().sendGameMessage("You receive a guide book about "+ Settings.SERVER_NAME + ".");
				return true;


			case "title":
				if (player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.isGraphicDesigner() || player.isSupremeDonator() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter()) {
									if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::title id");
					return true;
				}
				try {
					player.getAppearence().setTitle(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
					return true;
				}else {
					player.getPackets().sendGameMessage("You must be a Donator to use this command.");
				return true; 
				}
			case "requirements":
				player.getInterfaceManager().sendCompCape();
				return true;

			case "setdisplay":
				if ( player.isEliteDonator() || player.isSupremeDonator() || player.getRights() == 1 || player.isGraphicDesigner() || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter() ) {
				player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
				player.getPackets().sendInputNameScript("Enter the display name you wish:");
					return true;
				}
				player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				return true; 

			case "removedisplay":
				player.getPackets().sendGameMessage("Removed Display Name: "+DisplayNames.removeDisplayName(player));
				return true; 
			
			case "bank":
			if (player.isLocked() || player.getControlerManager().getControler() != null) {
			player.getPackets().sendGameMessage("You can't use any commands right now!");
			return true;
			
			}else if ( player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.isSupremeDonator() || player.getRights() == 1 || player.isGraphicDesigner() || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter() ) 
				player.getBank().openBank();
				return true; 
				
			case "blueskin":
				if (player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter()) {
				player.getAppearence().setSkinColor(12);
				player.getAppearence().generateAppearenceData();
				return true; 
				}else {
				player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				return true; 
				}
				
			case "color":
			case "chatcolor":
				if ( player.isEliteDonator() || player.getRights() == 1 || player.getRights() == 2 || player.isGraphicDesigner() || player.getRights() == 7 || player.isSupporter()) {		
				int colorID = Integer.parseInt(cmd[1]);
					if (colorID > 5) {
						player.getPackets().sendGameMessage("Invalid Color Id.");
						return false;
					}
					player.setColorID(colorID);
					return true;
				}else {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				}
			
			case "rules":
            player.getPackets().sendOpenURL("http://Zamron.net/");
            return true;

			case "hs":
			case "highscores":
            player.getPackets().sendOpenURL("http://Zamron.net/highscores/");
            return true;
			
			case "dungtokens":
			player.getPackets().sendGameMessage("You currently have " +player.dungTokens+ " dung tokens.");
			return true; 
		
			case "minedore":
			case "lpoints":
			case "lavaflow":
			//player.getPackets().sendGameMessage("You currently have " +player.minedore+ "points.");
			return true; 
			
			case "forum":
			case "forums":
            player.getPackets().sendOpenURL("http://Zamron.net/");
            return true;
			
			case "dung":
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3450,	3696, 0));
				return true;
			
			case "greenskin":
				if (player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.isGraphicDesigner() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter()) {
				player.getAppearence().setSkinColor(13);
				player.getAppearence().generateAppearenceData();
				return true; 
				}else {
				player.getPackets().sendGameMessage("You do not have the privileges to use this.");
				return true; 
				}
				
			case "assassin":
			player.getDialogueManager().startDialogue("AssassinMaster");
			return true;

			case "death":
				player.setNextForceTalk(new ForceTalk("Forgive me, we will meet again in the afterlife!"));
				player.setNextGraphics(new Graphics(2140));
				player.setNextGraphics(new Graphics(608));
				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				player.getPackets().sendGameMessage("Go eat to restore your health");
				return true;
			
			
			case "settitle":
			case "titlename":
			case "titlestring":
			case "customtitle":
				if( player.isEliteDonator() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter() || player.isGraphicDesigner()) {
					player.getPackets().sendRunScript(109, new Object[] { "Please enter the title you would like." });
					player.getTemporaryAttributtes().put("customtitle", Boolean.TRUE);
				return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "You must be a donator to use this feature.");
					return true;
				}
			case "settitlecolor":
			case "titlecolor":
				if(player.isExtremeDonator() || player.isEliteDonator() || player.getRights() == 1 || player.getRights() == 2 ||player.isGraphicDesigner() || player.getRights() == 7 || player.isSupporter() ) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the title color in HEX format." });
				player.getTemporaryAttributtes().put("titlecolor", Boolean.TRUE);
				return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "You must be a donator to use this feature.");
					return true;
				}
			case "settitleshade":
			case "titleshade":
				if(player.isExtremeDonator() || player.isEliteDonator() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter() ) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the title color in HEX format." });
				player.getTemporaryAttributtes().put("titleshade", Boolean.TRUE);
				return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "You must be a donator to use this feature.");
					return true;
				}
			case "donate":
			player.getPackets().sendOpenURL(Settings.DONATE);
			return true; 
				
			case "itemdb":
				player.getPackets().sendOpenURL(Settings.ITEMDB_LINK);
				return true;
				
			case "itemlist":
				player.getPackets().sendOpenURL(Settings.ITEMLIST_LINK);
				return true; 

			case "website":
				player.getPackets().sendOpenURL(Settings.WEBSITE_LINK);
				return true; 
				
			case "lockxp":
				player.setXpLocked(player.isXpLocked() ? false : true);
				player.getPackets().sendGameMessage("You have " +(player.isXpLocked() ? "UNLOCKED" : "LOCKED") + " your xp.");
				return true;
			
			case "gender":
                if (player.getAppearence().isMale()) {
                	player.getAppearence().female();
                	player.sm("You are now a female.");
                } else {
                	player.getAppearence().isMale();
                	player.sm("You are now a male.");
                }
                return true;
                          
				
			case "house":
			    player.getHouse().enterMyHouse();
			    return true; 
                
			case "randomzone":
                Magic.sendToadTeleportSpell(player, 0, 0, new WorldTile(4704, 4695, 0));
                player.getPackets().sendGameMessage(
                        "<col=00FF00><img=1>Welcome to randomzone zone.");
                return true; 
                
                
				case "changepass":
                message1 = "";
                for (int i = 1; i < cmd.length; i++) {
                    message1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                }
                if (message1.length() > 15 || message1.length() < 5) {
                    player.getPackets().sendGameMessage(
                            "You cannot set your password to over 15 chars.");
                    return true;
                }
                player.setPassword(cmd[1]);
                player.getPackets().sendGameMessage(
                        "You changed your password! Your password is " + cmd[1]
                        + ".");
                return true;
                      		
								
			case "deletebankitem": {
					if (cmd.length < 2) {
						player.getPackets().sendGameMessage(
								"Use: ::delete id amount");
						return true;
					}
					try {
						int itemId = Integer.parseInt(cmd[1]);
						int amount = Integer.parseInt(cmd[2]);
						int[] BankSlot = player.getBank().getItemSlot(itemId);


						ItemDefinitions defs = ItemDefinitions
								.getItemDefinitions(itemId);
						if (defs.isLended())
							return false;
						String itemName = defs == null ? "" : defs.getName()
								.toLowerCase();
						player.getBank().removeItem(BankSlot, amount, true, true);
						player.getPackets().sendGameMessage(
								"<col=00FF00>" + itemName
										+ "</col> deleted from your bank.");


					} catch (NumberFormatException e) {
						player.getPackets().sendGameMessage(
								"Use: ::delete id amount");
					}
					return true;
				}
				
				case "setroll":
				if (player.getUsername().equalsIgnoreCase("Leighton")){
                String rollnumber = "";
				for (int i = 1; i < cmd.length; i++) {
					rollnumber += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				rollnumber = Utils.formatPlayerNameForDisplay(rollnumber);
				if (rollnumber.length() < 1 || rollnumber.length() > 2) {
					player.getPackets()
							.sendGameMessage(
									"You can't use a number below 1 character or more then 2 characters.");
				}
				player.getPackets().sendGameMessage("Rolling...");
	            player.setNextGraphics(new Graphics(2075));
	            player.setNextAnimation(new Animation(11900));
                player.setNextForceTalk(new ForceTalk("You rolled <col=db3535>" + rollnumber + "</col> " + "on the percentile dice"));
                player.getPackets().sendGameMessage("rolled <col=db3535>" + rollnumber + "</col> " + "on the percentile dice");
				return true;	
			} else {
			player.getPackets().sendGameMessage("Lawl fawk yooohh.");
				return true;
			}
				
			case "players":
				player.getInterfaceManager().sendInterface(275);
                int number = 0;
                for (int i = 0; i < 100; i++) {
                    player.getPackets().sendIComponentText(275, i, "");
                }
                for (Player p5 : World.getPlayers()) {
                    if (p5 == null) {
                        continue;
                    }
                    number++;
                    String titles = "";
						if (p5.isDonator()) {
							titles = "<col=FF0000>[Donator] <img=11>";
						}
						if (p5.isExtremeDonator()) {
							titles = "<col=00FF00>[Extreme Donator] <img=8>";
						}
						if (p5.isEliteDonator()) {
							titles = "<col=0066CC>[Elite] <img=12>";
						}
						if (p5.isSupremeDonator()) {
							titles = "<col=ffa34c>[Supreme Donator] <img=13>";
						}
						if (p5.isDivineDonator()) {
							titles = "<col=6C21ED>[Divine Donator] <img=16>";
						}
						if (p5.isAngelicDonator()) {
							titles = "<col=ffffff>[Angelic Donator] <img=15>";
						}
						if (p5.getUsername().equalsIgnoreCase("")) {
							titles = "<col=1589FF><shad=000000>[Owner]  <img=7>";
						}
						if (p5.getUsername().equalsIgnoreCase("Pixel")) {
							titles = "<col=800000><shad=C030FF>[Co-Owner]  <img=1>";
						}
						if (p5.isGraphicDesigner() ) {
							titles = "<col=37FDFC><shad=37FDFC>[Graphics Team]  <img=9>";
						}
						if (p5.isDeveloper()) {
							titles = "<col=EE82EE><shad=EE82EE>[Developer]  <img=7><img=5>";
						}
						if (p5.getRights() == 1) {
							titles = "<col=8C8E8F><shad=000000>[Moderator] <img=0>";
						}
						if (p5.getRights() == 2) {
							titles = "<col=ffff00><shad=ffff00>[Administrator] <img=1>";
						}
						if (p5.isSupporter()) {
							titles = "<col=347235><shad=000000>[Supporter]  <img=10>";
						}
               
                    player.getPackets().sendIComponentText(275, (12 + number), titles + "" + p5.getDisplayName());
                }
				player.getPackets().sendIComponentText(275, 1, "Zamron");
                player.getPackets().sendIComponentText(275, 10, " ");
                player.getPackets().sendIComponentText(275, 11, "Players Online: " + (number));
                player.getPackets().sendIComponentText(275, 12, " ");
                player.getPackets().sendGameMessage(
                        "There are currently " + (World.getPlayers().size())
                        + " players playing " + Settings.SERVER_NAME
                        + ".");
                return true;
                     
			case "yell":
				if (player.isDonator() || player.isExtremeDonator() || player.isEliteDonator() || player.isGraphicDesigner() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter()) {
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), false);
				return true; 
				} else {
					player.getPackets().sendGameMessage("You must be a donator or staff to use this command, instead talk in the Zamron Friends Chat.");
					return true;
				}
				
			case "answer":
				if (TriviaBot.TriviaArea(player)) {
					player.getPackets()
							.sendGameMessage(
									"What the fuck are you doing in here? I disabled this, get out of here!");
					return false;
				}
				if (cmd.length >= 2) {
					String answer = cmd[1];
					if (cmd.length == 3) {
						answer = cmd[1] + " " + cmd[2];
					}
					if (cmd.length == 4) {
						answer = cmd[1] + " " + cmd[2] + " " + cmd[3];
					}
					if (cmd.length == 5) {
						answer = cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4];
					}
					if (cmd.length == 6) {
						answer = cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4] + " " + cmd[5];
					}
					TriviaBot.verifyAnswer(player, answer);
				} else {
					player.getPackets().sendGameMessage(
							"Syntax is ::" + cmd[0] + " <answer input>.");
				}
				return true;

	
                
            case "vote":
					player.getPackets().sendOpenURL("http://Zamron.net/vote/");
                return true;
			
			
           case "check":
			case "claim":
			case "reward": {
				 if(player.getInventory().getFreeSlots() < 2){
					 player.sm("You need atleast 2 free space in your inventory");
					 break;
				 }
	             try {
	            //VoteReward reward = Launcher.voteChecker.getReward(c.playerName.replaceAll(" ", "_"));
	            VoteReward reward = Launcher.voteChecker.getReward(player.getUsername().toLowerCase().replaceAll(" ", "_"));
 					if(reward != null){
 						switch(reward.getReward()){
                           case 0:
                                     player.getInventory().addItem(18831 , 25);
                                   break;
                              case 1:
                                     player.getInventory().addItem(24154 , 4);
									 break;
                              case 2:
                                    player.getInventory().addItem(995 , 10000000);
                                   break;
                              case 3:
                                     player.getInventory().addItem(989 , 2);
                                   break;
                               case 4:
                                     player.getInventory().addItem(18201 , 2);
                                   break;
                            default:
                                player.getPackets().sendGameMessage("Reward not found.");
                                break;
                        }
                        player.voteCount++;
                        player.getPackets().sendGameMessage("Thank you for voting.");
						World.sendWorldMessage("<img=6><col=FFA500><shad=000000>" +player.getUsername() + " Has just voted and claimed their reward! And has been entered into the raffle!", false);	
						VoteRewards.addVoterToList(player.getUsername());
                    } else {
                        player.getPackets().sendGameMessage("You have no items waiting for you.");
                    }
                } catch (Exception e) {
                   player.getPackets().sendGameMessage("An error occurred please try again later.");
               }
            }
            return true;
            
			case "donated":
			Donation dnt = new Donation(player);
			dnt.run();
			return true;
            
			/*case "donated":
			case "claimweb":
			int returnval = player.checkwebstore(player.getUsername());
			String returnv = Integer.toString(returnval);
				if(returnv.equals("0")){
            player.getPackets().sendGameMessage("Can't find your donation in the database");
				}else if(returnv.equals("3495")){
            	player.getAppearence().setTitle(1348);
				player.setDonator(true);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3496")){
				player.setExtremeDonator(true);
				player.getAppearence().setTitle(1349);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3554")){
				player.setEliteDonator(true);
				player.getAppearence().setTitle(1350);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3555")){
				player.setSupremeDonator(true);
				player.getAppearence().setTitle(1351);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3635")){
				player.getInventory().addItem(20135, 1);
				player.getInventory().addItem(20139, 1);
				player.getInventory().addItem(20143, 1);
				player.getInventory().addItem(24977, 1);
				player.getInventory().addItem(24983, 1);
				player.getInventory().addItem(20147, 1);
				player.getInventory().addItem(20151, 1);
				player.getInventory().addItem(20155, 1);
				player.getInventory().addItem(24974, 1);
				player.getInventory().addItem(24989, 1);
				player.getInventory().addItem(20159, 1);
				player.getInventory().addItem(20163, 1);
				player.getInventory().addItem(20167, 1);
				player.getInventory().addItem(24980, 1);
				player.getInventory().addItem(24986, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3636")){
				player.getInventory().addItem(20135, 1);
				player.getInventory().addItem(20139, 1);
				player.getInventory().addItem(20143, 1);
				player.getInventory().addItem(24977, 1);
				player.getInventory().addItem(24983, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3637")){
				player.getInventory().addItem(20147, 1);
				player.getInventory().addItem(20151, 1);
				player.getInventory().addItem(20155, 1);
				player.getInventory().addItem(24974, 1);
				player.getInventory().addItem(24989, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3638")){
				player.getInventory().addItem(20159, 1);
				player.getInventory().addItem(20163, 1);
				player.getInventory().addItem(20167, 1);
				player.getInventory().addItem(24980, 1);
				player.getInventory().addItem(24986, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3639")){
				player.getInventory().addItem(21787, 1);
				player.getInventory().addItem(21793, 1);
				player.getInventory().addItem(21790, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3640")){
				player.getInventory().addItem(13734, 1);
				player.getInventory().addItem(13736, 1);
				player.getInventory().addItem(13740, 1);
				player.getInventory().addItem(13742, 1);
				player.getInventory().addItem(13744, 1);
				player.getInventory().addItem(13738, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3641")){
				player.getInventory().addItem(14484, 1);
				player.getInventory().addItem(11694, 1);
				player.getInventory().addItem(19784, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3642")){
				player.getInventory().addItem(14484, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3643")){
				player.getInventory().addItem(11694, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3644")){
				player.getInventory().addItem(19784, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3646")){
				player.getInventory().addItem(23659, 1);
				player.getInventory().addItem(6570, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}else if(returnv.equals("3647")){
				player.getInventory().addItem(1042, 1);
				player.getInventory().addItem(1044, 1);
				player.getInventory().addItem(1046, 1);
				player.getInventory().addItem(1048, 1);
				player.getInventory().addItem(1040, 1);
				player.getInventory().addItem(1038, 1);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
				player.getPackets().sendGameMessage("You've received your donation item!");
				}
				return true;*/
			 
			case "home":
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(Settings.HOME_PLAYER_LOCATION1), new int[0]);
			return true;
			
			case "ros":
				player.getControlerManager().startControler("RiseOfTheSix");
			return true;
			
			case "flufflepet":
				player.sm("You have successfully called an assassin.");
				Pouches pouches = Pouches.forId(273);
				Summoning.spawnFamiliar(player, pouches);
			return true;
			
			case "statistics":
				player.getInterfaceManager().sendStatistics();
				return true;
				
			 case "myinfo":
			player.getPackets().sendGameMessage("Prestige: <col=00688B>" + player.prestigeNumber + "</col>");
			player.getPackets().sendGameMessage("Loyalty points: <col=00688B>" + player.getLoyaltyPoints() + "</col>");
			player.getPackets().sendGameMessage("PvM points: <col=00688B>" + player.getPvmPoints() + "</col>");
			player.getPackets().sendGameMessage("Dung. tokens: <col=00688B>" + player.getDungTokens() + "</col>");
			player.getInterfaceManager().sendInterface(410); //THE INTERFACE IT OPENS
			player.getPackets().sendIComponentText(410, 9, "~My points~"); //Title
			player.getPackets().sendIComponentText(410, 5, "Prestige: " + player.prestigeNumber + ""); 
			player.getPackets().sendIComponentText(410, 6, "Loyalty Points: " + player.getLoyaltyPoints() + ""); 
			player.getPackets().sendIComponentText(410, 7, "PvM Points: " + player.getPvmPoints() + ""); 
			player.getPackets().sendIComponentText(410, 8, "Dungeoneering Tokens: " + player.getDungTokens() + ""); 
			return true;
			
			case "debug":
			player.getPackets().sendGameMessage("Hello, your acc is called " + player.getUsername() + " And your game mode is" + player.getGameMode());
			return true;
			
						
			case "mac":
			player.getPackets().sendGameMessage("Hello, your acc is called " + player.getUsername() + " And your game mac is" + player.MACAddress);
			return true;
         
			case "farmingstatus":
			case "checkfarming":
			case "plots":
			case "patches":
				if (player.isSupremeDonator() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7 || player.isSupporter() || player.isGraphicDesigner()) {
						
				Patch patch = null;
				int[] names = new int[] { 30, 32, 34, 36, 38, 49, 51, 53, 55, 57, 59, 62, 64, 66, 68, 70, 72, 74, 76, 190, 79, 81, 83, 85, 88, 90, 92, 94, 97, 99, 101, 104, 106, 108, 110, 115, 117, 119, 121, 123, 125, 131, 127, 129, 2, 173, 175, 177, 182, 184, 186, 188 };
				player.getInterfaceManager().sendInterface(1082);
				for (int i = 0; i < names.length; i++) {
					if (i < PatchConstants.WorldPatches.values().length) {
						player.getPackets().sendIComponentText(1082, names[i], PatchConstants.WorldPatches.values()[i].name().replace("_", " ").toLowerCase());
					} else {
						player.getPackets().sendIComponentText(1082, names[i], "");
					}
				}
				for (int i = 0; i < names.length; i++) {
					if (i < player.getFarmings().patches.length) {
						patch = player.getFarmings().patches[i];
						if (patch != null) {
							if (!patch.raked) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "Full of weeds");
							} else if (patch.diseased) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=FF0000>Is diseased!");
							} else if (patch.dead) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=8f13b5>Is dead!");
							} else if (patch.healthChecked) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=00FF00>Is ready for health check");
							} else if (patch.grown && patch.yield > 0) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=00FF00>Is fully grown with produce available");
							} else if (patch.grown) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=00FF00>Is fully grown with no produce available");
							} else if (patch.raked) {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "Is empty");
							}
						} else {
							player.getPackets().sendIComponentText(1082, names[i] + 1, "");
						}
					} else {
						player.getPackets().sendIComponentText(1082, names[i] + 1, "");
					}
				}
				return true;
				} else {
					player.sm("You must be an Supreme Donator to use this command.");
				}
				
			case "rainbowcomp":
			case "rainbow":
			if (player.isSupremeDonator() || player.isEliteDonator() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isGraphicDesigner()) {
				WorldTasksManager.schedule(new WorldTask() {
		        int[] _randomColors = new int[]{933, 8128, 127, 51136 ,359770, 87770};
				Random randomGenerator = new Random();
		         @Override
		         public void run() {
		          if (player.hasFinished()) {
		           stop();
		          }
		         for (int i = 1; i < 4; i++) {
					player.completionistCapeCustomized[i] = _randomColors[Utils.getRandom(_randomColors.length - 1)];
					player.getAppearence().ce = true;
					player.getAppearence().cr =  randomGenerator.nextInt(255);
					player.getAppearence().cg = randomGenerator.nextInt(255);
					player.getAppearence().cb = randomGenerator.nextInt(255);
					player.getAppearence().ci = 60;
					player.getAppearence().ca = 0;
					player.getAppearence().generateAppearenceData();
				 
		          }
		         }
		        }, 0, 3);
		    return true;
			} else {
			player.sm("You must be an supreme Donator to use this command.");
			}
			return true;
				
			case "switchitemslook":
				player.switchItemsLook();
				player.getPackets().sendGameMessage("You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
				return true; 
				
			case "udid":
				player.getPackets().sendGameMessage(""+player.userid);
				return true;
				
			case "fly":
				if (player.isSupremeDonator() || player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isGraphicDesigner()) {
				player.setNextAnimation(new Animation(1914));
				player.setNextGraphics(new Graphics(92));
				player.getAppearence().setRenderEmote(1666);
				} else {
				player.sm("You must be an supreme Donator to use this command.");
				}
				return true;
				
			case "unlend":
				LendingManager.process();
				return true;
				
			case "land":
				player.getAppearence().setRenderEmote(-1);
				return true;
				
			case "makeover":
			case "looks":
			case "makeovermage":
			PlayerLook.openCharacterCustomizing(player);
			return true;
			
			
		
			}
			

		}
		
		return true;
	}


 public static void archiveLogs(Player player, String[] cmd) {
     try {
         if (player.getRights() <= 1) 
		 return;
         String location = "";
         if (player.getRights() == 7) {
             location = "data/playersaves/logs/commandlogs/owner/" + player.getUsername() + ".txt";
         } else if (player.getRights() == 2) {
             location = "data/playersaves/logs/commandlogs/admin/" + player.getUsername() + ".txt";
         } else if (player.getRights() == 1) {
             location = "data/playersaves/logs/commandlogs/mod/" + player.getUsername() + ".txt";
         } else if (player.isSupporter()) {
             location = "data/playersaves/logs/commandlogs/helper/" + player.getUsername() + ".txt";
         } else if (player.getRights() == 0) {
             location = "data/playersaves/logs/commandlogs/user/" + player.getUsername() + ".txt";
         } else if (player.isDonator()) {
             location = "data/playersaves/logs/commandlogs/donator/" + player.getUsername() + ".txt";
         }
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location,
					true));
			writer.write("[" + now("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::"
					+ cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/*
	 * doesnt let it be instanced
	 */
	private Commands() {

	}
}