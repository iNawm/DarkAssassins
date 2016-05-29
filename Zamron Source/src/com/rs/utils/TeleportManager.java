package com.rs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import com.rs.game.WorldTile;

public class TeleportManager {

	private static HashMap<String, WorldTile> WorldTiles = new HashMap<String, WorldTile>();
	
	private static void add(String teleName, WorldTile tile) {
		if(WorldTiles == null) 
			WorldTiles = new HashMap<String, WorldTile>();
		WorldTiles.put(teleName, tile);
	}
	
	public static void addNewTeleport(String name, WorldTile tile) {
		if(name == null || tile == null)
			return;
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter("data/teleports.txt", true));
			file.write(name + " " + tile.getX() + " " + tile.getY() + " " + tile.getPlane());
			file.newLine();
			file.close();
			initiate();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static HashMap<String, WorldTile> getHashMap() {
		return WorldTiles;
	}
	public static WorldTile getLocation(String teleName) {
		return WorldTiles.get(teleName);
	}
	
	public static void initiate() {
		WorldTiles.clear();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/teleports.txt"));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("//"))
					continue;
				String[] text = line.split(" ");
				String name = text[0];
				int x = Integer.parseInt(text[1]);
				int y = Integer.parseInt(text[2]);
				int plane = Integer.parseInt(text[3]);
				WorldTile tile = new WorldTile(x, y, plane);
				add(name, tile);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		Logger.log("Launcher", WorldTiles.size() + " Teleports loaded succesfully..");
	}
}
