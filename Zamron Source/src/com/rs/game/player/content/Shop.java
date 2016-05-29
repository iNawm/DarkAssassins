package com.rs.game.player.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.custom.ItemManager;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemSetsKeyGenerator;

public class Shop {

    private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

    private static final int MAX_SHOP_ITEMS = 40;
    public static final int COINS = 995, TOKKUL = 6529;
    public static final int TOKEN = 5250;
	
	 public static int loyaltyShop = 0;
	 
    
	public static int[][] loyaltyPrices = { 
	{ 20958, 5000 }, { 22268, 10000 }, { 20962, 5000 }, { 22270, 10000 }, { 20967, 5000 }, { 22272, 10000 }, 
	{ 22280, 10000 }, { 22282, 5000 }, { 22284, 10000 }, { 22286, 5000 }, { 20966, 10000 }, { 22274, 5000 }, 
	{ 22288, 10000 },{ 22290, 10000 },{ 20965, 10000 },{ 22276, 5000 },{ 22292, 10000 },{ 22294, 10000 },
	{ 22300, 5000 },{ 22296, 5000 },{ 22298, 10000 },{ 22302, 18000 }, { 22899, 5000 },{ 22901, 10000 }, 
	{ 22903, 10000 }, { 23876, 20000 },{ 22905, 5000 }, { 22907, 10000 }, { 22909, 10000 }, 
	{ 23874, 20000 },{ 23848, 5000 }, { 23850, 10000 }, { 23852, 10000 },{ 23854, 20000 }, };
	
	public static int pestinvshop = 72;
	public static int[][] pestinvPrices = { {28847,10}, {28872,10}, {28873,10}, {28874,10}, {28942,10}, {28944,10}, {28946,10} 
	};
	
	public static int triviashop = 65;
	public static int[][] triviaPrices = { {29131, 350}, {29132, 500}, {29133, 500}, {29134, 350}, {29135, 350}, {29136, 400}, {29137, 350}, 
{29149, 250}, {29139, 150}, {29144, 750}, {29145, 750}, {29146, 750}, {24428, 50}, {24427, 50}, 
{24429, 50}, {24430, 50}, {21485, 75}, {21484, 75}, {21487, 75}, {21486, 75}, {24100, 35}, 
{24102, 35}, {24104, 35}, {24106, 35}, {22552, 35}, {22554, 35}, {22556, 35}, {22558, 35}, 
{21258, 35}, {21260, 35}, {21262, 35}, {21264, 35}, {28960, 50}}; 
	
    public static int PVMShop = 19;
	public static int[][] PVMPrices = {
	{989, 300}, {28960, 1750}, {24455, 5500}, {24456, 5500}, {24457, 5500}, {28825, 3500}, {28935, 10000}, {28936, 10000}, {28937, 10000}, 
	{28938, 10000}, {28939, 10000}, {28940, 10000}, {28921, 55000}, {28922, 55000}, {28923, 55000}, {28924, 55000}, {28925, 55000}, 
	{28926, 55000}, {28927, 55000}, {28928, 55000}, {28929, 55000}, };
	
    public static int DonatorShop = 66;
    public static int DonatorShop2 = 67;
    public static int Donatorshop3 = 68;
	public static int[][] DonatorPrices = { 
		{1038, 2000}, {1040, 2000}, {1042, 2000}, {1044, 2000}, {1046, 2000}, {1048, 2000}, {1050, 2000},
		{29104, 1000}, {29093, 500}, {29092, 500}, {29091, 1500}, {29084, 1000}, {29078, 500}, {29064, 500}, {29065, 500}, {29066, 500},
		{29067, 500}, {29068, 500}, {29063, 1500}, {29062, 1500}, {29052, 2000}, {29052, 2000}, {29053, 2000}, {29054, 2000}, {29055, 2000},
		{29056, 2000}, {29057, 2000}, {29058, 2000}, {29059, 2000}, {29060, 2000}, {29032, 1000}, {29031, 1000}, {29011, 500}, {28995, 500},
		{28968, 1500}, {28967, 1500}, {28966, 1500}, {28965, 1500}, {28964, 1500}, {28963, 1500}, {28962, 1500}, {28961, 1500}, {28956, 1000},
		{28920, 1000}, {28919, 1000}, {28918, 1000 }, {28917, 1000}, {28916, 1000}, {28915, 1000}, {28882, 1500}, {28867, 500},
		
	};


