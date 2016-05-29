package com.rs.game.minigames;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.SoulWarsPlaying;
import com.rs.game.player.controlers.SoulwarsWaiting;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public final class Soulwars {
	public static final int BLUE = 0;
	public static final int RED = 1;
	private static final int GREEN = 2;
	public static boolean waiting1 = false;
	public int swwating;
	public static int minutesLeft;
	public static int waitingt;
	public int playingt;
	@SuppressWarnings("unchecked")
	private static final List<Player>[] waiting = new List[3];
	@SuppressWarnings("unchecked")
	private static final List<Player>[] playing = new List[3];
	private static int[] seasonWins = new int[2];
	public static final WorldTile LOBBY = new WorldTile(1892, 3178, 0),
			BLUE_WAITING = new WorldTile(1879, 3162, 0),
			BLUE_OUT = new WorldTile(1880, 3162, 0),
			BLUE_GRAVE = new WorldTile(1886, 3171, 0),
			RED_WAITING = new WorldTile(1900, 3162, 0),
			RED_OUT = new WorldTile(1899, 3162, 0),
			RED_GRAVE = new WorldTile(1894, 3171, 0),
			BLUE_BASE = new WorldTile(1820, 3225, 0),
			RED_BASE = new WorldTile(1954, 3239, 0);

	private static PlayingGame playingGame;
	private static PlayingGame waitingGame;
	

	static {
		init();
	}

	public static void init() {
		for (int i = 0; i < waiting.length; i++)
			waiting[i] = Collections.synchronizedList(new LinkedList<Player>());
		for (int i = 0; i < playing.length; i++)
			playing[i] = Collections.synchronizedList(new LinkedList<Player>());
	}

	public static int getPowerfullestTeam() {
		int redteam = waiting[RED].size() + playing[RED].size();
		int blueteam = waiting[BLUE].size() + playing[BLUE].size();
		if (blueteam == redteam)
			return GREEN;
		if (redteam > blueteam)
			return RED;
		return BLUE;
	}

	public static void joinPortal(Player player, int team) {
		if (player.getEquipment().getCapeId() != -1) {
			player.getPackets().sendGameMessage("You cannot wear hats, capes or helmets in the arena.");
			return;
		}
		int powerfullestTeam = getPowerfullestTeam();
		if (team == GREEN) {
			team = powerfullestTeam == RED ? BLUE : RED;
		} else if (team == powerfullestTeam) {
			if (team == RED)
				player.getPackets().sendGameMessage("The red team is much to powerful, join the blue team instead.");
			else if (team == BLUE)
				player.getPackets().sendGameMessage("The blue team is much to powerful, join the red team instead.");
			return;
		}
		player.addStopDelay(2);
		waiting[team].add(player);
		setCape(player, new Item(team == RED ? 14641 : 14642));
		player.getControlerManager().startControler("SoulwarsWaiting", team);
		player.setNextWorldTile(new WorldTile(team == RED ? RED_WAITING
				: BLUE_WAITING, 0));
		player.getMusicsManager().playMusic(318); // temp testing else 5
		if (playingGame == null && waiting[BLUE].size() >= 1 && waiting[RED].size() >= 1) 
			startGame();
			joinPlayingGame(player, team);
		if(playingGame != null)
			refreshWaitingTimeLeft(player);
			startGame();
			joinPlayingGame(player, team);
	}

	public static void setCape(Player player, Item cape) {
		player.getEquipment().getItems().set(Equipment.SLOT_CAPE, cape);
		player.getEquipment().refresh(Equipment.SLOT_CAPE);
		player.getAppearence().loadAppearanceBlock();
	}

	public static void createPlayingGame() {
		playingGame = new PlayingGame();
		CoresManager.fastExecutor.scheduleAtFixedRate(playingGame, 60000, 60000);
		refreshAllPlayersTime();
	}

	public static void createWaitingGame(Player player) {
		waitingGame = new PlayingGame();
		CoresManager.fastExecutor.scheduleAtFixedRate(waitingGame, 60000, 60000);
		refreshWaitingTimeLeft(player);
		refreshAllPlayersTime();
	}
		public static boolean handleObjects(Player player, int objectId) {
			if (objectId == 42030 && waiting1 == false) {
				joinPortal(player, RED);
				waiting1 = true;
				return true;
			}
			if (objectId == 42030 && waiting1 == true) {
				removeWaitingPlayerRed(player, RED);
				waiting1 = false;
				return true;
			}
			if (objectId == 42031) {
				joinPortal(player, GREEN);
				waiting1 = true;
				return true;
				}
			if (objectId == 42029 && waiting1 == false) {
				joinPortal(player, BLUE);
				waiting1 = true;
				return true;
			}
			if (objectId == 42029 && waiting1 == true) {
				removeWaitingPlayerBlue(player, BLUE);
				waiting1 = false;
				return true;
			}
			return false;
		}

	public static void destroyPlayingGame() {
		playingGame.cancel();
		playingGame = null;
		refreshAllPlayersTime();
		leavePlayersSafely();
	}

	public static void destroywaitingGame() {
		waitingt = -1;
		refreshAllPlayersTime();
		leavePlayersSafely();
	}

	public static void leavePlayersSafely() {
		leavePlayersSafely(-1);
	}
	public static void leavePlayersSafely(final int winner) {
		for (int i = 0; i < playing.length; i++) {
			for (final Player player : playing[i]) {
				player.addStopDelay(7);
				player.stopAll();
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				for (int i = 0; i < playing.length; i++)
					for (Player player : playing[i]
							.toArray(new Player[playing[i].size()])) {
						forceRemovePlayingPlayer(player);
						if (winner != -1) {
							if (winner == -2) {
								player.getPackets().sendGameMessage("Both team draw.");
								player.soulwarsp += 2;
							} else if (winner == i) {
								player.getPackets().sendGameMessage("Your team won.");
								player.soulwarsp += 3;					} else
								player.getPackets().sendGameMessage("Your team lost.");
								player.soulwarsp += 1;	
						}
					}
			}
		}, 6);
	}

	public static void forceRemoveWaitingPlayer(Player player) {
		player.getControlerManager().forceStop();
	}

	public static void removeWaitingPlayerBlue(Player player, int team) {
		waiting[team].remove(player);
		setCape(player, null);
		forceRemoveWaitingPlayer(player);
		player.setNextWorldTile(new WorldTile(BLUE_OUT, 0));
		if (playingGame != null && waiting[team].size() == 0
				&& playing[team].size() == 0)
			destroywaitingGame(); // cancels if 0 players playing/waiting on any
									// of the tea
	}

	public static void removeWaitingPlayerRed(Player player, int team) {
		waiting[team].remove(player);
		setCape(player, null);
		forceRemoveWaitingPlayer(player);
		player.setNextWorldTile(new WorldTile(RED_OUT, 0));
		if (playingGame != null && waiting[team].size() == 0
				&& playing[team].size() == 0)
			destroywaitingGame(); // cancels if 0 players playing/waiting on any
									// of the tea
	}
	public static void refreshWaitingTimeLeft(Player player) {
		// player.getPackets().sendConfig(380, playingGame == null ? 0 :
		// playingGame.minutesLeft(player.getControlerManager().getControler()
		// instanceof SoulWarsPlaying ? 5 : 0));
		//player.getPackets().sendIComponentText(837 ,9 ,""+waitingminutesLeft()+"");
		player.getPackets().sendIComponentText(837,9,""+WaitingGame.waitingminutessLeft()+"");
		player.getPackets().sendIComponentText(837, 5, "" + waiting[RED].size());
		player.getPackets().sendIComponentText(837, 3, "" + waiting[BLUE].size());
	}
	public static void refreshPlayingTimeLeft(Player player) {
		/* player.getPackets().sendConfig(380, playingGame == null ? 0 :
		 playingGame.minutesLeft(player.getControlerManager().getControler()
		 instanceof SoulWarsPlaying ? 5 : 0));
		player.getPackets()
				.sendIComponentText(
						837,
						9,
						""
								+ (playingGame == null ? 0
										: playingGame
												.minutesLeft(player
														.getControlerManager()
														.getControler() instanceof SoulWarsPlaying ? 5
														: 0)));
		player.getPackets().sendIComponentText(837, 5, "" + waiting[RED].size());
		player.getPackets().sendIComponentText(837, 3, "" + waiting[BLUE].size());*/
	}
	public static void startGame() {
		for (int i = 0; i < waiting.length; i++) {
			for (Player player : waiting[i].toArray(new Player[waiting[i].size()])) {
				joinPlayingGame(player, i);
			}
		}
	}

	public static void forceRemovePlayingPlayer(Player player) {
		player.getControlerManager().forceStop();
	}

	public static void removePlayingPlayer(Player player, int team) {
		playing[team].remove(player);
		player.reset();
		player.setCanPvp(false);

		// remove the items
		setCape(player, null);
		int weaponId = player.getEquipment().getWeaponId();
		player.getInventory().deleteItem(4049, Integer.MAX_VALUE); // bandages
		player.getInventory().deleteItem(4053, Integer.MAX_VALUE); // barricades
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		if (team == BLUE) {
		player.setNextWorldTile(new WorldTile(BLUE_GRAVE, 2));
		}
		else if (team == RED) {
		player.setNextWorldTile(new WorldTile(RED_GRAVE, 2));
		}
		if (playingGame != null && waiting[team].size() == 0
				&& playing[team].size() == 0)
			destroyPlayingGame(); // cancels if 0 players playing/waiting on any
									// of the tea
	}

	public static void joinPlayingGame(Player player, int team) {
		playingGame.refresh(player);
		waiting[team].remove(player);
		player.getControlerManager().removeControlerWithoutCheck();
		playing[team].add(player);
		player.setCanPvp(true);
		player.getControlerManager().startControler("SoulWarsPlaying", team);
		player.setNextWorldTile(new WorldTile(team == RED ? RED_BASE
				: BLUE_BASE, 1));
	}

	public static void endGame(int winner) {
		if (winner != -2)
		seasonWins[winner]++;
		leavePlayersSafely(winner);
	}

	public static void refreshAllPlayersTime() {
		for (int i = 0; i < waiting.length; i++)
			for (Player player : waiting[i])
			refreshPlayingTimeLeft(player);
		for (int i = 0; i < playing.length; i++)
			for (Player player : playing[i])
			refreshPlayingTimeLeft(player);
	}

	public static void refreshAllPlayersPlaying() {
		for (int i = 0; i < playing.length; i++)
			for (Player player : playing[i])
				playingGame.refresh(player);
	}
	public static void refreshAllPlayersWaiting() {
		for (int i = 0; i < playing.length; i++)
			for (Player player : playing[i])
				waitingGame.refresh(player);
	}

	private static class WaitingGame extends TimerTask {
		public static int minutesLeft = 0;
			public static int waitingminutessLeft() {
				return minutesLeft;
			}
			@Override
			public void run() {
				minutesLeft--;
				for (Player player : World.getPlayers()) {
				if (minutesLeft == 0) {
					minutesLeft = 23;
					startGame();
					refreshAllPlayersTime();
					refreshWaitingTimeLeft(player);
					}
				else if(minutesLeft >= 1) {
					minutesLeft--;
					refreshAllPlayersTime();
					refreshWaitingTimeLeft(player);
					}
				}
			}
		}

	private static class PlayingGame extends TimerTask {

		private static final int ALIVE = 0, ATTACKED = 1, DEAD = 2;
		public int minutesLeft;
		private int[] score;
		private int[] avatarStatus;

		public PlayingGame() {
			reset();
		}

		public int PlayingminutesLeft(int i) {
			return 6;
		}

		public void reset() {
			minutesLeft = 7; // temp testing else 5
			score = new int[2];
			avatarStatus = new int[2];
		}

		public void addScore(Player player, int team, int flagTeam) {
			score[team] += 1;
		}
		public static boolean handleObjects(Player player, int objectId) {
			if (objectId == 42030) {
				if(waiting1 = false) {
				joinPortal(player, RED);
				waiting1 = true;
				return true;
				}
			}
			else if (objectId == 42030) {
				if(waiting1 = true) {
				removeWaitingPlayerRed(player, RED);
				waiting1 = false;
				return true;
				}
			}
			else if (objectId == 42031) {
				joinPortal(player, GREEN);
				waiting1 = true;
				return true;
				}
			else if (objectId == 42029) {
				if(waiting1 = false) {
				joinPortal(player, BLUE);
				waiting1 = true;
				return true;
				}
			}
			else if (objectId == 42029) {
				if(waiting1 = false) {
				removeWaitingPlayerBlue(player, BLUE);
				waiting1 = false;
				return true;
				}
			}
			return false;
		}
		public void refresh(Player player) {
			player.getPackets().sendConfigByFile(145, score[BLUE]);
			player.getPackets().sendConfigByFile(155, score[RED]);
		}
		@Override
		public void run() {
			minutesLeft--;
			if (minutesLeft == 5) {
				endGame(score[BLUE] == score[RED] ? -2
						: score[BLUE] > score[RED] ? BLUE
								: RED);
				reset();
			} else if (minutesLeft == 0) {
				minutesLeft = 25;
				startGame();
			} else if (minutesLeft > 6) // adds ppl waiting on lobby
				startGame();
			refreshAllPlayersTime();
		}
	}



	public static List<Player>[] getPlaying() {
		return playing;
	}

	public static boolean isBarricadeAt(WorldTile toTile) {
		// TODO Auto-generated method stub
		return false;
	}
}