package com.rs.game.player.actions.CitadelActions;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

public class CitadelGrabFish extends Action {

	@Override
	public boolean start(Player player) {
		if(checkAll(player)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	private boolean checkAll(Player player) {
		if(!player.getInventory().hasFreeSlots())
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		player.setNextAnimation(new Animation(881));
		player.getInventory().addItemMoneyPouch(new Item(3703));
		player.getPackets().sendGameMessage("You take a fish off the counter, you should cook it next.");
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
