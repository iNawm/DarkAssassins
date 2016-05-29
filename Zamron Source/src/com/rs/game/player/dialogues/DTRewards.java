package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.DominionTower;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class DTRewards extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", I'm Brian. I will trade in your Dominion Points for special rewards." );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("What would you like to say?", "Show me your Shop", "How maney DP do I have?", "How do I get Dominion Points?");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
		sendOptionsDialogue("What would you like to say?", "Goliath gloves (black)", "Goliath gloves (white)", "Goliath gloves (yellow)" , "Goliath gloves (red)", "More Options...");
		stage = 4;
		}
		if(componentId == OPTION_2) {
		    sendNPCDialogue(npcId, 9827, "You currently have " +player.dominionFactor + " Dominion Points." );
			stage = 3;
		}
        if(componentId == OPTION_3) {
		    sendNPCDialogue(npcId, 9827, "By killing the monsters in the Dominion Tower ofcourse! For each monster you will receive a 100 points. You can spend those at my shop." );
			stage = 3;
		}
		} else if (stage == 3) {
		end();
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				if (player.dominionFactor >= 50000) {
				player.getInventory().addItem(22358, 1);
				player.dominionFactor -= 50000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 50.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_2) {
				if (player.dominionFactor >= 50000) {
				player.getInventory().addItem(22359, 1);
				player.dominionFactor -= 50000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 50.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_3) {
				if (player.dominionFactor >= 50000) {
				player.getInventory().addItem(22360, 1);
				player.dominionFactor -= 50000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 50.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_4) {
				if (player.dominionFactor >= 50000) {
				player.getInventory().addItem(22361, 1);
				player.dominionFactor -= 50000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 50.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_5) {
				sendOptionsDialogue("What would you like to say?", "Swift gloves (black)", "Swift gloves (white)", "Swift gloves (yellow)" , "Swift gloves (red)", "More Options...");
				stage = 5;
				}
			} else if (stage == 5) {
			if (componentId == OPTION_1) {
				if (player.dominionFactor >= 26000) {
				player.getInventory().addItem(22362, 1);
				player.dominionFactor -= 26000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 26.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_2) {
				if (player.dominionFactor >= 26000) {
				player.getInventory().addItem(22363, 1);
				player.dominionFactor -= 26000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 26.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_3) {
				if (player.dominionFactor >= 26000) {
				player.getInventory().addItem(22364, 1);
				player.dominionFactor -= 26000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 26.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_4) {
				if (player.dominionFactor >= 26000) {
				player.getInventory().addItem(22365, 1);
				player.dominionFactor -= 26000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 26.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_5) {
				sendOptionsDialogue("What would you like to say?", "Spellcaster gloves (black)", "Spellcaster gloves (white)", "Spellcaster gloves (yellow)" , "Spellcaster gloves (red)", "More Options...");
				stage = 6;
				}
			} else if (stage == 6) {
			if (componentId == OPTION_1) {
				if (player.dominionFactor >= 20000) {
				player.getInventory().addItem(22366, 1);
				player.dominionFactor -= 20000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 20.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_2) {
				if (player.dominionFactor >= 20000) {
				player.getInventory().addItem(22368, 1);
				player.dominionFactor -= 20000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 20.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_3) {
				if (player.dominionFactor >= 20000) {
				player.getInventory().addItem(22368, 1);
				player.dominionFactor -= 20000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 20.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_4) {
				if (player.dominionFactor >= 20000) {
				player.getInventory().addItem(22369, 1);
				player.dominionFactor -= 20000;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 20.000 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_5) {
				sendOptionsDialogue("What would you like to say?", "Dominion sword", "Dominion crossbow", "Dominion staff" , "Back...");
				stage = 7;
				}
			} else if (stage == 7) {
			if (componentId == OPTION_1) {
				if (player.dominionFactor >= 52500) {
				player.getInventory().addItem(22346, 1);
				player.dominionFactor -= 52500;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 52.500 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_2) {
				if (player.dominionFactor >= 52500) {
				player.getInventory().addItem(22348, 1);
				player.dominionFactor -= 52500;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 52.500 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_3) {
				if (player.dominionFactor >= 52500) {
				player.getInventory().addItem(22347, 1);
				player.dominionFactor -= 52500;
				stage = 3;
				}else{
				sendNPCDialogue(npcId, 9827, "You need 52.500 Dominion Points to buy this item. " );
				stage = 3;
				}
				}
			if (componentId == OPTION_4) {
				sendOptionsDialogue("What would you like to say?", "Goliath gloves (black)", "Goliath gloves (white)", "Goliath gloves (yellow)" , "Goliath gloves (red)", "More Options...");
				stage = 4;
				}
	  }
	}

	@Override
	public void finish() {

	}

}