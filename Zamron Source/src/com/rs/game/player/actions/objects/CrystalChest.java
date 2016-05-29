package com.rs.game.player.actions.objects;

import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

/**
 * Represents the chest on which the key is used.
 * @author 'Corey 2010 <MobbyGFX96@hotmail.co.uk>
 */

public class CrystalChest {
	
	private static final int[] CHEST_REWARDS = { 2639, 2641, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671, 2673, 2675, 2643, 6857, 6859, 6861, 6863, 9470, 10400, 10402, 10404, 10406, 10408, 10410, 10412, 10414, 10416, 10418, 10420, 10422, 10424, 10426, 10426, 10428, 10430, 10432, 10434, 10436, 10438, 24154, 24155, 24154, 24155, 24154, 24155, 24154, 24155, 7462, 20072, };
	public static final int[] KEY_HALVES = { 985, 987 };
	public static final int KEY = 989;
	public static final int Animation = 881;
	
	/**
	 * Represents the key being made.
	 * Using tooth halves.
	 */
	public static void makeKey(Player p){
		if (p.getInventory().containsItem(toothHalf(), 1) 
				&& p.getInventory().containsItem(loopHalf(), 1)){
			p.getInventory().deleteItem(toothHalf(), 1);
			p.getInventory().deleteItem(loopHalf(), 1);
			p.getInventory().addItem(KEY, 1);
			p.sendMessage("You made a crystal key.");
		}
	}
	
	/**
	 * If the player can open the chest.
	 */
	public static boolean canOpen(Player p){
		if(p.getInventory().containsItem(KEY, 1)){
			return true;
		}else{
			p.sendMessage("This chest is locked.");
			return false;
		}
	}
	
	/**
	 * When the player searches the chest.
	 */
	public static void searchChest(final Player p){
		if (canOpen(p)){
			p.sendMessage("You unlock the chest with your key.");
			p.getInventory().deleteItem(KEY, 1);
			p.setNextAnimation(new Animation(Animation));
			p.getInventory().addItem(995, Utils.random(1000000));
			p.getInventory().addItem(CHEST_REWARDS[Utils.random(getLength() - 1)], 1);
			p.crystalChest++;
			p.sendMessage("You find some treasure in the chest.");
		}
	}
	
	public static int getLength() {
		return CHEST_REWARDS.length;
	}
	
	/**
	 * Represents the toothHalf of the key.
	 */
	public static int toothHalf(){
		return KEY_HALVES[0];
	}
	
	/**
	 * Represent the loop half of the key.
	 */
	public static int loopHalf(){
		return KEY_HALVES[1];
	}
	
}