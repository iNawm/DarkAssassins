package com.rs.game.player.controlers;

import java.util.List;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.Soulwars;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.CastleWarBarricade;
import com.rs.game.player.Equipment;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class SoulWarsPlaying extends Controler {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	@Override
	public boolean canMove(int dir) {
		WorldTile toTile = new WorldTile(player.getX()
				+ Utils.DIRECTION_DELTA_X[dir], player.getY()
				+ Utils.DIRECTION_DELTA_Y[dir], player.getPlane());
		return !Soulwars.isBarricadeAt(toTile);
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387) {
			if (componentId == 9) {
				player.getPackets().sendGameMessage(
						"You can't remove your team's colours.");
				return false;
			}
		}
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE) {
			player.getPackets().sendGameMessage(
					"You can't take away your team's faith!");
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			if (canHit(target))
				return true;
			player.getPackets().sendGameMessage("You can't attack your team.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		Player p2 = (Player) target;
		if (p2.getEquipment().getCapeId() == player.getEquipment().getCapeId())
			return false;
		return true;
	}

	// You can't leave just like that!

	public void leave() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 8);
		Soulwars.removePlayingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 836);
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					int weaponId = player.getEquipment().getWeaponId();
					if (weaponId == 1 || weaponId == 2) {
						CastleWars.setWeapon(player, null);
						CastleWars.dropFlag(player,
								weaponId == 4037 ? CastleWars.SARADOMIN
										: CastleWars.ZAMORAK);
					} else {
						Player killer = getMostDamageReceivedSourcePlayer();// temporary
																		// so
																		// ppl
																		// have
																		// reason
																		// to
																		// play
																		// cw
						if (killer != null)
							killer.increaseKillCount(player);
					}

					player.reset();
					player.setNextWorldTile(new WorldTile(
							team == CastleWars.ZAMORAK ? CastleWars.ZAMO_BASE
									: CastleWars.SARA_BASE, 1));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	protected Player getMostDamageReceivedSourcePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(CastleWars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 42029 || id == 42030) {
			removeControler();
			leave();
			return false;
			// under earth from basess
/*		} else if (id == 4411) {// stepping stone
			if (object.getX() == player.getX()
					&& object.getY() == player.getY())
				return false;
			player.addStopDelay(2);
			player.setNextAnimation(new Animation(741));
			player.addWalkSteps(object.getX(), object.getY(), -1, false);
		} else if (id == 36693) {
			player.useStairs(827, new WorldTile(2430, 9483, 0), 1, 2);
			return false;
		} else if (id == 36481) {
			player.useStairs(-1, new WorldTile(2417, 3075, 0), 0, 1);
			return false;
		} else if (id == 36644) {
			if (object.getY() == 9508)
				player.useStairs(828, new WorldTile(2400, 3106, 0), 1, 2);
			else if (object.getY() == 9499)
				player.useStairs(828, new WorldTile(2399, 3100, 0), 1, 2);
			player.setFreezeDelay(0);
			player.setFrozeBlocked(0);
			return false;*/
		}/*
		 * else if (id == 4438) player.getActionManager().setSkill(new
		 * Mining(object, RockDefinitions.SMALLER_ROCKS)); else if (id == 4437)
		 * player.getActionManager().setSkill(new Mining(object,
		 * RockDefinitions.ROCKS ));
		 */
		else if (id == 4448) {
			for (List<Player> players : Soulwars.getPlaying()) {
				for (Player player : players) {
					if (player.withinDistance(object, 1))
						player.applyHit(new Hit(player, player.getHitpoints(),
								HitLook.REGULAR_DAMAGE));
				}
			}
			World.spawnObject(
					new WorldObject(4437, object.getType(), object
							.getRotation(), object.getX(), object.getY(),
							object.getPlane()), true);
		}
		return true;
	}

/*	@Override
	public boolean processObjectClick2(WorldObject object) {
		int id = object.getId();
		if (id == 36579 || id == 36586) {
			player.getInventory().addItem(new Item(4049, 5));
			return false;
		} else if (id == 36575 || id == 36582) {
			player.getInventory().addItem(new Item(4053, 5));
			return false;
		} else if (id == 36577 || id == 36584) {
			player.getInventory().addItem(new Item(4045, 5));
			return false;
		}
		return true;
	}*/

	public void passBarrier(WorldObject object) {
		if (object.getRotation() == 0 || object.getRotation() == 2) {
			if (player.getY() != object.getY())
				return;
			player.addStopDelay(2);
			player.addWalkSteps(object.getX() == player.getX() ? object.getX()
					+ (object.getRotation() == 0 ? -1 : +1) : object.getX(),
					object.getY(), -1, false);
		} else if (object.getRotation() == 1 || object.getRotation() == 3) {
			if (player.getX() != object.getX())
				return;
			player.addStopDelay(2);
			player.addWalkSteps(
					object.getX(),
					object.getY() == player.getY() ? object.getY()
							+ (object.getRotation() == 3 ? -1 : +1) : object
							.getY(), -1, false);
		}
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}