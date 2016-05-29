package com.rs.game.minigames.fog;

import com.rs.game.WorldTile;

/**
 * Represents necessities of the game.
 * 
 * @author Jae <jae@xiduth.com>
 * @since Nov 28, 2013
 */
public class FistOfGuthixConfiguration {
	public static WorldTile LOBBY_LOCATION;
	public static WorldTile HALL_LOCATION;
	public static WorldTile GAME_LOCATION;	
public FistOfGuthixConfiguration(){
	LOBBY_LOCATION = new WorldTile(1642,5600, 0);
	GAME_LOCATION = new WorldTile(1663,5695, 0);
	HALL_LOCATION = new WorldTile(1675,5599, 0);
}
}
