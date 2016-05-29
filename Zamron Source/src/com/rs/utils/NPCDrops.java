package com.rs.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.rs.game.npc.Drop;
import com.rs.game.npc.Drop.Rarity;

public class NPCDrops {

	private final static String PACKED_PATH = "data/npcs/npcDrops.txt";
	private static HashMap<Integer, Drop[]> npcDrops;
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public static final void init() {
		loadPackedNPCDrops();
	}

	public static ArrayList<Drop> getDrops(int npcId) {
		ArrayList<Drop> drops = new ArrayList<Drop>();
		if (npcDrops.get(npcId) != null) {
			for (Drop drop : npcDrops.get(npcId)) {
				drops.add(drop);
			}
		}
		return drops;
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {
		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}
		return dropMapx;
	}

	private static void loadPackedNPCDrops() {
		try {
			npcDrops = new HashMap<Integer, Drop[]>();
			Path path = Paths.get(PACKED_PATH);
			try (Scanner scanner = new Scanner(path, ENCODING.name())) {
				ArrayList<Drop> drops = new ArrayList<Drop>();
				int npcId = -1;
				int lineNumber = 0;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					lineNumber++;
					if (line.isEmpty() && !drops.isEmpty() && npcId != -1) {
						Drop[] npcDraps = new Drop[drops.size()];
						npcDrops.put(npcId, drops.toArray(npcDraps));
						drops.clear();
						npcId = -1;
					} else {
						String[] subs = line.split(":");
						String[] info = subs[1].split("-");
								
						Rarity rarity = null;
						int itemId = 0;
						int minAmount = 0;
						int maxAmount = 0;

						npcId = Integer.parseInt(subs[0]);
						itemId = Integer.parseInt(info[0]);
						switch(info[1]) {
							case "ALWAYS":
								rarity = Rarity.ALWAYS;
								break;
							case "COMMON":
								rarity = Rarity.COMMON;
								break;
							case "UNCOMMON":
								rarity = Rarity.UNCOMMON;
								break;
							case "RARE":
								rarity = Rarity.RARE;
								break;
							case "VERYRARE":
								rarity = Rarity.VERYRARE;
								break;
							case "ULTRARARE":
								rarity = Rarity.ULTRARARE;
								break;
						}
						minAmount = Integer.parseInt(info[2]);
						maxAmount = Integer.parseInt(info[3]);
						
						if (minAmount > maxAmount) {
							int temp = minAmount;
							minAmount = maxAmount;
							maxAmount = temp;
						}
						
						if (npcId == -1 || itemId == 0 || minAmount == 0 || maxAmount == 0 || rarity == null) {
							System.out.println("INVALID NPC DROP ON LINE: "+lineNumber);
							return;
						}
						
						drops.add(new Drop(rarity, itemId, minAmount, maxAmount));
					}
				}
				System.out.println("Parsed "+lineNumber+" lines of NPC drops.");
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}
}