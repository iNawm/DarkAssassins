package com.rs.game.player.content.custom.playershop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.rs.game.player.content.custom.ItemManager;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
//import com.rs.game.minigames.zombs.ZombieGame;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Shop;
//import com.rs.game.player.content.shops.DonatorShop;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

/**
 * 
 * @author xiles
 * 
 */

public class PlayerShop implements Serializable {

	private boolean locked;
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLock(boolean value) {
		this.locked = value;
	}
	
	public PlayerShop(Player player) {
		this.player = player;
	}

	/**
	 * Searches the list of online players for shop items
	 * @param searcher the person doing the search
	 * @param itemName - the item to search for
	 */
	public static void searchByName(Player searcher, String itemName) {
		if (itemName == null)
			return;
		searcher.lock();
		List<Player> allPlayers = new ArrayList<Player>();
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			allPlayers.add(player);
		}
		List<Object[]> sellers = new ArrayList<Object[]>();
		for (Player player : allPlayers) {
			if (player.getCustomisedShop() == null)
				continue;
			for (com.rs.game.CustomisedShop.MyShopItem item : player.getCustomisedShop().getShopItems()) {
				if (item == null
						|| item.getItem() == null
						|| item.getItem().getName() == null) 
					continue;
				if (item.getItem().getName().toLowerCase().contains(itemName.toLowerCase())) {
					sellers.add(new Object[] { player.getDisplayName(), item.getItem().getId(), item.getPrice() });
				}
			}
		}

