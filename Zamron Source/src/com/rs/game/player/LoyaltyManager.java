package com.rs.game.player;

import java.util.TimerTask;

import com.rs.cores.CoresManager;

public class LoyaltyManager {

	private static final int INTERFACE_ID = 1143;
	private transient Player player;

	public LoyaltyManager(Player player) {
		this.player = player;
	}

	public void openLoyaltyStore(Player player) {
		player.getPackets().sendWindowsPane(INTERFACE_ID, 0);
	}

	public void startTimer() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			int timer = 1800;

			@Override
			public void run() {
				if (timer == 1) {
					if (player.isSupremeDonator() ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 1250);
						player.getPackets().sendGameMessage("<col=008000>You have recieved 1250 Loyalty Tokens for playing for 30 minutes!");
					}else if (player.isEliteDonator()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 1000);
						player.getPackets().sendGameMessage("<col=008000>You have recieved 1000 Loyalty Tokens for playing for 30 minutes!");
					} else if (player.isExtremeDonator()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 750);
						player.getPackets().sendGameMessage("<col=008000>You have recieved 750 Loyalty Tokens for playing for 30 minutes!");
					} else if (player.isDonator()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 500);
						player.getPackets().sendGameMessage("<col=008000>You have recieved 500 Loyalty Tokens for playing for 30 minutes!");
					} else {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 250);
						player.getPackets().sendGameMessage("<col=008000>You have recieved 250 Loyalty Tokens for playing for 30 minutes!");
					}
					timer = 1800;
						}
				if (timer > 0) {
					timer--;
				}
			}
		}, 0L, 1000L);
	}
}