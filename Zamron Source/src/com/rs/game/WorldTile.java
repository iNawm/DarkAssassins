package com.rs.game;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.utils.Utils;

public class WorldTile implements Serializable {

	private static final long serialVersionUID = -6567346497259686765L;

	private short x, y;
	private byte plane;
	
    public int getXInChunk() {
	return x & 0x7;
    }

    public int getYInChunk() {
	return y & 0x7;
    }

	public WorldTile(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}
	
	public String getLocation() {
		return "X - "+getX()+", Y - "+getY()+", P - "+getPlane();
	}

	public WorldTile(WorldTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}

	public WorldTile(WorldTile tile, int randomize) {
		this.x = (short) (tile.x + Utils.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Utils.getRandom(randomize * 2) - randomize);
		this.plane = tile.plane;
	}

	public WorldTile(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.plane = (byte) (hash >> 28);
	}

	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		plane += planeOffset;
	}

	public final void setLocation(WorldTile tile) {
		setLocation(tile.x, tile.y, tile.plane);
	}

	public final void setLocation(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public int getX() {
		return x;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getY() {
		return y;
	}

	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(WorldTile tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalY(WorldTile tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (Settings.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalX(WorldTile tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(WorldTile tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}

	public int getTileHash() {
		return y + (x << 14) + (plane << 28);
	}

	public boolean withinDistance(WorldTile tile, int distance) {
		if (tile.plane != plane)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance
				&& deltaY >= -distance;
	}

	public boolean withinDistance(WorldTile tile) {
		if (tile.plane != plane)
			return false;
		// int deltaX = tile.x - x, deltaY = tile.y - y;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;// deltaX
																		// <= 14
																		// &&
																		// deltaX
																		// >=
																		// -15
																		// &&
																		// deltaY
																		// <= 14
																		// &&
																		// deltaY
																		// >=
																		// -15;
	}

	
	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY,
			int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY,
			int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	public WorldTile transform(int x, int y, int plane) {
		return new WorldTile(this.x + x, this.y + y, this.plane + plane);
	}
	
	public void set(WorldTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}

	/**
	 * Checks if this world tile's coordinates match the other world tile.
	 * @param other The world tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(WorldTile other) {
		return x == other.x && y == other.y && plane == other.plane;
	}
	
    public boolean withinArea(int a, int b, int c, int d) {
	return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
    }

    public static WorldTile getLocationByName(String name) {
		if(name.equals("Edgevill"))
			 new WorldTile(3087, 3492, 0);
		if(name.equals("Lumbridge"))
			 new WorldTile(3222, 3219, 0);
		if(name.equals("Al-kharid"))
			 new WorldTile(3293, 3188, 0);
		if(name.equals("Varrock"))
			 new WorldTile(3211, 3423, 0);
		if(name.equals("Falador"))
			 new WorldTile(2965, 3379, 0);
		if(name.equals("Camelot"))
			 new WorldTile(2757, 3478, 0);
		if(name.equals("Ardounge"))
			 new WorldTile(2661, 3305, 0);
		if(name.equals("Watchtower"))
			 new WorldTile(2569, 3098, 0);
		if(name.equals("Trollheim"))
			 new WorldTile(2867, 3593, 0);
		if(name.equals("Ape Atoll"))
			 new WorldTile(2764, 2775, 0);
		if(name.equals("Canifas"))
			 new WorldTile(3052, 3497, 0);
		if(name.equals("Port Sarim"))
			 new WorldTile(3025, 3217, 0);
		if(name.equals("Rimmington"))
			 new WorldTile(2957, 3214, 0);
		if(name.equals("Draynor"))
			 new WorldTile(3093, 3244, 0);
		if(name.equals("IceQueen Lair"))
			 new WorldTile(2866, 9953, 0);
		if(name.equals("Brimhaven Dungeon"))
			 new WorldTile(2713, 9453, 0);
		if(name.equals("Gnome Agility"))
			 new WorldTile(2477, 3438, 0);
		if(name.equals("Wilderness Agility"))
			 new WorldTile(2998, 3932, 0);
		if(name.equals("Distant kingdom"))
			 new WorldTile(2767, 4723, 0);
		if(name.equals("Maze Event"))
			 new WorldTile(2911, 4551, 0);
		if(name.equals("Drill Instructor"))
			 new WorldTile(3163, 4828, 0);
		if(name.equals("Grave Digger"))
			 new WorldTile(1928, 5002, 0);
		if(name.equals("Karamja Lessers"))
			 new WorldTile(2835, 9563, 0);
		if(name.equals("Evil Bob's Island"))
			 new WorldTile(2525, 4776, 0);
		if(name.equals("Secret Island"))
			 new WorldTile(2152, 5095, 0);
		if(name.equals("Ibans Trap"))
			 new WorldTile(2319, 9804, 0);
		if(name.equals("Fishing Docks"))
			 new WorldTile(2767, 3277, 0);
		if(name.equals("Mage Trainging"))
			 new WorldTile(3365, 9640, 0);
		if(name.equals("Quest Place"))
			 new WorldTile(2907, 9712, 0);
		if(name.equals("Duel Arena"))
			 new WorldTile(3367, 3267, 0);
		if(name.equals("Bandit Camp"))
			 new WorldTile(3171, 3028, 0);
		if(name.equals("Uzer"))
			 new WorldTile(3484, 3092, 0);

		return null;
	}
}
