package com.rs.game.player.dialogues;

import java.util.ArrayList;

import com.rs.game.player.Skills;


public class StarterClass extends Dialogue {
	
	private static ArrayList<String> ips = new ArrayList<String>();

	@Override
	public void start() {
		sendOptionsDialogue("Picking Your Game Mode!",
				"IronMan",
				"Economy Account");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_5) {
				sendOptionsDialogue("Chose a Class!",
						"Pure",
						"Zerker",
						"Master",
						"Hybrid",
						"Tank");
				stage = 0;
                player.starterstage = 3;
				player.isIronMan = false;
                player.isPker = true;
			}if (componentId == OPTION_1) {	
				sendOptionsDialogue("Picking Your Game Mode!","Regular","Challenging","Difficult","Veteran");
				stage = 9;
				player.starterstage = 3;
				player.isIronMan = true;
                player.isPker = false;
			} else if (componentId == OPTION_2) {
				player.isPker = false;
				player.isIronMan = false;
			    String ip = player.getSession() != null ? player.getSession().getIP() : null;
			    if (ip != null && !ips.contains(ip)) {
				ips.add(ip);
				stage = 1;
				player.lock();
				sendOptionsDialogue("Picking Your Starter Class!",
						"Warrior",
						"Archer",
						"Sorcerer",
						"Skiller");
			    } else {
					if (!player.choseGameMode) {
						stage = 3;
					sendOptionsDialogue("Picking Your Game Mode!",
							"Regular",
							"Challenging",
							"Difficult",
							"Veteran");
					} else {
						player.starterstage = 3;
						player.getInterfaceManager().sendInterfaces();
						player.closeInterfaces();
						player.unlock();
						player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
						player.getHintIconsManager().addHintIcon(1348, 5199, 0, 100, 0, 0, -1, false);
					}
			    }
			}
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
                player.getInventory().addItem(9186, 1);//Rune crossbow
                player.getInventory().addItem(9244, 1000); //Dragon Bolt (e)
                player.getInventory().addItem(6586, 1);//amulet of fury
                player.getInventory().addItem(1950, 1);//Chef hat
                player.getInventory().addItem(545, 1);//Monk robe top
                player.getInventory().addItem(5699, 1);//Dragon dagger
                player.getInventory().addItem(4588, 1);//Dragon scimitar
                player.getInventory().addItem(2498, 1);//Black D’hide chaps
                player.getInventory().addItem(565, 10000);//Blood rune
                player.getInventory().addItem(555, 10000);//Water rune
                player.getInventory().addItem(560, 10000);//Death rune
                player.getInventory().addItem(6569, 1);//obsidian cape
                player.getInventory().addItem(15273, 1000);//1k rocktails
                player.getInventory().addItem(6686, 250);//Saradomin brew (4)
                player.getInventory().addItem(3025, 250);//Super restore (4)
                player.getInventory().addItem(2441, 250);//Super strenght pot(4)
                player.getInventory().addItem(2437, 250);//super attack pot (4)
                player.getInventory().addItem(6738, 1);//Breserker ring
                player.getInventory().addItem(2443, 250);//super defence pot (4)
                player.getInventory().addItemMoneyPouch(995, 10000000);//10M cash
                player.getInventory().addItem(6099, 1);//crystal
				