    private String name;
    private Item[] mainStock;
    private int[] defaultQuantity;
    private Item[] generalStock;
    private int money;
    private CopyOnWriteArrayList<Player> viewingPlayers;

    public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
	viewingPlayers = new CopyOnWriteArrayList<Player>();
	this.name = name;
	this.money = money;
	this.mainStock = mainStock;
	defaultQuantity = new int[mainStock.length];
	for (int i = 0; i < defaultQuantity.length; i++)
	    defaultQuantity[i] = mainStock[i].getAmount();
	if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
	    generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
    }

    public boolean isGeneralStore() {
	return generalStock != null;
    }

    public void addPlayer(final Player player) {
	viewingPlayers.add(player);
	player.getTemporaryAttributtes().put("Shop", this);
	player.setCloseInterfacesEvent(new Runnable() {
	    @Override
	    public void run() {
		viewingPlayers.remove(player);
		player.getTemporaryAttributtes().remove("Shop");
		player.getTemporaryAttributtes().remove("shop_transaction");
		player.getTemporaryAttributtes().remove("isShopBuying");
		player.getTemporaryAttributtes().remove("ShopSelectedSlot");
		player.getTemporaryAttributtes().remove("ShopSelectedInventory");
	    }
	});
	player.refreshVerboseShopDisplayMode();
	player.getVarsManager().sendVar(118, generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY); 
	player.getVarsManager().sendVar(1496, -1); // sample items container id (TODO: add support for it)
	player.getVarsManager().sendVar(532, money);
	resetSelected(player);
	sendStore(player);
	player.getInterfaceManager().sendInterface(1265); // opens shop
	resetTransaction(player);
	setBuying(player, true);
	if (generalStock != null)
	    player.getPackets().sendHideIComponent(1265, 19, false); // unlocks general store icon
	player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize(), 1150); // unlocks stock slots
	sendInventory(player);
	player.getPackets().sendIComponentText(1265, 85, name);
    }

    public void resetTransaction(Player player) {
	setTransaction(player, 1);
    }

    public void increaseTransaction(Player player, int amount) {
	setTransaction(player, getTransaction(player) + amount);
    }

    public int getTransaction(Player player) {
	Integer transaction = (Integer) player.getTemporaryAttributtes().get("shop_transaction");
	return transaction == null ? 1 : transaction;
    }

    public void pay(Player player) {
	Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
	Boolean inventory = (Boolean) player.getTemporaryAttributtes().get("ShopSelectedInventory");
	if (selectedSlot == null || inventory == null)
	    return;
	int amount = getTransaction(player);
	if (inventory)
	    sell(player, selectedSlot, amount);
	else
	    buy(player, selectedSlot, amount);
    }

    public int getSelectedMaxAmount(Player player) {
	Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
	Boolean inventory = (Boolean) player.getTemporaryAttributtes().get("ShopSelectedInventory");
	if (selectedSlot == null || inventory == null)
	    return 1;
	if (inventory) {
	    Item item = player.getInventory().getItem(selectedSlot);
	    if (item == null)
		return 1;
	    return player.getInventory().getAmountOf(item.getId());
	} else {
	    if (selectedSlot >= getStoreSize())
		return 1;
	    Item item = selectedSlot >= mainStock.length ? generalStock[selectedSlot - mainStock.length] : mainStock[selectedSlot];
	    if (item == null)
		return 1;
	    return item.getAmount();
	}
    }

    public void setTransaction(Player player, int amount) {
	int max = getSelectedMaxAmount(player);
	if (amount > max)
	    amount = max;
	else if (amount < 1)
	    amount = 1;
	player.getTemporaryAttributtes().put("shop_transaction", amount);
	player.getVarsManager().sendVar(2564, amount);
    }

    public static void setBuying(Player player, boolean buying) {
	player.getTemporaryAttributtes().put("isShopBuying", buying);
	player.getVarsManager().sendVar(2565, buying ? 0 : 1);
    }

    public static boolean isBuying(Player player) {
	Boolean isBuying = (Boolean) player.getTemporaryAttributtes().get("isShopBuying");
	return isBuying != null && isBuying;
    }

    public void sendInventory(Player player) {
	player.getInterfaceManager().sendInventoryInterface(1266);
	player.getPackets().sendItems(93, player.getInventory().getItems());
	player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0, 1, 2, 3, 4, 5);
	player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
    }

    public void buyAll(Player player, int slotId) {
	if (slotId >= getStoreSize())
	    return;
	Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
	buy(player, slotId, item.getAmount());
    }

    public void buy(Player player, int slotId, int quantity) {
	if (slotId >= getStoreSize())
	    return;
	Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
	if (item == null)
	    return;
	if (item.getAmount() == 0) {
	    player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
	    return;
	}
	int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
	int price = getBuyPrice(item);
	if (price <= 0)
		return;
	int amountCoins = money == COINS ? player.getInventory().getCoinsAmount() : player.getInventory().getItems().getNumberOf(money);
	int maxQuantity = amountCoins / price;
	int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

	boolean enoughCoins = maxQuantity >= buyQ;
	if (money != 995) {
		for (int i11 = 0; i11 < loyaltyPrices.length; i11++) {
			loyaltyShop = 1;
		if (item.getId() == loyaltyPrices[i11][0]) {
			if (player.getLoyaltyPoints() < loyaltyPrices[i11][1] * quantity) {
			player.getPackets().sendGameMessage("You need " + loyaltyPrices[i11][1] + " Loyalty Points to buy this item!");
			return;
		} else
			loyaltyShop = 1;
			player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the loyalty store.");
			player.getInventory().addItem(loyaltyPrices[i11][0], 1);
			player.setLoyaltyPoints(player.getLoyaltyPoints() - loyaltyPrices[i11][1]);
			return;
		}
	}for (int i11 = 0; i11 < PVMPrices.length; i11++) {
		PVMShop = 19;
	if (item.getId() == PVMPrices[i11][0]) {
		if (player.getPvmPoints() < PVMPrices[i11][1] * quantity) {
		player.getPackets().sendGameMessage("You need " + PVMPrices[i11][1] + " PVM points to buy this item!");
		player.getPackets().sendGameMessage("You only have " + player.pvmPoints + " PVM points");
		return;
	} else
		PVMShop = 19;
		player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the PVM store.");
		player.getInventory().addItem(PVMPrices[i11][0], 1);
		player.setPvmPoints(player.getPvmPoints() - PVMPrices[i11][1]);
		return;
    }
	}for (int i11 = 0; i11 < pestinvPrices.length; i11++) {
		pestinvshop = 72;
	if (item.getId() == pestinvPrices[i11][0]) {
		if (player.getPestinvPoints() < pestinvPrices[i11][1] * quantity) {
		player.getPackets().sendGameMessage("You need " + pestinvPrices[i11][1] + " PestInvasion points to buy this item!");
		player.getPackets().sendGameMessage("You only have " + player.pestinvasionpoints + " PestInvasion points");
		return;
	} else
		PVMShop = 19;
		player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the PVM store.");
		player.getInventory().addItem(PVMPrices[i11][0], 1);
		player.setPvmPoints(player.getPvmPoints() - PVMPrices[i11][1]);
		return;
    }
}for (int i11 = 0; i11 < triviaPrices.length; i11++) {
		triviashop = 65;
	if (item.getId() == triviaPrices[i11][0]) {
		if (player.getTriviaPoints() < triviaPrices[i11][1] * quantity) {
		player.getPackets().sendGameMessage("You need " + triviaPrices[i11][1] + " Trivia Points to buy this item!");
		player.getPackets().sendGameMessage("You only have " + player.TriviaPoints + " Trivia points");
		return;
	} else
		triviashop = 65;
		player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Trivia store.");
		player.getInventory().addItem(triviaPrices[i11][0], 1);
		player.setTriviaPoints(player.getTriviaPoints() - triviaPrices[i11][1]);
		return;
    }
}
	}for (int i11 = 0; i11 < DonatorPrices.length; i11++) {
	    DonatorShop = 66;
	    DonatorShop2 = 67;
	    Donatorshop3 = 68;
	if (item.getId() == DonatorPrices[i11][0]) {
		if(player.getInventory().contains(5250) < DonatorPrices[i11][1] * quantity) {
		player.getPackets().sendGameMessage("You need " + DonatorPrices[i11][1] + " Donator Tokens to buy this item!");
		return;
	} else
	    DonatorShop = 66;
	    DonatorShop2 = 67;
	    Donatorshop3 = 68;
		player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Donator Store");
		player.getInventory().addItem(DonatorPrices[i11][0], 1);
		player.getInventory().removeItems(new Item(5250, DonatorPrices[i11][1]));
		return;
    }
}
	/*if (player.isIronMan && isGeneralStore()) {
		player.sm("IronMan Accs are not allowed to use the general store.");
		return;
	}*/
	
	if (!enoughCoins) {
	    player.getPackets().sendGameMessage("You don't have enough " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
	    buyQ = maxQuantity;
	} else if (quantity > buyQ)
	    player.getPackets().sendGameMessage("The shop has run out of stock.");
	if (item.getDefinitions().isStackable()) {
	    if (player.getInventory().getFreeSlots() < 1) {
		player.getPackets().sendGameMessage("Not enough space in your inventory.");
		return;
	    }
	} else {
	    int freeSlots = player.getInventory().getFreeSlots();
	    if (buyQ > freeSlots) {
		buyQ = freeSlots;
		player.getPackets().sendGameMessage("Not enough space in your inventory.");
	    }
	}
	if (buyQ != 0) {
	    int totalPrice = price * buyQ;
	    if (player.getInventory().removeItemMoneyPouch(new Item(money, totalPrice))) {
	    	player.shopLog(player, item.getId(), item.getAmount(), false);
		player.getInventory().addItem(item.getId(), buyQ);
		item.setAmount(item.getAmount() - buyQ);
		if (item.getAmount() <= 0 && slotId >= mainStock.length)
		    generalStock[slotId - mainStock.length] = null;
		refreshShop();
		resetSelected(player);
	    }
	}
    }

    public void restoreItems() {
	boolean needRefresh = true;
	for (int i = 0; i < mainStock.length; i++) {
	    if (mainStock[i].getAmount() < defaultQuantity[i]) {
		mainStock[i].setAmount(mainStock[i].getAmount() + 1);
		needRefresh = true;
	    } else if (mainStock[i].getAmount() > defaultQuantity[i]) {
		mainStock[i].setAmount(mainStock[i].getAmount() + -1);
		needRefresh = true;
	    }
	}
	if (generalStock != null) {
	    for (int i = 0; i < generalStock.length; i++) {
		Item item = generalStock[i];
		if (item == null)
		    continue;
		item.setAmount(item.getAmount() - 1);
		if (item.getAmount() <= 0)
		    generalStock[i] = null;
		needRefresh = false;
	    }
	}
	if (needRefresh)
	    refreshShop();
    }

    private boolean addItem(int itemId, int quantity) {
	for (Item item : mainStock) {
	    if (item.getId() == itemId) {
		item.setAmount(item.getAmount() + quantity);
		refreshShop();
		return true;
	    }
	}
	if (generalStock != null) {
	    for (Item item : generalStock) {
		if (item == null)
		    continue;
		if (item.getId() == itemId) {
		    item.setAmount(item.getAmount() + quantity);
		    refreshShop();
		    return true;
		}
	    }
	    for (int i = 0; i < generalStock.length; i++) {
		if (generalStock[i] == null) {
		    generalStock[i] = new Item(itemId, quantity);
		    refreshShop();
		    return true;
		}
	    }
	}
	return false;
    }

    public void sell(Player player, int slotId, int quantity) {
	if (player.getInventory().getItemsContainerSize() < slotId)
	    return;
	Item item = player.getInventory().getItem(slotId);
	if (item == null)
	    return;
	int originalId = item.getId();
	if (item.getDefinitions().isNoted() && item.getDefinitions().getCertId() != -1)
	    item = new Item(item.getDefinitions().getCertId(), item.getAmount());
	if (!ItemConstants.isTradeable(item) || item.getId() == money) {
	    player.getPackets().sendGameMessage("You can't sell this item.");
	    return;
	}
	int dq = getDefaultQuantity(item.getId());
	if (dq == -1 && generalStock == null) {
	    player.getPackets().sendGameMessage("You can't sell this item to this shop.");
	    return;
	}
	int price = getSellPrice(item);
	if (price <= 0)
		return;
	int numberOff = player.getInventory().getItems().getNumberOf(originalId);
	if (quantity > numberOff)
	    quantity = numberOff;
	if (!addItem(item.getId(), quantity) && !isGeneralStore()) {
		player.getPackets().sendGameMessage("Shop is currently full.");
		return;
	}
//	if (player.getRights()== 2) {
//		player.sm("Pkers are not allowed to use the general store.");
//		return;
//	}
	player.shopLog(player, item.getId(), item.getAmount(), true);
	player.getInventory().deleteItem(originalId, quantity);
	refreshShop();
	resetSelected(player);
	if (price == 0)
	    return;
	player.getInventory().addItemMoneyPouch(new Item(995, price * quantity));
    }

    public void sendValue(Player player, int slotId) {
	if (player.getInventory().getItemsContainerSize() < slotId)
	    return;
	Item item = player.getInventory().getItem(slotId);
	if (item == null)
	    return;
	if (item.getDefinitions().isNoted())
	    item = new Item(item.getDefinitions().getCertId(), item.getAmount());
	if (!ItemConstants.isTradeable(item) || item.getId() == money) {
	    player.getPackets().sendGameMessage("You can't sell this item.");
	    return;
	}
	int dq = getDefaultQuantity(item.getId());
	if (dq == -1 && generalStock == null) {
	    player.getPackets().sendGameMessage("You can't sell this item to this shop.");
	    return;
	}
	int price = getSellPrice(item);
	if (price <= 0)
		return;
	player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will buy for: " + price + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ". Right-click the item to sell.");
    }

    public int getDefaultQuantity(int itemId) {
	for (int i = 0; i < mainStock.length; i++)
	    if (mainStock[i].getId() == itemId)
		return defaultQuantity[i];
	return -1;
    }

    public void resetSelected(Player player) {
	player.getTemporaryAttributtes().remove("ShopSelectedSlot");
	player.getVarsManager().sendVar(2563, -1);
    }

    public void sendInfo(Player player, int slotId, boolean inventory) {
	if (!inventory && slotId >= getStoreSize())
	    return;
	Item item = inventory ? player.getInventory().getItem(slotId) : slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
	if (item == null)
	    return;
	if (item.getDefinitions().isNoted())
	    item = new Item(item.getDefinitions().getCertId(), item.getAmount());
	if (inventory && (!ItemConstants.isTradeable(item) || item.getId() == money)) {
	    player.getPackets().sendGameMessage("You can't sell this item.");
	    resetSelected(player);
	    return;
	}
	for (int i = 0; i < loyaltyPrices.length; i++) {
		if (item.getId() == loyaltyPrices[i][0]) {
			player.getPackets().sendGameMessage(
					"" + item.getDefinitions().getName() + " costs "
							+ loyaltyPrices[i][1] + " loyalty points.");
			player.getPackets().sendConfig(2564, loyaltyPrices[i][1]);
			return;
		}
	}
	for (int i = 0; i < PVMPrices.length; i++) {
		if (item.getId() == PVMPrices[i][0]) {
			player.getPackets().sendGameMessage(
					"" + item.getDefinitions().getName() + " costs "
							+ PVMPrices[i][1] + " PVM points.");
			player.getPackets().sendConfig(2564, PVMPrices[i][1]);
			return;
		}
	}for (int i = 0; i < triviaPrices.length; i++) {
		if (item.getId() == triviaPrices[i][0]) {
			player.getPackets().sendGameMessage(
					"" + item.getDefinitions().getName() + " costs "
							+ triviaPrices[i][1] + " Trivia points.");
			player.getPackets().sendConfig(2564, triviaPrices[i][1]);
			return;
		}
	}
	for (int i = 0; i < DonatorPrices.length; i++) {
		if (item.getId() == DonatorPrices[i][0]) {
			player.getPackets().sendGameMessage(
					"" + item.getDefinitions().getName() + " costs "
							+ DonatorPrices[i][1] + " Donator Tokens");
			player.getPackets().sendConfig(2564, DonatorPrices[i][1]);
			return;
		}
	}
	for (int i = 0; i < pestinvPrices.length; i++) {
		if (item.getId() == pestinvPrices[i][0]) {
			player.getPackets().sendGameMessage(
					"" + item.getDefinitions().getName() + " costs "
							+ pestinvPrices[i][1] + " PestInvasion points.");
			player.getPackets().sendConfig(2564, pestinvPrices[i][1]);
			return;
		}
	}
	resetTransaction(player);
	player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
	player.getTemporaryAttributtes().put("ShopSelectedInventory", inventory);
	player.getVarsManager().sendVar(2561, inventory ? 93 : generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY); // inv key
	player.getVarsManager().sendVar(2562, item.getId());
	player.getVarsManager().sendVar(2563, slotId);
	player.getPackets().sendGlobalString(362, ItemExamines.getExamine(item));
	player.getPackets().sendGlobalConfig(1876, item.getDefinitions().isWearItem() ? 0 : -1); // TODO item  pos or usage if has one, setting 0 to allow see stats
	int price = inventory ? getSellPrice(item) : getBuyPrice(item);
	player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will " + (inventory ? "buy" : "sell") + " for: " + price + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase());
    }
    

	public static int getSellPrice(Item item) {
	return (int) (getBuyPrice(item) / 1.2);
	}
    
    /* [clientscript]
     * int get_buy_price(int arg0) { 
	int ivar1; 
	if (arg0 == -1) { 
		return 0; 
	} 
	arg0 = getRealItem(arg0); 
	ivar1 = getDataByKey('o', 'i', 731, arg0); 
	if (standart_config_532 == 6529 && ivar1 != -1 && ivar1 > 0) { 
		return ivar1; 
	} 
	ivar1 = getDataByKey('o', 'i', 733, arg0); 
	if (ivar1 != -1 && ivar1 > 0) { 
		return ivar1; 
	} 
	if (getItemAttribute(arg0, 258) == 1 || getItemAttribute(arg0, 259) == 1) { 
		return 99000; 
	} 
	ivar1 = getItemValue(arg0); 
	if (standart_config_532 == 6529) { 
		ivar1 = multiplyDivide(3, 2, ivar1); 
	} 
	return max(ivar1, 1); 
      }
     */

	public static int getBuyPrice(Item item) {
	return ItemManager.getPrice(item.getId());
}

public static int getASPrice(Item item) {
	return ItemManager.getSellPrice(item.getId());
}

    public void sendExamine(Player player, int slotId) {
	if (slotId >= getStoreSize())
	    return;
	Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
	if (item == null)
	    return;
	player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
    }

    public void refreshShop() {
	for (Player player : viewingPlayers) {
	    sendStore(player);
	    player.getPackets().sendIComponentSettings(620, 25, 0, getStoreSize() * 6, 1150);
	}
    }

    public int getStoreSize() {
	return mainStock.length + (generalStock != null ? generalStock.length : 0);
    }

    public void sendStore(Player player) {
	Item[] stock = new Item[mainStock.length + (generalStock != null ? generalStock.length : 0)];
	System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
	if (generalStock != null)
	    System.arraycopy(generalStock, 0, stock, mainStock.length, generalStock.length);
	player.getPackets().sendItems(generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY, stock);
    }

}