		int interfaceId = 275;
		int startLine = 10;
		if (searcher != null) {
			for (int i = 0; i < 300; i++) {
				searcher.getPackets().sendIComponentText(interfaceId, i, "");
			}
			ListIterator<Object[]> it$ = sellers.listIterator();
			while(it$.hasNext()) {
				Object[] entry = it$.next();
				searcher.getPackets().sendIComponentText(interfaceId, startLine++, "" + entry[0] + "'s shop is selling: " + ItemDefinitions.getItemDefinitions((int) entry[1]).getName() + " for " + Utils.formatNumber((int) entry[2]) + " coins.");
			}
			searcher.getPackets().sendIComponentText(interfaceId, 1, "Searching for \"" + itemName + "\"");
			searcher.getInterfaceManager().sendInterface(interfaceId);
			searcher.unlock();
		}
	}

	/**
	 * sends the playes own shop with the correct item options
	 */
	public void sendOwnShop() {
		if (player.getControlerManager().getControler() != null) {
			player.sendMessage("You're not allowed to do that here.");
			return;
		}
		if (player.getSkills().getTotalLevel(player) < 400) {
			player.sendMessage("You need atleast a total level of 400 to use this feature.");
			return;
		}
		player.getPackets().sendItems(100, new Item[] { });
		int interfaceId = 1171;
		Item[] itemsArray = new Item[getShopItems().size() + 1];
		for (int i = 0; i < getShopItems().size(); i++) {
			itemsArray[i] = getShopItems().get(i).getItem();
		}
		player.getAttributes().put("editting_own_store", Boolean.TRUE);
		player.getAttributes().remove("viewing_player_shop");
		player.getPackets().sendItems(100, itemsArray);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0, 1, 2);
		player.getPackets().sendInterSetItemsOptionsScript(interfaceId, 7, 100, 8, 3, "Remove", "Original Value", "Examine", "Edit Price");
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Sell");
		player.getPackets().sendUnlockIComponentOptionSlots(interfaceId, 7, 0, 24, 0, 1, 2, 3);
		player.getPackets().sendIComponentText(interfaceId, 27, "Customise Your Store");
		player.getPackets().sendIComponentText(interfaceId, 41, "Refresh Store");
		player.getPackets().sendIComponentText(interfaceId, 4, isLocked() == true ? "<col=FF0000>Buying Locked</col>" : "<col=00FF00>Buying Unlocked</col>");

		if (!player.getInterfaceManager().containsInterface(interfaceId))
			player.getInterfaceManager().sendInterface(interfaceId);
	}

	/**
	 * Sends the shop items to the player opening your shop
	 * @Param opener The player opening your shop
	 */
	public void open(Player opener) {
		if (opener.getControlerManager().getControler() != null) {
			player.sendMessage("You're not allowed to do this here.");
			return;
		}
		if (isLocked()) {
			opener.sendMessage("This player currently has their shop locked.");
			return;
		}
		if (opener.getSkills().getTotalLevel() < 400) {
			player.sendMessage("You need atleast a total level of 400 to use this feature.");
			return;
		}
		int interfaceId = 1171;
		Item[] itemsArray = new Item[getShopItems().size() + 1];
		for (int i = 0; i < getShopItems().size(); i++) {
			itemsArray[i] = getShopItems().get(i).getItem();
		}
		opener.getPackets().sendItems(100, itemsArray);
		opener.getPackets().sendInterSetItemsOptionsScript(interfaceId, 7, 100, 8, 3, "Check Price", "Purchase", "Original Value");
		opener.getPackets().sendUnlockIComponentOptionSlots(interfaceId, 7, 0, 24, 0, 1, 2, 3);
		opener.getAttributes().put("viewing_player_shop", player);
		opener.getAttributes().replace("editting_own_store", Boolean.FALSE);
		opener.getPackets().sendIComponentText(interfaceId, 27, player.getDisplayName() + "'s Store");
		opener.getPackets().sendIComponentText(interfaceId, 41, "Refresh Store");
		if (!opener.getInterfaceManager().containsInterface(interfaceId))
			opener.getInterfaceManager().sendInterface(interfaceId);
	}

	/**
	 * Adds an item to your shop items
	 * @Param item The item to add
	 * @return
	 */
	public boolean addItem(Item itemToAdd) {
		if (itemToAdd == null) {
			return false;
		}
		
		MyShopItem item = new MyShopItem(itemToAdd, ItemManager.getPrice(itemToAdd.getId()));
		
		if (player.getControlerManager().getControler() != null) {
			player.sendMessage("You're not allowed to do this here.");
			return false;
		}
		if (player.getSkills().getTotalLevel() < 400) {
			player.sendMessage("You need atleast a total level of 400 to use this feature.");
			return false;
		}
		if (player.isTradeLocked()) {
			player.sendMessage("You are not allowed to use this while trade locked.");
			return false;
		}		
		if (itemToAdd.getDefinitions().isNoted()) {
			player.sendMessage("Noted items can not be sold.");
			return false;
		}
	//	for (int i : Shop.NotSellable){
	//		if (itemToAdd.getId() == i){
	//			player.sm("Donator items cannot be sold or trade.");
	//			return false;
	//		}
	//	}
		//if (DonatorShop.isDonorItem(itemToAdd.getId())) {
			//player.sendMessage(""+itemToAdd.getName()+" is a donator item and cannot be dropped or sold.");
			//return false;
		//}
		if (itemToAdd.getId() == 995 || !ItemConstants.isTradeable(itemToAdd)) {
			player.sendMessage("This item can not be sold.");
			return false;
		}
		
		if (getShopItems().size() >= 24) {
			player.sendMessage("You do not have any more room in your shop to add an item to it.");
			return false;
		}
		
		if (!getShopItems().contains(item)) {
			if (player.getInventory().containsItem(itemToAdd.getId(), itemToAdd.getAmount()) && getShopItems().add(item)) {
				player.getInventory().deleteItem(itemToAdd.getId(), itemToAdd.getAmount());
				player.sendMessage("You have added: "+itemToAdd.getName()+" which costs "+Utils.formatNumber(item.getPrice())+"gp");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes an item from the player's shop items array.
	 * @Param item The item to remove
	 * @return
	 */
	public boolean remove(Player buyer, MyShopItem item) {
		if (!getShopItems().contains(item)) {
			return false;
		}
		if (player.getControlerManager().getControler() != null) {
			player.sendMessage("You're not allowed to do this here.");
			return false;
		}
		int price = item.getPrice() * item.getItem().getAmount();
		if (price < 1) {
			buyer.sendMessage("An error occured while trying to purchase this item.");
			buyer.sendMessage("Please ask the seller to reduce the quantity of this item.");
			return false;
		}
		if (buyer != null) {
			if (buyer.getControlerManager().getControler() != null) {
				buyer.sendMessage("You're not allowed to do this here.");
				return false;
			}
			if (buyer.getSkills().getTotalLevel() < 400) {
				buyer.sendMessage("You need atleast a total level of 400 to use this feature.");
				return false;
			}
			if (buyer.isTradeLocked()) {
				buyer.sendMessage("You are not allowed to use this while trade locked.");
				return false;
			}
			if (!buyer.getInventory().containsItem(995, price)) {
				buyer.sendMessage("You do not have enough gold to purchase this item.");
				return false;
			}
			if (player.getCustomisedShop().isLocked()) {
				buyer.sendMessage("This player currently has their shop locked.");
				return false;
			}
		}
		
		boolean removed = getShopItems().remove(item);
		
		if (removed) {
			if (buyer == null) {
 				if (player.getInventory().hasFreeSlots()) {
 					player.getInventory().addItem(item.getItem().getId(), item.getItem().getAmount());
 					player.sendMessage("You have removed: "+item.getItem().getName()+" which costs "+Utils.formatNumber(item.getPrice())+"gp");
					return true;
				} else {
					player.sendMessage("You do not have enough free inventory slots.");
					return false;
				}
 			} else {
 				player.setVault(player.getVault() + price);
 				buyer.getInventory().deleteItem(995, price);
 				if (buyer.getInventory().hasFreeSlots()) {
 					buyer.getInventory().addItem(item.getItem());
 				} else {
 					buyer.getBank().addItem(item.getItem().getId(), item.getItem().getAmount(), true);
 				}
 				buyer.sendMessage(""+item.getItem().getName()+" has been bought for "+Utils.formatNumber(price)+" gp!");
 				if (World.containsPlayer(player.getUsername())) {
 					player.sendMessage("<col=AF00FF>" + item.getItem().getName() + " has been bought from your store! Money has been added to your vault.");
 				}
 				if (!World.containsPlayer(player.getUsername())) {
					SerializableFilesManager.savePlayer(player);
				}
 				return true;
 			}
			
		}
		return false;
	}
	
	/**
	 * Withdraws specific amount from the vault
	 * @param amount to withdraw
	 */
	public void withdrayFromVault(long amount) {
		try {
		int inInventory = player.getInventory().getNumerOf(995);
		player.getAttributes().remove("remove_money1");
		
		if (player.getVault() == 0) {
			player.sendMessage("You have no money in your vault to withdraw.");
			return;
		}
		
		if (amount < 1)
			amount = 1;
		
		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("Not enough free inventory space.");
			return;
		}
		
		if (amount > player.getVault())
			amount = player.getVault();
		
		if (inInventory + amount > Integer.MAX_VALUE)
			amount = Integer.MAX_VALUE - inInventory;
		
		player.setVault(player.getVault() - amount);
		player.getInventory().addItem(995, (int)amount);
		player.sendMessage("You have withdrawn "+Utils.formatNumber((int)amount)+" gp from your vault.");
		} catch (Exception e) {
			player.sendMessage("There was an error!");
		}
		return;
	}
	
	/**
	 * sets the items value in the players shop
	 * @param slot to edit
	 * @param value of he item
	 */
	public void setValue(int slot, int value) {
		try {
			if (getShopItems().get(slot) == null) {
				player.sendMessage("An error has occured!");
				return;
			}
			getShopItems().get(slot).setPrice(value);
			String itemName = getShopItems().get(slot).getItem().getName();
			int newValue = getShopItems().get(slot).getPrice();
			player.sendMessage("Value of "+itemName+" has been set to "+Utils.formatNumber(newValue)+" gp.");
		} catch (Exception e) {
			// nothing ._.
		}
	}
	
	/**
	 * Sends a message with the item's value
	 * @param item the item to get the rice of
	 */
	public void getValue(MyShopItem item) {
		String name = ItemDefinitions.getItemDefinitions(item.getItem().getId()).getName();
		player.sendMessage(""+name+" is worth "+Utils.formatNumber(item.getPrice())+" gp per unit.");
	}
	public void sendExamine(Player buyer, MyShopItem item) {
		String name = ItemDefinitions.getItemDefinitions(item.getItem().getId()).getName();
		buyer.sendMessage("Your "+name+" is on sale for "+Utils.formatNumber(item.getPrice())+" gp per unit.");
	}
	/**
	 * @return the shopItems
	 */
	public List<MyShopItem> getShopItems() {
		return shopItems;
	}

	/**
	 * The owner of the shop.
	 */
	private final Player player;

	/**
	 * The items in the shop.
	 */
	private final List<MyShopItem> shopItems = new ArrayList<MyShopItem>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 8858101569046698742L;


	/**
	 * The item with it's customised price.
	 * 
	 * @author Tyluur
	 * 
	 */
	public static class MyShopItem implements Serializable {

		private static final long serialVersionUID = 4578135011527893771L;

		public MyShopItem(Item item, int price) {
			this.item = item;
			this.price = price;
		}

		/**
		 * @return the price
		 */
		public int getPrice() {
			return price;
		}
		
		public void setPrice(int price) {
			this.price = price;
		}
		/**
		 * @return the item
		 */
		public Item getItem() {
			return item;
		}

		private final Item item;

		private int price;

	}

} 