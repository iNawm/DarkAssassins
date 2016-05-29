package com.rs.game.npc;

public class Drop {
	
	public enum Rarity {
		ALWAYS,
		COMMON,
		UNCOMMON,
		RARE,
		VERYRARE,
		ULTRARARE
	};
	
	private Rarity rarity;
	private int itemId;
	private int minAmount;
	private int maxAmount;
	
	public Drop(Rarity rarity, int itemId, int minAmount, int maxAmount) {
		this.rarity = rarity;
		this.itemId = itemId;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public int getItemId() {
		return itemId;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

}
