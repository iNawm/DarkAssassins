package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

public class ObjectSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		//Thieving stalls @ home
		World.spawnObject(new WorldObject(4874, 10, 2, 3093, 3487, 0), true);
		World.spawnObject(new WorldObject(4875, 10, 2, 3094, 3487, 0), true);
		World.spawnObject(new WorldObject(4876, 10, 2, 3095, 3487, 0), true);
		World.spawnObject(new WorldObject(4877, 10, 2, 3096, 3487, 0), true);
		World.spawnObject(new WorldObject(4878, 10, 2, 3097, 3487, 0), true);
		
		//Altar @ home
		World.spawnObject(new WorldObject(409, 10, 1, 3082, 3490, 0), true); //done
		
		//house portal @ home
		World.spawnObject(new WorldObject(15482, 10, 0, 3084, 3484, 0), true); //Done
		
		//crystal chest @ home
		World.spawnObject(new WorldObject(2380, 10, 3, 3090, 3488, 0), true); //done
		
		//comp cape stand @ home
		World.spawnObject(new WorldObject(2563, 10, 1, 3090, 3499, 0), true); //done
		
		//portal starter
		World.spawnObject(new WorldObject(7288, 10, 2, 2592, 5601, 1), true);
		
		//xpWell
		World.spawnObject(new WorldObject(43096, 10, 2, 3165, 3455, 0), true);
		
		World.spawnObject(new WorldObject(14860, 10, 2, 3061, 3883, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3062, 3882, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3063, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3064, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3065, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3066, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3067, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3068, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3069, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3070, 3880, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3058, 3885, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3057, 3884, 0), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 3056, 3883, 0), true);
		
		//lz banks
		World.spawnObject(new WorldObject(782, 10, 2, 2833, 3866, 3), true);
		World.spawnObject(new WorldObject(782, 10, 2, 2832, 3866, 3), true);
		World.spawnObject(new WorldObject(782, 10, 2, 2831, 3866, 3), true);
		//lz magic trees
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3851, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3853, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3856, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3859, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3862, 3), true);
		World.spawnObject(new WorldObject(37823, 10, 2, 2824, 3865, 3), true);
		//lz rune rock
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3861, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3860, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3859, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3858, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3857, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3856, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3855, 3), true);
		World.spawnObject(new WorldObject(14860, 10, 2, 2841, 3854, 3), true);
		//lz furnace
		World.spawnObject(new WorldObject(6189, 10, 2, 2841, 3866, 3), true);
		//lz anvil
		World.spawnObject(new WorldObject(2782, 10, 2, 2841, 3864, 3), true);
		//lz stalls
		World.spawnObject(new WorldObject(4874, 10, -2, 2826, 3867, 3), true);
		World.spawnObject(new WorldObject(4875, 10, -2, 2827, 3867, 3), true);
		World.spawnObject(new WorldObject(4876, 10, -2, 2828, 3867, 3), true);
		World.spawnObject(new WorldObject(4877, 10, -2, 2829, 3867, 3), true);
		World.spawnObject(new WorldObject(4878, 10, -2, 2830, 3867, 3), true);
		//lz noticeboard
		World.spawnObject(new WorldObject(40760, 10, -2, 2832, 3858, 3), true);
		//lz rune ess
		World.spawnObject(new WorldObject(2491, 10, -2, 2834, 3867, 3), true);
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