                 player.starterstage = 3;
			} else if (componentId == OPTION_2) {
                player.getInventory().addItem(6917, 1);//Infinity robe top
                player.getInventory().addItem(6925, 1);//Infinity robe bottom
                player.getInventory().addItem(6586, 1);//amulet of fury
                player.getInventory().addItem(4676, 1);//Ancient staff
                player.getInventory().addItem(1080, 1);//Rune Platelegs 
                player.getInventory().addItem(1128, 1);//Rune Platebody
                player.getInventory().addItem(4152, 1);//abyssal whip
                player.getInventory().addItem(3752, 1);//berserker helm
                player.getInventory().addItem(5699, 1);//Dragon dagger
                player.getInventory().addItem(2498, 1);//Black D'hide chaps
                player.getInventory().addItem(2504, 1);//Black D'hide body
                player.getInventory().addItem(555, 10000);//water rune
                player.getInventory().addItem(565, 10000);//blood rune
                player.getInventory().addItem(9075, 10000);//Astral rune
                player.getInventory().addItem(557, 1);// Earth rune                
                player.getInventory().addItem(560, 10000);//Death rune
                player.getInventory().addItem(6569, 1);//obsidian cape
                player.getInventory().addItem(15273, 1000);//1k rocktails
                player.getInventory().addItem(6686, 250);//Saradomin brew (4)
                player.getInventory().addItem(3025, 250);//Super restore (4)
                player.getInventory().addItem(2441, 250);//Super strenght pot(4)
                player.getInventory().addItem(2437, 250);//super attack pot (4)
                player.getInventory().addItem(6738, 1);//Breserker ring
                player.getInventory().addItem(2443, 250);//super defence pot (4)
                player.getInventory().addItemMoneyPouch(995, 10000000);//10M cash
                player.getInventory().addItem(6099, 1);//crystal 
                player.starterstage = 3;
			} else if (componentId == OPTION_3) {     
                player.getInventory().addItem(1080, 1);//Rune platelegs
                player.getInventory().addItem(1128, 1);//Rune platebody
                player.getInventory().addItem(10843, 1);//Helm of Nietiznot
                player.getInventory().addItem(6586, 1);//Amulet of fury
                player.getInventory().addItem(11733, 1);//Dragon boots
                player.getInventory().addItem(5699, 1);//Dragon dagger(p)
                player.getInventory().addItem(560, 5000);// Death rune
                player.getInventory().addItem(557, 10000);//Earth rune
                player.getInventory().addItem(9075, 5000);//Astral rune
                player.getInventory().addItem(6569, 1);//obsidian cape
                player.getInventory().addItem(15273, 1000);//1k rocktails
                player.getInventory().addItem(6686, 250);//Saradomin brew (4)
                player.getInventory().addItem(3025, 250);//Super restore (4)
                player.getInventory().addItem(2441, 250);//Super strenght pot(4)
                player.getInventory().addItem(2437, 250);//super attack pot (4)
                player.getInventory().addItem(6738, 1);//Breserker ring
                player.getInventory().addItem(2443, 250);//super defence pot (4)
                player.getInventory().addItemMoneyPouch(995, 10000000);//10M cash
                player.getInventory().addItem(6099, 1);//crystal 
                player.starterstage = 3;
			} else if (componentId == OPTION_4) {
                player.getInventory().addItem(4102, 1);//Mystic robe top
                player.getInventory().addItem(4104, 1);//Mystic robe bottom
                player.getInventory().addItem(6586, 1);//amulet of fury
                player.getInventory().addItem(4676, 1);//ancient staff
                player.getInventory().addItem(1080, 1);//Rune Platelegs 
                player.getInventory().addItem(1128, 1);//Rune Platebody
                player.getInventory().addItem(10843, 1);//Helm of neitzinot
                player.getInventory().addItem(5699, 1);//Dragon dagger
                player.getInventory().addItem(2504, 1);//Black D’hide body
                player.getInventory().addItem(2498, 1);//Black D’hide chaps
                player.getInventory().addItem(565, 10000);//Blood rune
                player.getInventory().addItem(555, 10000);//Water rune
                player.getInventory().addItem(560, 10000);//Death rune
                player.getInventory().addItem(6569, 1);//obsidian cape
                player.getInventory().addItem(15273, 1000);//1k rocktails
                player.getInventory().addItem(6686, 250);//Saradomin brew (4)
                player.getInventory().addItem(3025, 250);//Super restore (4)
                player.getInventory().addItem(2441, 250);//Super strenght pot(4)
                player.getInventory().addItem(2437, 250);//super attack pot (4)
                player.getInventory().addItem(6738, 1);//Breserker ring
                player.getInventory().addItem(2443, 250);//super defence pot (4)
                player.getInventory().addItemMoneyPouch(995, 10000000);//10M cash
                player.getInventory().addItem(6099, 1);//crystal 
                player.starterstage = 3;
			} else if (componentId == OPTION_5) {
                player.getInventory().addItem(1164, 1);//rune full helmet
                player.getInventory().addItem(3134, 1);//granite shield
                player.getInventory().addItem(6586, 1);//amulet of fury
                player.getInventory().addItem(9186, 1);//rune crossbow
                player.getInventory().addItem(1080, 1);//Rune Platelegs 
                player.getInventory().addItem(9244, 1000);//Dragon bolt (e)
                player.getInventory().addItem(2504, 1);//Black D’hide body
                player.getInventory().addItem(9075, 10000);//Astral rune
                player.getInventory().addItem(557, 1);// Earth rune                
                player.getInventory().addItem(560, 10000);//Death rune
                player.getInventory().addItem(6569, 1);//obsidian cape
                player.getInventory().addItem(15273, 1000);//1k rocktails
                player.getInventory().addItem(6686, 250);//Saradomin brew (4)
                player.getInventory().addItem(3025, 250);//Super restore (4)
                player.getInventory().addItem(2441, 250);//Super strenght pot(4)
                player.getInventory().addItem(2437, 250);//super attack pot (4)
                player.getInventory().addItem(6738, 1);//Breserker ring
                player.getInventory().addItem(2443, 250);//super defence pot (4)
                player.getInventory().addItemMoneyPouch(995, 10000000);//10M cash
                player.getInventory().addItem(6099, 1);//crystal 
                player.starterstage = 3;
			}
			player.getInterfaceManager().sendInterfaces();
			player.closeInterfaces();
			player.unlock();
			player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
			player.getHintIconsManager().addHintIcon(1348, 5199, 0, 100, 0, 0, -1, false);
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
                 player.getInventory().addItem(11814, 1);//Bronze Armour Set
                 player.getInventory().addItem(11834, 1);//addy Armour Set
                 player.getInventory().addItem(6568, 1);//Obby Cape
                 player.getInventory().addItem(1725, 1);//Amulet of strength
                 player.getInventory().addItem(1321, 1);//Bronze Scimitar
                 player.getInventory().addItem(1333, 1);//Rune Scimitar
                 player.getInventory().addItem(4587, 1);//Dragon Scimitar
                 player.getInventory().addItem(386, 250);//Shark
                 player.getInventory().addItem(15273, 100);//Rocktail
                 player.getInventory().addItem(2435, 15);//Prayer Potions
                 player.getInventory().addItem(2429, 1);//Attack Potions
                 player.getInventory().addItem(114, 1);//strength Potions
                 player.getInventory().addItem(2433, 1);//Defence Potions
                 player.getInventory().addItemMoneyPouch(995, 10000000);//2.5m cash
                 player.getInventory().addItem(6099, 1);//Crystal
                 player.starterstage = 3;
			} else if (componentId == OPTION_2) {
				player.getInventory().addItem(11864, 1);//Green Dhide Set
				player.getInventory().addItem(26777, 1);//Ava's Attractor
				player.getInventory().addItem(1478, 1);//Amulet of Accuracy
                player.getInventory().addItem(1061, 1);//Leather boots
                player.getInventory().addItem(1129, 1);//leather Body
                player.getInventory().addItem(1095, 1);//Leather Chaps
                player.getInventory().addItem(1063, 1);//Leather vambs
                player.getInventory().addItem(841, 1);//Shortbow
                player.getInventory().addItem(861, 1);//Magic Shortbow
                player.getInventory().addItem(882, 500);//bronze arrows
                player.getInventory().addItem(386, 250);//Shark
                player.getInventory().addItem(15273, 100);//Rocktail
                player.getInventory().addItem(2435, 15);//Prayer Potions
                player.getInventory().addItem(2445, 1);//Range Potions
                player.getInventory().addItem(2433, 1);//Defence Potions  
                player.getInventory().addItemMoneyPouch(995, 10000000);//cash
                player.getInventory().addItem(6099, 1);//Crystal
                player.starterstage = 3;
			} else if (componentId == OPTION_3) {
                player.getInventory().addItem(11872, 1);//Mystic Set
                player.getInventory().addItem(2412, 1);//Sardomin Cape
                player.getInventory().addItem(1727, 1);//Amulet of Magic
                player.getInventory().addItem(577, 1);//Wizard Robe top
                player.getInventory().addItem(1011, 1);//Wizard skirt
                player.getInventory().addItem(579, 1);//Wizard Hat
                player.getInventory().addItem(1389, 1);//Staff
                player.getInventory().addItem(556, 500);//Air Runes
                player.getInventory().addItem(555, 500);//Water Runes
                player.getInventory().addItem(554, 500);//Fire runes
                player.getInventory().addItem(557, 500);//Earth Runes
                player.getInventory().addItem(558, 500);//Mind Runes
                player.getInventory().addItem(562, 500);//Chaos Runes
                player.getInventory().addItem(386, 250);//Shark
                player.getInventory().addItem(15273, 100);//Rocktail
                player.getInventory().addItem(2435, 15);//Prayer Potions
                player.getInventory().addItem(3041, 1);//Magic Potions
                player.getInventory().addItem(2433, 1);//Defence Potions
                player.getInventory().addItemMoneyPouch(995, 10000000);//cash
                player.getInventory().addItem(6099, 1);//Crystal
                player.starterstage = 3;
			} else if (componentId == OPTION_4) {
                player.getInventory().addItem(1949, 1);//Chef's Hat
                player.getInventory().addItem(6180, 1);//Lederhosen Top
                player.getInventory().addItem(6181, 1);//Lederhosen Shorts
                player.getInventory().addItem(6182, 1);//Lederhosen Hat
                player.getInventory().addItem(1351, 1);//bronze axe
                player.getInventory().addItem(1265, 1);//bronze Pickaxe
                player.getInventory().addItem(303, 1);//Fishing net
                player.getInventory().addItem(10006, 1);//Bird snare
                player.getInventory().addItem(2347, 1);//Hammer
                player.getInventory().addItem(590, 1);//Tinderbox
                player.getInventory().addItem(1733, 1);//Needle
                player.getInventory().addItem(1734, 100);//Thread
                player.getInventory().addItem(946, 1);//Knife
                player.getInventory().addItem(1755, 1);//Chisel
                player.getInventory().addItem(233, 1);//Pestle and mortar
                player.getInventory().addItem(1512, 50);//Logs
                player.getInventory().addItem(2350, 50);//Bronze Bar
                player.getInventory().addItem(228, 50);//Vial of water
                player.getInventory().addItem(1626, 50);//Opals
                player.getInventory().addItemMoneyPouch(995, 10000000);//cash
                player.getInventory().addItem(6099, 1);//Crystal
                player.starterstage = 3;
			}
			if (!player.choseGameMode) {
				stage = 3;
					sendOptionsDialogue("Picking Your Game Mode!",
							"Regular",
							"Challenging",
							"Difficult",
							"Veteran");
			} else {
				player.starterstage = 3;
				player.getInterfaceManager().sendInterfaces();
				player.closeInterfaces();
				player.unlock();
				player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
				player.getHintIconsManager().addHintIcon(1348, 5199, 0, 100, 0, 0,
						-1, false);
			}
		}		else if (stage == 2) {
		
			
			player.getInterfaceManager().sendInterfaces();
			player.closeInterfaces();
			player.unlock();
			player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
			player.getHintIconsManager().addHintIcon(1348, 5199, 0, 100, 0, 0,
					-1, false);
	} else if (stage == 3) {
		if (componentId == OPTION_1) {
			player.gameMode = 0;
			player.sm("You have selected the normal gamemode you will get rsps rates and lower drop chances");
		} else if (componentId == OPTION_2) {
			player.gameMode = 1;
			player.sm("You have selected the challenging gamemode, xp will be slightly lower and you will have a higher drop rate than regulars");
		} else if (componentId == OPTION_3) {
			player.gameMode = 2;
			player.sm("You have choosen difficult gamemode, xp will be lower and you will have a higher drop chance");
		} else if (componentId == OPTION_4) {
			player.gameMode = 3;
			player.sm("You are now playing as veteran xp rates will be on the lowest option and a higher drop rate goodluck!");
		}
		player.starterstage = 3;
		player.getInterfaceManager().sendInterfaces();
		player.closeInterfaces();
		player.unlock();
		player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
		player.getHintIconsManager().addHintIcon(1348, 5199, 0, 100, 0, 0,-1, false);
		}else if (stage == 9) {
			if (componentId == OPTION_1) {
				player.gameMode = 0;
				player.sm("You have selected the normal gamemode you will get rsps rates and lower drop chances");
				player.getInventory().addItem(1351, 1);//bronze axe
				player.getInventory().addItem(590, 1);//Tinderbox
				player.getInventory().addItem(303, 1);//Fishing net
				player.getInventory().addItem(315, 1);//Tinderbox
				player.getInventory().addItem(946, 1);//Knife
                player.getInventory().addItem(1755, 1);//Chisel
                player.getInventory().addItem(1265, 1);//bronze Pickaxe
                player.getInventory().addItem(2347, 1);//Hammer
                player.getInventory().addItem(1205, 1);//bronze dagger
				player.getInventory().addItem(1291, 1);//bronze longsword
				player.getInventory().addItem(1171, 1);//wooden shield
				player.getInventory().addItem(839, 1);//longbow
				player.getInventory().addItem(882, 25);//bronze arrows
				player.getInventory().addItem(556, 50);//Air
				player.getInventory().addItem(558, 50);//Mind		
                player.getInventory().addItem(10006, 1);//Bird snare
				player.getInventory().addItem(228, 50);//Vial of water		
				player.isIronMan = true;		
				player.starterstage = 3;
			} else if (componentId == OPTION_2) {
				player.gameMode = 1;
				player.sm("You have selected the challenging gamemode, xp will be slightly lower and you will have a higher drop rate than regulars");
				player.getInventory().addItem(1351, 1);//bronze axe
				player.getInventory().addItem(590, 1);//Tinderbox
				player.getInventory().addItem(303, 1);//Fishing net
				player.getInventory().addItem(315, 1);//Tinderbox
				player.getInventory().addItem(946, 1);//Knife
                player.getInventory().addItem(1755, 1);//Chisel
                player.getInventory().addItem(1265, 1);//bronze Pickaxe
                player.getInventory().addItem(2347, 1);//Hammer
                player.getInventory().addItem(1205, 1);//bronze dagger
				player.getInventory().addItem(1291, 1);//bronze longsword
				player.getInventory().addItem(1171, 1);//wooden shield
				player.getInventory().addItem(839, 1);//longbow
				player.getInventory().addItem(882, 25);//bronze arrows
				player.getInventory().addItem(556, 50);//Air
				player.getInventory().addItem(558, 50);//Mind		
                player.getInventory().addItem(10006, 1);//Bird snare
				player.getInventory().addItem(228, 50);//Vial of water		
				player.isIronMan = true;		
				player.starterstage = 3;
			} else if (componentId == OPTION_3) {
				player.gameMode = 2;
				player.sm("You have choosen difficult gamemode, xp will be lower and you will have a higher drop chance");
				player.getInventory().addItem(1351, 1);//bronze axe
				player.getInventory().addItem(590, 1);//Tinderbox
				player.getInventory().addItem(303, 1);//Fishing net
				player.getInventory().addItem(315, 1);//Tinderbox
				player.getInventory().addItem(946, 1);//Knife
                player.getInventory().addItem(1755, 1);//Chisel
                player.getInventory().addItem(1265, 1);//bronze Pickaxe
                player.getInventory().addItem(2347, 1);//Hammer
                player.getInventory().addItem(1205, 1);//bronze dagger
				player.getInventory().addItem(1291, 1);//bronze longsword
				player.getInventory().addItem(1171, 1);//wooden shield
				player.getInventory().addItem(839, 1);//longbow
				player.getInventory().addItem(882, 25);//bronze arrows
				player.getInventory().addItem(556, 50);//Air
				player.getInventory().addItem(558, 50);//Mind		
                player.getInventory().addItem(10006, 1);//Bird snare
				player.getInventory().addItem(228, 50);//Vial of water		
				player.isIronMan = true;		
				player.starterstage = 3;
			} else if (componentId == OPTION_4) {
				player.gameMode = 3;
				player.sm("You are now playing as veteran xp rates will be on the lowest option and a higher drop rate goodluck!");
				player.getInventory().addItem(1351, 1);//bronze axe
				player.getInventory().addItem(590, 1);//Tinderbox
				player.getInventory().addItem(303, 1);//Fishing net
				player.getInventory().addItem(315, 1);//Tinderbox
				player.getInventory().addItem(946, 1);//Knife
                player.getInventory().addItem(1755, 1);//Chisel
                player.getInventory().addItem(1265, 1);//bronze Pickaxe
                player.getInventory().addItem(2347, 1);//Hammer
                player.getInventory().addItem(1205, 1);//bronze dagger
				player.getInventory().addItem(1291, 1);//bronze longsword
				player.getInventory().addItem(1171, 1);//wooden shield
				player.getInventory().addItem(839, 1);//longbow
				player.getInventory().addItem(882, 25);//bronze arrows
				player.getInventory().addItem(556, 50);//Air
				player.getInventory().addItem(558, 50);//Mind		
                player.getInventory().addItem(10006, 1);//Bird snare
				player.getInventory().addItem(228, 50);//Vial of water		
				player.isIronMan = true;		
				player.starterstage = 3;
			}
			player.starterstage = 3;
			player.getInterfaceManager().sendInterfaces();
			player.closeInterfaces();
			player.unlock();
			player.getPackets().sendGameMessage("You may now enter the realm of Zamron through this portal");
			
		}
	}

	@Override
	public void finish() {

	}
}
