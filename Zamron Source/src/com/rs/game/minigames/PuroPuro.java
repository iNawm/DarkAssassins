package com.rs.game.minigames;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import com.rs.Launcher;
import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.GodWarsBosses;
import com.rs.game.minigames.ZarosGodwars;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.RequestController;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.corp.CorporealBeast;
import com.rs.game.npc.dragons.KingBlackDragon;
import com.rs.game.npc.godwars.GodWarMinion;
import com.rs.game.npc.godwars.armadyl.GodwarsArmadylFaction;
import com.rs.game.npc.godwars.armadyl.KreeArra;
import com.rs.game.npc.godwars.bandos.GeneralGraardor;
import com.rs.game.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.game.npc.godwars.saradomin.CommanderZilyana;
import com.rs.game.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.game.npc.godwars.zammorak.GodwarsZammorakFaction;
import com.rs.game.npc.godwars.zammorak.KrilTstsaroth;
import com.rs.game.npc.godwars.zaros.Nex;
import com.rs.game.npc.godwars.zaros.NexMinion;
import com.rs.game.npc.kalph.KalphiteQueen;
import com.rs.game.npc.nomad.FlameVortex;
import com.rs.game.npc.nomad.Nomad;
import com.rs.game.npc.others.Bork;
import com.rs.game.npc.others.ItemHunterNPC;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.npc.others.Lucien;
import com.rs.game.npc.others.MasterOfFear;
import com.rs.game.npc.others.MercenaryMage;
import com.rs.game.npc.others.PestMonsters;
import com.rs.game.npc.others.Revenant;
import com.rs.game.npc.others.TormentedDemon;
import com.rs.game.npc.slayer.GanodermicBeast;
import com.rs.game.npc.slayer.Strykewyrm;
import com.rs.game.npc.sorgar.Elemental;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.BoxAction.HunterNPC;
import com.rs.game.player.content.Hunter;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.LivingRockCavern;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.TriviaBot;
import com.rs.game.player.content.Hunter.FlyingEntities;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.AntiFlood;
import com.rs.utils.IPBanL;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;





	public class PuroPuro extends Controler {

	    private static final Item[][] REQUIRED = { { new Item(11238, 3), new Item(11240, 2), new Item(11242, 1) }, { new Item(11242, 3), new Item(11244, 2), new Item(11246, 1) }, { new Item(11246, 3), new Item(11248, 2), new Item(11250, 1) }, { null } };

	    private static final Item[] REWARD = { new Item(11262, 1), new Item(11259, 1), new Item(11258, 1), new Item(11260, 3) };

	    public void start() {
		player.getPackets().sendBlackOut(2);
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 8, 169);
		initPuroImplings();
	    }

	    public void forceClose() {
		player.getPackets().sendBlackOut(0);
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 8);
	    }

	    public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	    }

	    public boolean logout() {
		return false;
	    }

	    public boolean login() {
		start();
		return false;
	    }

	    public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		    case 25014:
			player.getControlerManager().forceStop();
			Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new WorldTile(2427, 4446, 0), 9, false, Magic.OBJECT_TELEPORT);
			return true;
		}
		return true;
	    }
	    public static void enterPuro(final Player player){
	    	   WorldTasksManager.schedule(new WorldTask() {

	   			public void run() {
	   			    player.getControlerManager().startControler("PuroPuro");
	   			}
	   		    }, 10);
	   		    Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new WorldTile(2591, 4320, 0), 9, false, Magic.OBJECT_TELEPORT);
	    }
	    
	    
	    
	    static boolean depositedNet(Player player){
	    	if (player.getBank().getAmountOf(11259) >= 1)
	    		return true;
	    	 else 
	    		 return false;
	    }
	    static boolean depositedRepellent(Player player){
	    	if (player.getBank().getAmountOf(11262) >= 1)
	    		return true;
	    	 else 
	    		 return false;
	    }
	    static boolean full(Player player){
	    	if (player.getBank().getAmountOf(11260) >= 127) 
	    		return true;
	    	else
	    		return false;
	    }
	  
	    public static void handleButtonsShop(Player player, int componentId, int packetId){
	    	if (componentId == 5) {
	    		   if (player.getBank().getAmountOf(11259) > 0) {
	    		    player.getBank().withdrawItem(11259, 1);
	    		   } else {
	    		    player.sm("You don't have a Magic butterfly net stored");
	    		   }
	    		  }
	    	if (componentId == 12){
	    		if (player.getInventory().getFreeSlots() > 1 && player.getBank().getAmountOf(11262) >= 1){
		    		player.getBank().withdrawItem(11262, 1);
	    		} else {
	    			player.sm("You don't have enough space to withdraw the Imp repellent.");
	    		}
	    		if (player.getBank().getAmountOf(11262) < 1){
	    			player.sm("You don't have an Imp repellent stored.");
	    		}
	    	}
	    	if (componentId == 3){
	    		if (player.getBank().getAmountOf(11260) < 0)
	    			player.sm("You don't have a impling jar stored.");
	   
	    			
	    		if (packetId ==  WorldPacketsDecoder.ACTION_BUTTON1_PACKET){
	    			player.getBank().withdrawItem(11260, 1);
	    		}
	    		if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET){
	    			player.getBank().withdrawItem(11260, 5);
	    		}
	    		if (packetId == 5){
	    			if (player.getBank().getAmountOf(11260) > 0){
	    				player.getBank().withdrawItem(11260, player.getInventory().getFreeSlots());
	    			} else {
	    				player.sm("You don't have any impling jar stored.");
	    			}
	    		}
	    		if (packetId == 55){
	    			player.getPackets().sendRunScript(108,new Object[] { "How many would you like to deposit?" });
	    			player.getTemporaryAttributtes().put("remove_X_money", 11260);
	    			   player.getTemporaryAttributtes().put("Puro_Deposit", Boolean.TRUE);
		    		}
	    		
	    		}
	    	if (componentId == 18){
	    		
	    		if (player.getInventory().getNumerOf(11260) > 1 && player.getInventory().getNumerOf(11260) + player.getBank().getAmountOf(11260) > 127 && player.getBank().getAmountOf(11260) < 127){
	    			player.getBank().deposit(new Item(11260, player.getBank().getAmountOf(11260) == 127 ? 0 :127 - player.getBank().getAmountOf(11260)));
	    		} else if(player.getInventory().getNumerOf(11260) + player.getBank().getAmountOf(11260) < 127) {
	    			player.getBank().deposit(new Item (11260,player.getInventory().getNumerOf(11260)));
	    		} else if (player.getInventory().getNumerOf(11260) + player.getBank().getAmountOf(11260) > 127 && player.getBank().getAmountOf(11260) >= 127){
	    			player.sm("You can't store more than 127 Imp jars.");
	    		}
	    		if (player.getInventory().getNumerOf(11262) >= 1 && depositedRepellent(player) == false){
	    			player.getBank().deposit(new Item(11262,1));
	    		} else  if (player.getInventory().containsItem(11262, 1) && player.getBank().getAmountOf(11262) >= 1){
	    			player.sm("You can't store more than 1 Imp repellant.");
	    		}
	    		if (player.getInventory().getNumerOf(11259) >= 1 && depositedNet(player)==false){
	    			player.getBank().deposit(new Item(11259,1));
	    			player.getInterfaceManager().sendInterface(592);
	    		} else  if (player.getInventory().containsItem(11259, 1) && player.getBank().getAmountOf(11259) >= 1){
	    			player.sm("You can't store more than 1 Magic butterfly net.");
	    			}
	    		player.getBank().removeItem(player.getBank().getItemSlot(11260), 1, true, false); // fix the bug for adding 1 jar too much
	    	}
	    	if (componentId == 19){
	    				if (player.getBank().getAmountOf(11259) >= 1)
	    		    	player.getBank().withdrawItem(11259, 1);
	    				if (player.getBank().getAmountOf(11262) >= 1)
	    		    	player.getBank().withdrawItem(11262, 1);
	    				if (player.getBank().getAmountOf(11260) >= 1)
	    		    	player.getBank().withdrawItem(11260, player.getBank().getAmountOf(11260) > player.getInventory().getFreeSlots() ? player.getInventory().getFreeSlots() : player.getBank().getAmountOf(11260)) ;
	    		    	if (player.getBank().getAmountOf(11259) < 1)
	    		    		player.sm("You don't have a Magic Butterfly net stored.");
	    		    	if (player.getBank().getAmountOf(11260) < 1)
	    		    		player.sm("You don't have an Imp jar stored.");
	    		    	if (player.getBank().getAmountOf(11262) < 1)
	    		    		player.sm("You don't have an Imp repellent stored.");
	    		    	
	    	}    	
			player.getInterfaceManager().sendInterfaces();
			 //sendShop(player);
	    }
	    
	    
	    public static void sendShop(Player player){
			   player.getInterfaceManager().sendInterface(592);
			   player.getPackets().sendIComponentText(592, 4, depositedNet(player) ? "<col=00FF00>Magic butterfly net (1/1)" : "Magic butterfly net (0/1)");
			   player.getPackets().sendIComponentText(592, 11, depositedRepellent(player) ? "<col=00FF00>Imp repellent       (1/1)" : "Imp repellent           (0/1)");
			   player.getPackets().sendIComponentText(592, 2, player.getBank().getAmountOf(11260) == 0 ? "Impling jar      (0/127)" : full(player) ?"<col=00FF00>Impling jar     (127/127)":  "<col=00FF00>Impling jar         ("+player.getBank().getAmountOf(11260)+"/127)");
		   }
	    
	    public static void pushThrough(final Player player, WorldObject object){
		int objectX = object.getX();
		int objectY = object.getY();
		int direction = 0;
		if (player.getX() == objectX && player.getY() < objectY) {
		    objectY = objectY + 1;
		    direction = ForceMovement.NORTH;
		} else if (player.getX() == objectX && player.getY() > objectY) {
		    objectY = objectY - 1;
		    direction = ForceMovement.SOUTH;
		} else if (player.getY() == objectY && player.getX() < objectX) {
		    objectX = objectX + 1;
		    direction = ForceMovement.EAST;
		} else if (player.getY() == objectY && player.getX() > objectX) {
		    objectX = objectX - 1;
		    direction = ForceMovement.WEST;
		} else if (player.getX() != objectX && player.getY() > objectY) {
			objectX = object.getX();
			objectY = objectY - 1;			
			 direction = ForceMovement.SOUTH;
		} else if (player.getX() != objectX && player.getY() < objectY) {
			objectX = object.getX();
			 objectY = objectY + 1;
			    direction = ForceMovement.NORTH;
		} else if (player.getY() != objectY && player.getX() < objectX) {
			objectY = object.getY();
		    objectX = objectX + 1;
		    direction = ForceMovement.EAST;
		} else if (player.getY() == objectY && player.getX() > objectX) {
			objectY = object.getY();
		    objectX = objectX - 1;
		    direction = ForceMovement.WEST;
		}
		player.setNextFaceWorldTile(object);
		player.getPackets().sendGameMessage(Utils.getRandom(2) == 0 ? "You use your strength to push through the wheat in the most efficient fashion." : "You use your strength to push through the wheat.");
		player.setNextFaceWorldTile(object);
		player.setNextAnimation(new Animation(6594));
		player.lock();
		final WorldTile tile = new WorldTile(objectX, objectY, 0);
		player.setNextFaceWorldTile(object);
		player.setNextForceMovement(new ForceMovement(tile, 6, direction));
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			player.unlock();
			player.setNextWorldTile(tile);
		    }
		}, 6);
	    
	    }
	     public static void initPuroImplings() {
		for (int i = 0; i < 5; i++) {
		    for (int index = 0; index < 11; index++) {
			if (i > 2) {
			    if (Utils.getRandom(1) == 0)
				continue;
			}
			World.spawnNPC(FlyingEntities.values()[index].getNpcId(), new WorldTile(Utils.random(2558 + 3, 2626 - 3), Utils.random(4285 + 3, 4354 - 3), 0), -1, false);
		    }
		}
	    }

	    public static void openPuroInterface(final Player player) {
		player.getInterfaceManager().sendInterface(540); // puro puro
		for (int component = 50; component < 75; component++)
		    player.getPackets().sendHideIComponent(540, component, false);
		player.setCloseInterfacesEvent(new Runnable() {

		    @Override
		    public void run() {
			player.getTemporaryAttributtes().remove("puro_slot");
		    }
		});
	    }
	    
	    public static void handleButtons(Player player, int componentId){
	    	if (componentId >= 20 && componentId <= 26){
	    		handlePuroInterface(player,componentId);
	    		confirmPuroSelection(player);
	    	} else if (componentId == 69){
	    		player.closeInterfaces();
	    	} else if (componentId == 71){
	    		player.closeInterfaces();
	    		sendShop(player);
	    	}
	    }
	 
	   
	    public static void handlePuroInterface(Player player, int componentId) {
		player.getTemporaryAttributtes().put("puro_slot", (componentId - 20) / 2);
	    }

	    public static void confirmPuroSelection(Player player) {
		if (player.getTemporaryAttributtes().get("puro_slot") == null)
		    return;
		int slot = (int) player.getTemporaryAttributtes().get("puro_slot");
		Item exchangedItem = REWARD[slot];
		Item[] requriedItems = REQUIRED[slot];
		if (slot == 3) {
		    requriedItems = null;
		    for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null || FlyingEntities.forItem((short) item.getId()) == null)
			    continue;
			requriedItems = new Item[] { item };
		    }
		}
		if (requriedItems == null || !player.getInventory().containsItems(requriedItems)) {
		    player.getPackets().sendGameMessage("You don't have the required items.");
		    return;
		}
		if (player.getInventory().addItem(exchangedItem.getId(), exchangedItem.getAmount())) {
		    player.getInventory().removeItems(requriedItems);
		    player.closeInterfaces();
		    player.getPackets().sendGameMessage("You exchange the required items for: " + exchangedItem.getName().toLowerCase() + ".");
		}
	    }
	}
