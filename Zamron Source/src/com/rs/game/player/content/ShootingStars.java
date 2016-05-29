package com.rs.game.player.content;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class ShootingStars {

	private enum StarSpawns {
		VARROCK(new WorldTile(3257, 3407, 0)), RIMMINGTON(new WorldTile(2974,
				3238, 0)), DRAYNOR(new WorldTile(3092, 3259, 0)), DOMINION_TOWER(
				new WorldTile(3380, 3081, 0)), WILDERNESS_VOLCANO(
				new WorldTile(3173, 3690, 0)), ROCK_CRABS(new WorldTile(2706,
				3713, 0)), YANILLE(new WorldTile(2522, 3104, 0)), DUEL_ARENA(
				new WorldTile(3338, 3269, 0)), FALADOR(new WorldTile(3031,
				3348, 0)), ALKHARID(new WorldTile(3288, 3182, 0)), CHAMPIONS_GUILD(
				new WorldTile(3175, 3362, 0));

		private final WorldTile coord;

		private StarSpawns(WorldTile coord) {
			this.coord = coord;
		}

		public WorldTile getCoord() {
			return coord;
		}
	}

	public enum StarStages {
		FIRST(38659, 0, 0, 0, 0), 
		SECOND(38660, 300, 9, 90, 14), 
		THIRD(38661, 275, 8, 80, 25), 
		FORTH(38662, 250, 7, 70, 29), 
		FIVE(38663, 200, 6, 60, 32), 
		SIX(38664, 175, 5, 50, 47), 
		SEVEN(38665, 150, 4,40, 71),
		EIGHT(38666, 100, 3, 30, 114), 
		NINE(38667, 50, 2, 20, 145), 
		TEN(38668, 25, 1, 10, 210);

		private final int id, starDust, starSize, level, xp;

		private StarStages(int id, int starDust, int starSize, int level, int xp) {
			this.id = id;
			this.starDust = starDust;
			this.starSize = starSize;
			this.level = level;
			this.xp = xp;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public int getStarDust() {
			return starDust;
		}

		public int getStarSize() {
			return starSize;
		}

		public int getXP() {
			return xp;
		}
	}

	private static WorldTile location;
	public static String locationName;
	public static int stage = 0, starSize, starDust, totalStarDust;
	public static boolean found;

	private static boolean shutdown = false;
	public static void prospect(Player player) {
		player.sm("<col=006600>The star's size is " + starSize + "");
		player.sm("<col=006600>Star Dust - Count: " + starDust + " Total: "
				+ totalStarDust + "");
	}
	private static void reset() {
		World.spawnObject(
				new WorldObject(-1, 10, -1, location.getX(), location.getY(),
						location.getPlane()), false);
		stage = 0;
		starSize = 0;
		starDust = 0;
		totalStarDust = 0;
		location = null;
		found = false;
	}

	private static void sendStar() {// everything is like brand new, this is a
									// whole new star at variable location
		if (location == null) {// just to make sure
			spawnStar(false);
			return;
		} else {// if location isn't null lets spawn the first star at location
			World.spawnObject(new WorldObject(StarStages.FIRST.getId(), 10, -1,
					location.getX(), location.getY(), location.getPlane()),
					true);// spawns the star falling
			CoresManager.slowExecutor.schedule(new Runnable() {// waits 3
																// seconds
						@Override
						public void run() {
							try {
								stage = 1;
								starSize = StarStages.SECOND.getStarSize();
								starDust = 0;
								totalStarDust = StarStages.SECOND.getStarDust();
								World.spawnObject(new WorldObject(
										StarStages.SECOND.getId(), 10, -1,
										location.getX(), location.getY(),
										location.getPlane()), true);// then
																	// spawns
																	// right
																	// star you
																	// can mine
							} catch (Throwable e) {
								Logger.handle(e);
							}
						}
					}, 3, TimeUnit.SECONDS);
		}
	}

	private static void setLocation() {
		StarSpawns[] spawns = { StarSpawns.VARROCK, StarSpawns.RIMMINGTON,
				StarSpawns.ALKHARID, StarSpawns.CHAMPIONS_GUILD,
				StarSpawns.DOMINION_TOWER, StarSpawns.DRAYNOR,
				StarSpawns.DUEL_ARENA, StarSpawns.FALADOR,
				StarSpawns.ROCK_CRABS, StarSpawns.WILDERNESS_VOLCANO,
				StarSpawns.YANILLE };
		StarSpawns randomSpawn = spawns[Utils.getRandom(spawns.length - 1)];
		locationName = randomSpawn.name().toLowerCase()
				.replace("_", " ");
		location = randomSpawn.getCoord();
		World.sendWorldMessage(
				"<col=FFFF00>A new shooting star has fallen in "
						+ Character.toUpperCase(locationName.charAt(0))
						+ locationName.substring(1), false);
	}

	public static void shutdown() {
		stage = 8;
		starDust = 10000;
		shutdown = true;
		upgradeStar(null);
	}

	public static void spawnStar(boolean restarting) {// spawns star in new
														// location with
														// everything fresh
		if (location != null || restarting)// if a previous star existed the
											// location wouldn't be null
			reset();// so lets refresh everything
		setLocation();// generate a random location to spawn star, and announce
						// the location.
		sendStar();// spawn the star at location that was set
	}

	public static void upgradeStar(Player player) {// call this method whenever
													// recieveing Star Dust
		if (location == null) {
			spawnStar(false);
			return;
		} else {
			if (stage == 1) {
				if (starDust >= StarStages.SECOND.getStarDust()) {
					stage = 2;
					starSize = StarStages.THIRD.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.THIRD.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.THIRD.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 2) {
				if (starDust >= StarStages.THIRD.getStarDust()) {
					stage = 3;
					starSize = StarStages.FORTH.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.FORTH.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.FORTH.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 3) {
				if (starDust >= StarStages.FORTH.getStarDust()) {
					stage = 4;
					starSize = StarStages.FIVE.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.FIVE.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.FIVE.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 4) {
				if (starDust >= StarStages.FIVE.getStarDust()) {
					stage = 5;
					starSize = StarStages.SIX.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.SIX.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.SIX.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 5) {
				if (starDust >= StarStages.SIX.getStarDust()) {
					stage = 6;
					starSize = StarStages.SEVEN.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.SEVEN.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.SEVEN.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 6) {
				if (starDust >= StarStages.SEVEN.getStarDust()) {
					stage = 7;
					starSize = StarStages.EIGHT.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.EIGHT.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.EIGHT.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 7) {
				if (starDust >= StarStages.EIGHT.getStarDust()) {
					stage = 8;
					starSize = StarStages.NINE.getStarSize();
					starDust = 0;
					totalStarDust = StarStages.NINE.getStarDust();
					World.spawnObject(
							new WorldObject(StarStages.NINE.getId(), 10, -1,
									location.getX(), location.getY(), location
											.getPlane()), true);
					player.getActionManager().forceStop();
				}
			} else if (stage == 8) {
				if (starDust >= StarStages.NINE.getStarDust()) {
					stage = 0;
					starSize = 0;
					starDust = 0;
					totalStarDust = 0;
					World.spawnObject(
							new WorldObject(-1, 10, -1, location.getX(),
									location.getY(), location.getPlane()),
							false);
					if(player != null)
					player.getActionManager().forceStop();
					final NPC rewardPerson = World.spawnNPC(8091, location, -1,
							false);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if(!shutdown) {
							rewardPerson.finish();
							spawnStar(true);
							}
							stop();
						}
					}, 300);
				}
			}
		}
	}
}
