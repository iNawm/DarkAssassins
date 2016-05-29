package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		
		//Start of NEW HOME------- OBJECT DELETION-----
	World.deleteObject(new WorldTile(1497, 5221, 0));
	World.deleteObject(new WorldTile(1496, 5222, 0));
	World.deleteObject(new WorldTile(1496, 5221, 0));
	World.deleteObject(new WorldTile(1495, 5220, 0));
	World.deleteObject(new WorldTile(1495, 5221, 0));
	World.deleteObject(new WorldTile(1495, 5222, 0));
	World.deleteObject(new WorldTile(1495, 5223, 0));
	World.deleteObject(new WorldTile(1494, 5223, 0));
	World.deleteObject(new WorldTile(1494, 5222, 0));
	World.deleteObject(new WorldTile(1494, 5221, 0));
	World.deleteObject(new WorldTile(1494, 5220, 0));
	World.deleteObject(new WorldTile(1493, 5221, 0));
	World.deleteObject(new WorldTile(1493, 5222, 0));
	World.deleteObject(new WorldTile(1493, 5223, 0));
	World.deleteObject(new WorldTile(1492, 5224, 0));
	World.deleteObject(new WorldTile(1492, 5223, 0));
	World.deleteObject(new WorldTile(1492, 5222, 0));
	World.deleteObject(new WorldTile(1494, 5230, 0));
	World.deleteObject(new WorldTile(1493, 5230, 0));
	World.deleteObject(new WorldTile(1493, 5229, 0));
	World.deleteObject(new WorldTile(1493, 5228, 0));
	World.deleteObject(new WorldTile(1494, 5228, 0));
	World.deleteObject(new WorldTile(1494, 5229, 0));
	World.deleteObject(new WorldTile(1495, 5228, 0));
	World.deleteObject(new WorldTile(1494, 5227, 0));
	World.deleteObject(new WorldTile(1492, 5228, 0));
	World.deleteObject(new WorldTile(1492, 5229, 0));
	World.deleteObject(new WorldTile(1491, 5229, 0));
	World.deleteObject(new WorldTile(1490, 5230, 0));
	World.deleteObject(new WorldTile(1490, 5229, 0));
	World.deleteObject(new WorldTile(1490, 5228, 0));
	World.deleteObject(new WorldTile(1490, 5227, 0));
	World.deleteObject(new WorldTile(1490, 5226, 0));
	World.deleteObject(new WorldTile(1489, 5228, 0));
	World.deleteObject(new WorldTile(1489, 5229, 0));
	World.deleteObject(new WorldTile(1489, 5227, 0));
	World.deleteObject(new WorldTile(1488, 5228, 0));
	World.deleteObject(new WorldTile(1492, 5221, 0));
	World.deleteObject(new WorldTile(1493, 5224, 0));	
	World.deleteObject(new WorldTile(1502, 5221, 0));
	World.deleteObject(new WorldTile(1503, 5221, 0));
	World.deleteObject(new WorldTile(1504, 5221, 0));
	World.deleteObject(new WorldTile(1504, 5222, 0));
	World.deleteObject(new WorldTile(1503, 5222, 0));
	World.deleteObject(new WorldTile(1502, 5222, 0));
	World.deleteObject(new WorldTile(1503, 5223, 0));
	World.deleteObject(new WorldTile(1504, 5223, 0));
	World.deleteObject(new WorldTile(1504, 5224, 0));
	World.deleteObject(new WorldTile(1503, 5222, 0));
	World.deleteObject(new WorldTile(1500, 5223, 0));
	World.deleteObject(new WorldTile(1501, 5224, 0));
	World.deleteObject(new WorldTile(1499, 5224, 0));
	World.deleteObject(new WorldTile(1500, 5224, 0));
	World.deleteObject(new WorldTile(1500, 5225, 0));
	World.deleteObject(new WorldTile(1499, 5225, 0));
	World.deleteObject(new WorldTile(1499, 5226, 0));
	World.deleteObject(new WorldTile(1500, 5226, 0));
	World.deleteObject(new WorldTile(1498, 5226, 0));
	World.deleteObject(new WorldTile(1498, 5225, 0));
	World.deleteObject(new WorldTile(1504, 5226, 0));
	World.deleteObject(new WorldTile(1503, 5226, 0));
	World.deleteObject(new WorldTile(1503, 5227, 0));
	World.deleteObject(new WorldTile(1504, 5227, 0));
	World.deleteObject(new WorldTile(1506, 5228, 0));
	World.deleteObject(new WorldTile(1505, 5229, 0));
	World.deleteObject(new WorldTile(1506, 5229, 0));
	World.deleteObject(new WorldTile(1507, 5229, 0));
	World.deleteObject(new WorldTile(1508, 5229, 0));
	World.deleteObject(new WorldTile(1508, 5230, 0));
	World.deleteObject(new WorldTile(1507, 5230, 0));
	World.deleteObject(new WorldTile(1506, 5230, 0));
	World.deleteObject(new WorldTile(1507, 5231, 0));
	World.deleteObject(new WorldTile(1508, 5231, 0));
	World.deleteObject(new WorldTile(1501, 5230, 0));
	World.deleteObject(new WorldTile(1502, 5230, 0));
	World.deleteObject(new WorldTile(1500, 5230, 0));
	World.deleteObject(new WorldTile(1500, 5231, 0));
	World.deleteObject(new WorldTile(1501, 5231, 0));
	World.deleteObject(new WorldTile(1502, 5231, 0));
	World.deleteObject(new WorldTile(1502, 5232, 0));
	World.deleteObject(new WorldTile(1501, 5232, 0));
	World.deleteObject(new WorldTile(1503, 5232, 0));
	World.deleteObject(new WorldTile(1502, 5233, 0));
	World.deleteObject(new WorldTile(1501, 5233, 0));
	World.deleteObject(new WorldTile(1501, 5229, 0));
	World.deleteObject(new WorldTile(1503, 5224, 0));
	World.deleteObject(new WorldTile(1506, 5222, 0));
	World.deleteObject(new WorldTile(1507, 5222, 0));
	World.deleteObject(new WorldTile(1507, 5223, 0));
	World.deleteObject(new WorldTile(1507, 5221, 0));
	World.deleteObject(new WorldTile(1517, 5200, 0));
	World.deleteObject(new WorldTile(1517, 5197, 0));
	World.deleteObject(new WorldTile(1516, 5194, 0));
	World.deleteObject(new WorldTile(1521, 5204, 0));
	World.deleteObject(new WorldTile(1522, 5199, 0));
	World.deleteObject(new WorldTile(1525, 5193, 0));
	World.deleteObject(new WorldTile(1520, 5192, 0));
	World.deleteObject(new WorldTile(1527, 5197, 0));
	World.deleteObject(new WorldTile(1526, 5204, 0));
	World.deleteObject(new WorldTile(1531, 5199, 0));
	World.deleteObject(new WorldTile(1519, 5209, 0));
	World.deleteObject(new WorldTile(1522, 5208, 0));
	World.deleteObject(new WorldTile(1521, 5205, 0));
	World.deleteObject(new WorldTile(1522, 5204, 0));
	World.deleteObject(new WorldTile(1522, 5200, 0));
	World.deleteObject(new WorldTile(1521, 5195, 0));
	World.deleteObject(new WorldTile(1524, 5195, 0));
	World.deleteObject(new WorldTile(1525, 5192, 0));
	World.deleteObject(new WorldTile(1526, 5193, 0));
	World.deleteObject(new WorldTile(1524, 5191, 0));
	World.deleteObject(new WorldTile(1528, 5197, 0));
	World.deleteObject(new WorldTile(1526, 5200, 0));
	World.deleteObject(new WorldTile(1526, 5201, 0));
	World.deleteObject(new WorldTile(1527, 5200, 0));
	World.deleteObject(new WorldTile(1532, 5201, 0));
	World.deleteObject(new WorldTile(1527, 5203, 0));
	World.deleteObject(new WorldTile(1527, 5199, 0));
	World.deleteObject(new WorldTile(1481, 5197, 0));
	World.deleteObject(new WorldTile(1481, 5196, 0));
	World.deleteObject(new WorldTile(1482, 5196, 0));
	World.deleteObject(new WorldTile(1482, 5197, 0));
	World.deleteObject(new WorldTile(1482, 5200, 0));
	World.deleteObject(new WorldTile(1482, 5201, 0));
	World.deleteObject(new WorldTile(1481, 5201, 0));
	World.deleteObject(new WorldTile(1483, 5203, 0));
	World.deleteObject(new WorldTile(1483, 5204, 0));
	World.deleteObject(new WorldTile(1482, 5204, 0));
	World.deleteObject(new WorldTile(1482, 5203, 0));
	World.deleteObject(new WorldTile(1479, 5201, 0));
	World.deleteObject(new WorldTile(1478, 5201, 0));
	World.deleteObject(new WorldTile(1478, 5202, 0));
	World.deleteObject(new WorldTile(1479, 5202, 0));
	World.deleteObject(new WorldTile(1480, 5199, 0));
	World.deleteObject(new WorldTile(1479, 5199, 0));
	World.deleteObject(new WorldTile(1476, 5199, 0));
	World.deleteObject(new WorldTile(1475, 5199, 0));
	World.deleteObject(new WorldTile(1475, 5198, 0));
	World.deleteObject(new WorldTile(1476, 5198, 0));
	World.deleteObject(new WorldTile(1477, 5205, 0));
	World.deleteObject(new WorldTile(1476, 5205, 0));
	World.deleteObject(new WorldTile(1476, 5206, 0));
	World.deleteObject(new WorldTile(1477, 5206, 0));
	
	World.deleteObject(new WorldTile(1500, 5194, 0));
	World.deleteObject(new WorldTile(1499, 5194, 0));
	World.deleteObject(new WorldTile(1498, 5194, 0));
		
		
		
		
		
		//removed objects
		World.deleteObject(new WorldTile(3164, 3475, 0));
		World.deleteObject(new WorldTile(1499, 5201, 0));
		World.deleteObject(new WorldTile(1498, 5202, 0));

		//lz bankers
		World.spawnNPC(44, new WorldTile(2833, 3867, 3), 0, false);
		World.spawnNPC(44, new WorldTile(2832, 3867, 3), 0, false);
		World.spawnNPC(44, new WorldTile(2831, 3867, 3), 0, false);
		//lz shops
		World.spawnNPC(11571, new WorldTile(2834, 3850, 3), -4, false);
		World.spawnNPC(11577, new WorldTile(2833, 3850, 3), -4, false);
		//dz shop
		World.spawnNPC(1285, new WorldTile(2583, 3910, 0), -4, false);
		//tokens @ dice
		World.spawnNPC(15533, new WorldTile(4384, 5919, 0), -4, false);
		
		World.spawnNPC(11506, new WorldTile(2592, 5605, 1), -4, false);
	
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/spawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}