package com.rs.game.npc;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.net.decoders.handlers.NPCHandler;
import com.rs.utils.NPCSpawns;


public final class AdminNPCInterface {
	private static boolean deletingSpawn;
	private static boolean killingNPC;
	private static boolean getLocation;
	private static boolean getExamine;
	private static boolean test;
	
	private static NPC npc;
	
	public static void handleButtons(Player p, int componentId) {
		for (int i = 0; i < 4; i++) {
			if (componentId != 2) {
				setKillingNPC(false);
				getLocation = false;
				getExamine = false;
				deletingSpawn = false;
			} if (componentId == 9 || componentId == 7) {
				setKillingNPC(true);
			} if (componentId == 8 || componentId == 10) {
				deletingSpawn = true;
			} if (componentId == 16 || componentId == 15) {
				getLocation = true;
			} if (componentId == 18 || componentId == 19) {
				getExamine = true;
			} if (componentId == 2) {
				if (!(!(!(!(deletingSpawn && isKillingNPC() && getLocation && getExamine && test))))) {
					p.sm("You need to select an option first.");
					return;
				} else {
					p.getInterfaceManager().closeScreenInterface();
					if (isKillingNPC()) {
						npc.sendDeath(npc);
						npc.drop();
						p.sm("NPC killed");
						return;
					} if (deletingSpawn) {
						try {
							NPCSpawns.removeSpawn(npc);
							p.sm("Spawn successfully removed");
						} catch (Throwable e) {

							e.printStackTrace();
						}
						return;
					} if (getLocation) {
						p.sm(npc.getLocation());
						return;
					} if (getExamine) {
					//	NPCHandler.handleExamine();
						return;
					}
				}
			}
		}
	}
	
	public static void sendInterface(Player p, NPC npc) {
		p.getPackets().sendIComponentText(31, 3, "<img=1> Admin NPC Options");
		p.getPackets().sendIComponentText(31, 13, "NPC Id - "+npc.getId()+" ("+npc.getName()+")");
		p.getPackets().sendIComponentText(31, 14, "");
		p.getPackets().sendIComponentText(31, 9, "Kill NPC");
		p.getPackets().sendIComponentText(31, 10, "Remove NPC");
		p.getPackets().sendIComponentText(31, 16, "Print location");
		p.getPackets().sendIComponentText(31, 19, "Print examine");
		p.getPackets().sendIComponentText(31, 11, "");
		p.getPackets().sendIComponentText(31, 12, "");
		p.getPackets().sendIComponentText(31, 17, "");
		p.getPackets().sendIComponentText(31, 20, "");
		p.getPackets().sendIComponentText(31, 5, "Confirm");
		AdminNPCInterface.npc = npc;
		p.getInterfaceManager().sendInterface(31);
		return;
	}

	public static boolean isKillingNPC() {
		return killingNPC;
	}

	public static void setKillingNPC(boolean killingNPC) {
		AdminNPCInterface.killingNPC = killingNPC;
	}

}