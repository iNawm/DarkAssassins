package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Arham 4 on 6/18/14.
 */
public class XPWell {

    /**
     * The amount of time for the WorldTask.
     */
    public static int taskAmount = 1800000;
    public static int taskTime = 1800000;

    /**
     * Sends a dialogue for the amount to give.
     *
     * @param player The Player giving the amount.
     */
    public static void give(Player player) {
        if (World.isWellActive()) {
            player.getPackets().sendGameMessage("The XP well is already active! Go train!");
            return;
        }
        player.getPackets().sendRunScript(109, "Progress: " + NumberFormat.getNumberInstance(Locale.US).format(World.getWellAmount()) + " GP (" + ((World.getWellAmount() * 100) / Settings.WELL_MAX_AMOUNT) + "% of Goal); Goal: " + NumberFormat.getNumberInstance(Locale.US).format(Settings.WELL_MAX_AMOUNT) + " GP");
        player.getTemporaryAttributtes().put("donate_xp_well", Boolean.TRUE);
    }

    /**
     * Donates to the well the amount to give.
     *
     * @param player The Player donating.
     * @param amount The amount to give.
     */
    public static void donate(Player player, int amount) {
        if (amount < 0)
            return;
        if ((World.getWellAmount() + amount) > Settings.WELL_MAX_AMOUNT) {
            amount = Settings.WELL_MAX_AMOUNT - World.getWellAmount();
        }
        if (!player.getInventory().containsItem(995, amount) && player.money < amount) {
            player.getPackets().sendGameMessage("You don't have that much money!");
            return;
        }
        if (amount < 100000) {
            player.getPackets().sendGameMessage("You must donate at least 100,000 (100K) GP!");
            return;
        }
        boolean pouch = !player.getInventory().containsItem(995, amount);
        if (!pouch)
            player.getInventory().deleteItem(995, amount);
        else {
            player.getPackets().sendRunScript(5561, 0, amount);
            player.money -= amount;
            player.refreshMoneyPouch();
        }
        World.addWellAmount(player.getDisplayName(), amount);
        postDonation();
    }

    /**
     * A check after donating to the well to see if the x2 XP should start.
     */
    private static void postDonation() {
        if (World.getWellAmount() >= Settings.WELL_MAX_AMOUNT) {
  
            World.sendWorldMessage("<col=FF0000>The goal of " + NumberFormat.getNumberInstance(Locale.US).format(Settings.WELL_MAX_AMOUNT) + " GP has been reached! Double XP for 5 hours begins now!", false);
            taskAmount = 250000000;
			Settings.ATTACK_XP_RATE = (Settings.ATTACK_XP_RATE * 2);
            Settings.STRENGTH_XP_RATE = (Settings.STRENGTH_XP_RATE * 2);
			Settings.DEFENCE_XP_RATE = (Settings.DEFENCE_XP_RATE * 2);
            Settings.HITPOINTS_XP_RATE = (Settings.HITPOINTS_XP_RATE * 2);
			Settings.RANGE_XP_RATE = (Settings.RANGE_XP_RATE * 2);
            Settings.MAGIC_XP_RATE = (Settings.MAGIC_XP_RATE * 2);
			Settings.PRAYER_XP_RATE = (Settings.PRAYER_XP_RATE * 2);
            Settings.SLAYER_XP_RATE = (Settings.SLAYER_XP_RATE * 2);			
			Settings.FARMING_XP_RATE = (Settings.FARMING_XP_RATE * 2);
            Settings.AGILITY_XP_RATE = (Settings.AGILITY_XP_RATE * 2);
			Settings.HERBLORE_XP_RATE = (Settings.HERBLORE_XP_RATE * 2);
            Settings.THIEVING_XP_RATE = (Settings.THIEVING_XP_RATE * 2);
			Settings.CRAFTING_XP_RATE = (Settings.CRAFTING_XP_RATE * 2);
            Settings.MINING_XP_RATE = (Settings.MINING_XP_RATE * 2);
			Settings.SMITHING_XP_RATE = (Settings.SMITHING_XP_RATE * 2);
            Settings.FISHING_XP_RATE = (Settings.FISHING_XP_RATE * 2);
			Settings.COOKING_XP_RATE = (Settings.COOKING_XP_RATE * 2);
            Settings.FIREMAKING_XP_RATE = (Settings.FIREMAKING_XP_RATE * 2);
			Settings.WOODCUTTING_XP_RATE = (Settings.WOODCUTTING_XP_RATE * 2);
            Settings.CONSTRUCTION_XP_RATE = (Settings.CONSTRUCTION_XP_RATE * 2);
			Settings.HUNTER_XP_RATE = (Settings.HUNTER_XP_RATE * 2);
            Settings.SUMMONING_XP_RATE = (Settings.SUMMONING_XP_RATE * 2);
			Settings.DUNGEONEERING_XP_RATE = (Settings.DUNGEONEERING_XP_RATE * 2);
            Settings.FLETCHING_RATE = (Settings.FLETCHING_RATE * 2);
			Settings.RUNECRAFTING_XP_RATE = (Settings.RUNECRAFTING_XP_RATE * 2);
			
            setWellTask();
            World.setWellActive(true);
        }
    }

    /**
     * Sets the task for the reset of the well.
     */
    public static void setWellTask() {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                World.setWellActive(false);
                World.resetWell();
                this.stop();
            }
        }, taskAmount);
    }

    /**
     * Saves the progress of the well. If the x2 event is already active, this sends the amount
     * left in milliseconds.
     */
    public static void save() {
        File output = new File("./data/well/data.txt");

        if (output.canWrite()) {
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(output, false));
                if (World.isWellActive()) {
                    out.write("true " + taskTime);
                } else {
                    out.write("false " + World.getWellAmount());
                }
            } catch (IOException ignored) {
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}