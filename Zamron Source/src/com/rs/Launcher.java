package com.rs;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.alex.store.Index;
import com.guardian.PriceLoader;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.GameEngine;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.minigames.soulwars.SoulLobby;
import com.rs.game.minigames.soulwars.SoulWars;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.player.LendingManager;
import com.rs.game.player.Player;
import com.rs.game.player.StarterMap;
import com.rs.game.player.content.FishingSpotsHandler;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.XPWell;
import com.rs.game.player.content.botanybay.BotanyBay;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.custom.ItemManager;
import com.rs.game.player.content.grandExchange.GrandExchange;
import com.rs.game.player.content.ShootingStars;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.game.player.cutscenes.CutscenesHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.game.worldlist.WorldList;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.Censor;
import com.rs.utils.DTRank;
import com.rs.utils.DisplayNames;
import com.rs.utils.IPBanL;
import com.rs.utils.IPMute;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemSpawns;
import com.rs.utils.KillStreakRank;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.MapAreas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.NPCExamines;
import com.rs.utils.MACBanL;
import com.rs.utils.NPCSpawning;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawning;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.WeightManager;
import com.rs.utils.huffman.Huffman;
import org.runetoplist.VoteChecker;

public final class Launcher {
	
	public static VoteChecker voteChecker = new VoteChecker("", "", "", "zamronne_vote");
	
	public static void main(String[] args) throws Exception {
		long currentTime = Utils.currentTimeMillis();
		System.out.println("" + "\n--------------------------------------------------------------------------------"
				+ "\n                           Zamron                             	" + "\n                            Created by Leighton                            	"
				+ "\n--------------------------------------------------------------------------------");
		Logger.log("Zamron", "Reading Cache Intake...");
		Cache.init();
		com.guardian.Launcher.main(args);
		KillStreakRank.init();
		//GrandExchange.init(); disabled
		ItemsEquipIds.init();
		ItemManager.inits();
		ItemSpawns.init();
		Huffman.init();
		Logger.log("Zamron", "Loaded Cache...");
		Logger.log("Zamron", "Loading Data...");
		WorldList.init();
		Censor.init();
		DisplayNames.init();
		IPBanL.init();
		IPMute.init();
		MACBanL.init();
		Logger.log("Zamron", "Loading Item Weights...");
		WeightManager.init();
		PkRank.init();
		DTRank.init();
		Logger.log("Zamron", "Loading Minigames...");
		minigames();
		MapArchiveKeys.init();
		MapAreas.init();
		ObjectSpawns.init();
		NPCSpawns.init();
		NPCCombatDefinitionsL.init();
		NPCBonuses.init();
		NPCDrops.init();
		ItemExamines.init();
		ItemBonuses.init();
		StarterMap.init();
		MusicHints.init();
		BotanyBay.init();
		ShopsHandler.init();
		NPCExamines.init();
		Logger.log("Zamron", "Loading Global Spawns...");
		NPCSpawning.spawnNPCS();
		ObjectSpawning.spawnNPCS();
		FishingSpotsHandler.init();
		Logger.log("Zamron", "Loading Combat Scripts...");
		CombatScriptsHandler.init();
		Logger.log("Launcher", "Initing Lending Manager...");
		LendingManager.init();
		Logger.log("Zamron", "Reading Local Handlers...");
		Logger.log("Zamron", "Reading Local Controlers...");
		Logger.log("Zamron", "Reading Local Managers...");
		Logger.log("Zamron", "Preparing Game Engine...");
		Logger.log("Zamron", "Preparing Grand Exchange...");
		DialogueHandler.init();
		ControlerHandler.init();
		CutscenesHandler.init();
		FriendChatsManager.init();
		Logger.log("Launcher", "Initing Control Panel...");
		CoresManager.init();
		Logger.log("Zamron", "Loading World...");
		World.init();
		Logger.log("Zamron", "Loading Region Builder...");
		RegionBuilder.init();
		ClansManager.init();
		ShootingStars.spawnStar(false);
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			Logger.handle(e);
			Logger.log("Zamron",
					"Failed initing Server Channel Handler. Shutting down...");
			System.exit(1);
			return;
		}
		Logger.log("Zamron", "Server took "
				+ (Utils.currentTimeMillis() - currentTime)
				+ " milliseconds to launch.");
		addAccountsSavingTask();
		if (Settings.HOSTED)
		addCleanMemoryTask();
		ControlPanel.init();
		// Donations.init();
		//HalloweenEvent.startEvent();
		addrecalcPricesTask();
		Logger.log("World", "Zamron is now Online!");
	}
	
    private static void addrecalcPricesTask() {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, 1);
	c.set(Calendar.HOUR, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	int minutes = (int) ((c.getTimeInMillis() - Utils.currentTimeMillis()) / 1000 / 60);
	int halfDay = 12 * 60;
	if (minutes > halfDay)
	    minutes -= halfDay;
	CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
	    @Override
	    public void run() {
		try {
		  //  GrandExchange.recalcPrices();
		}
		catch (Throwable e) {
		    Logger.handle(e);
		}

	    }
	}, minutes, halfDay, TimeUnit.MINUTES);
    }
	

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

		private static void addAccountsSavingTask() {
			CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						saveFiles();
						Logger.log("Online", 
								"There are currently " + (World.getPlayers().size())
										+ " players playing " + Settings.SERVER_NAME
										+ ".");
					} catch (Throwable e) {
						Logger.handle(e);
					}

				}
			}, 1, 1, TimeUnit.MINUTES);//can be changed to seconds using "TimeUnit.SECONDS" as of now every one minute it will save the players.
		}

	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
		DisplayNames.save();
		IPBanL.save();
		IPMute.save();
		MACBanL.save();
		PkRank.save();
		DTRank.save();
		KillStreakRank.save();
		//GrandExchange.save();
		XPWell.save();
		}
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			/*for (Region region : World.getRegions().values())
				region.removeMapFromMemory();*/
		}
		for (Index index : Cache.STORE.getIndexes())
			index.resetCachedFiles();
		CoresManager.fastExecutor.purge();
		System.gc();
	}
	
	public static void minigames() {
		SoulLobby.minutes = 5;
		SoulWars.startedGame = false;
		SoulLobby.redWait.clear();
		SoulLobby.blueWait.clear();
		SoulLobby.allWaiting.clear();
		SoulWars.playing.clear();
	}

	public static void shutdown() {
		try {
			GameEngine.get().shutdown();
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (Settings.HOSTED) {
			try {
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}
	
	
	private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL(
				"http://Zamron.net/register.php?amount=" + amount);
		url.openStream().close();
	}
	
	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					setWebsitePlayersOnline(World.getPlayers().size());
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec("java -server -Xms2048m -Xmx20000m -cp bin;/data/libs/netty-3.5.2.Final.jar;/data/libs/FileStore.jar Launcher false false true false");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}

	}

	private Launcher() {

	}

}
