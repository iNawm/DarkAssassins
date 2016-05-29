package com.zamron.myql;

import java.sql.ResultSet;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Donation implements Runnable {

	public static final String HOST_ADDRESS = "";
	public static final String USERNAME = "zamronn1_donate";
	public static final String PASSWORD = "";
	public static final String DATABASE = "zamronn1_donate";
	
	private Player player;
	
	@Override
	public void run() {
		try {
			Database db = new Database(HOST_ADDRESS, USERNAME, PASSWORD, DATABASE);
			
			if (!db.init()) {
				System.err.println("[Donation] Failed to connect to database!");
				return;
			}
			
			String name = player.getUsername().replace("_", " ");
			ResultSet rs = db.executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND claimed=0");
			
			while(rs.next()) {
				String item_name = rs.getString("item_name");
				int item_number = rs.getInt("item_number");
				double amount = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				
				ResultSet result = db.executeQuery("SELECT * FROM products WHERE item_id="+item_number+" LIMIT 1");
				
				if (result == null || !result.next()
						|| !result.getString("item_name").equalsIgnoreCase(item_name)
						|| result.getDouble("item_price") != amount
						|| quantity < 1 || quantity > Integer.MAX_VALUE) {
					System.out.println("[Donation] Invalid purchase for "+name+" (item: "+item_name+", id: "+item_number+")");
					continue;
				}
				
				handleItems(item_number);
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleItems(int productId) {
		switch(productId) {
		case 0:
        	player.getAppearence().setTitle(1348);
			player.setDonator(true);
			player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your donation! Thank you for your support!");
			World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
			break;
		case 1:
			player.getAppearence().setTitle(1349);
			player.setExtremeDonator(true);
			player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your donation! Thank you for your support!");
			World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername()) + "<shad=7401DF> has just Donated!</shad>", false);
			break;
			
		}
	}
	
	public Donation(Player player) {
		this.player = player;
	}
}
