package com.rs.game.npc;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.SecondaryBar;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.zombies.Zombies;
import com.rs.game.npc.Drop.Rarity;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.SlayerManager;
import com.rs.game.player.actions.HerbCleaning;
import com.rs.game.player.content.Assassins;
import com.rs.game.player.content.Burying;
import com.rs.game.player.content.DoubleXpManager;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.custom.ItemManager;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.darkinvasion.DarkInvasion;
import com.rs.game.player.controlers.dung.RuneDungGame;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
//import com.rs.utils.AdventureLog;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.Misc;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = -4794678936277614443L;

	private int id;
	private static int id1;
	private WorldTile respawnTile;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab
	// def, blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;

	// npc masks
	private transient Transformation nextTransformation;
	private transient SecondaryBar nextSecondaryBar;
	//name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}
	
	

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.setSpawned(spawned);
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		for (int i : Settings.NON_WALKING_NPCS1) {
            if (i == id)
                setRandomWalk(false);
            else
                setRandomWalk((getDefinitions().walkMask & 0x2) != 0
                        || forceRandomWalk(id));
        }
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextSecondaryBar != null || nextTransformation != null || changedCombatLevel || changedName;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}
	
	public void setNextNPCTransformation(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
		if (getCustomCombatLevel() != -1)
		    changedCombatLevel = true;
		if (getCustomName() != null)
		    changedName = true;
	    }
	
	public static void moo() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				String[] mooing = { "Moo", "Moooo", "MOOOOOOOOO", "derp", "Mooooooooo", "Neigh" };
				int i = Misc.random(1, 5);
				for (NPC n : World.getNPCs()) {
					if (!n.getName().equalsIgnoreCase("Cow")) {
						continue;
					}
					n.setNextForceTalk(new ForceTalk(mooing[i]));
				}
			}
		}, 0, 5); //time in seconds
	}

	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCBonuses.getBonuses(id);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
		nextSecondaryBar = null;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}
	
	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}
	
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}
	
	public static NPCCombatDefinitions getCombatDefinitions1() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id1);
	}


	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public int getId() {
		return id;
	}

	public void processNPC() {
		if (isDead() || locked)
			return;
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Utils.currentTimeMillis()) {
							if (((hasRandomWalk()) && World.getRotation(
									getPlane(), getX(), getY()) == 0) // temporary
									// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								}else 
									addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		//Changing npc combat levels
		 if (id == 15581) {//Party Demon
			 setCombatLevel(1500);
	        }
		 if (id == 3064) {//Lesser Demon Champion
			 setCombatLevel(900);
	        }
		 if (id == 3058) {//Giant Champion
			 setCombatLevel(800);
	        }
		 if (id == 3063) {//Jogre Champion
			 setCombatLevel(850);
	        }
		 if (id == 15187) {//TokHaar-Ket Champion
			 setCombatLevel(1100);
	        }
		 if (id == 10495) {//High level Lesser Demon
			 setCombatLevel(550);
	        }
		 if (id == 4706) {//High Level Moss giant
			 setCombatLevel(600);
	        }
		 if (id == 10769) {//High level ice giant
			 setCombatLevel(650);
	        }
		 if (id == 10717) {//High level Hill giant
			 setCombatLevel(550);
	        }
		 if (id == 10761) {//High level Fire giant
			 setCombatLevel(680);
	        }
		 if (id == 3450) {//High level Jogre
			 setCombatLevel(600);
	        }
		 if (id == 999) {//Doomion
			 setCombatLevel(900);
	        }
		 if (id == 998) {//Othainian
			 setCombatLevel(900);
	        }
		 if (id == 1000) {//Holthion
			 setCombatLevel(900);
	        }
		 if (id == 14550) {//Chronozon
			 setCombatLevel(950);
	        }
		 if (id == 14503) {//Agrith Naar
			 setCombatLevel(850);
	        }
		 if (id == 9356) {//Agrith Naar
			 setCombatLevel(100);
	        }
		//Renaming basic npcs
		
		
		 if (id == 220) {
			 
	           setName("Donator Points Shop");
	        }
		 if (id == 541) {
			 
	           setName("Donator Points Shop 2");
	        }
		 
		
		 if (id == 410)setName("Mysterious Gambler");
			if (id == 1694)setName("Range Supplies");
			if (id == 4519)setName("Banker");
			if (id == 13191)setName("Corporeal Beast Shop");
			if (id == 279)setName("Ticket Exchange");
			if (id == 5113)setName("Hunter Falconry");
			if (id == 8864)setName("Fishing Shop");
			if (id == 585)setName("Crafting Shop");
			if (id == 529)setName("General Store");
			if (id == 546)setName("Magic Supplies");
			if (id == 15811)setName("Skilling Supplies");
			if (id == 2253)setName("Prestige Master");
			if (id ==550 )setName("Ranging Shop");
			if (id == 576)setName("Food & Potion Shop");
			if (id == 14792)setName("Skill Cape Shop");
			if (id == 6539)setName("Vote Shop");
			if (id == 6970)setName("Summoning Shop");
			if (id == 1285) setName("Donator stores");
			if (id == 758)setName("Farming Shop");
			if (id == 11506) setName("General Korasi");
			if (id == 5112)setName("Hunter Shop");
			if (id == 9711)setName("Dungeoneering Rewards");
			if (id == 14165)setName("Tokkul Shop");
			if (id == 6537)setName("Pk/PvM Shop");
			if (id == 13727)setName("Loyalty Rewards");
			if (id == 6361)setName("Monster teleports");
			if (id == 11571)setName("Elite shop");
			if (id == 11577)setName("Elite shop");
			if (id == 15533)setName("Zamron Currency");
			if (id == 3709)setName("More teleports");
			if (id == 6988)setName("Summoning Shop");
			if (id == 1918)setName("Extreme Donator Donator Shop");
			if (id == 14854)setName("Extreme Donator Potions");
			if (id == 14811)setName("Combat Supplies");
			if (id == 8556)setName("DTRewards");
			if (id == 2904)setName("Account Guardian");
			if (id == 8085)setName("Dice Host");
	
		 //edit npc's movement
		
		if (isForceWalking()) {
			if (getFreezeDelay() < Utils.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps())
						addWalkSteps(forceWalk.getX(), forceWalk.getY(),
								getSize(), true);
					if (!hasWalkSteps()) { // failing finding route
						setNextWorldTile(new WorldTile(forceWalk)); // force
						// tele
						// to
						// the
						// forcewalk
						// place
						forceWalk = null; // so ofc reached forcewalk place
								if(id == 576)//Pure shop
							setRandomWalk(false); 
							if(id == 3021)//Pure shop
							setRandomWalk(false);
							if(id == 8085)//Pure shop
							setRandomWalk(false);
					}
				} else
					// walked till forcewalk place
					forceWalk = null;
			}
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0
				&& definitions.respawnDirection <= 8)
			return (4 + definitions.respawnDirection) << 11;
		return 0;
	}

	/*
	 * forces npc to random walk even if cache says no, used because of fake
	 * cache information
	 */
	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
		case 11226:
			return true;
		case 3341:
		case 3342:
		case 3343:
			return true;
		default:
			return false;
			/*
			 * default: return NPCDefinitions.getNPCDefinitions(npcId).name
			 * .equals("Icy Bones");
			 */
		}
	}
	
	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
							0);
			}
		}, 1);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) 
					sendSoulSplit(hit, p2);
				if (hit.getDamage() == 0)
					return;
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Attack from the enemy, boosting your Attack.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2215, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2231, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Strength from the enemy, boosting your Strength.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2248, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}

						}
					}
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2218, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2236, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2221, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2240, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}

					// overall

					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Utils.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets()
								.sendGameMessage(
										"Your opponent has been weakened so much that your leech curse has no effect.",
										true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets()
								.sendGameMessage(
										"Your curse drains Defence from the enemy, boosting your Defence.",
										true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							World.sendProjectile(p2, this, 2244, 35, 35, 20, 5,
									0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
		TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
				
				if (DoubleXpManager.isDoubleDrops()) {
					drop1();
				} else {
					drop();
				}
					reset();
					setLocation(respawnTile);
					finish();
					if (!isSpawned())
						setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	public void drop() {
		try {
			ArrayList<Drop> drops = NPCDrops.getDrops(id);	
			Player killer = getMostDamageReceivedSourcePlayer();
			if (killer == null)
				return;
				//String moof = "Has just slain";
				//AdventureLog.createConnection();
				//AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+killer.getUsername()+"','"+AdventureLog.Timer()+"','"+moof+"','"+this.getName()+"');");
				//System.out.println("[SQLMANAGER] Query Executed.");
				//AdventureLog.destroyConnection();
				//killer.npcs++;					
			if (onLowPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 10);
			killer.getPackets().sendGameMessage("You've been awarded 10 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			} else if (onMedPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 35);
			killer.getPackets().sendGameMessage("You've been awarded 35 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			} else if (onHighPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 100);
			killer.getPackets().sendGameMessage("You've been awarded 100 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			}		
			Player otherPlayer = killer.getSlayerManager().getSocialPlayer();
			SlayerManager manager = killer.getSlayerManager();
			if (manager.isValidTask(getName())) {
			    manager.checkCompletedTask(getDamageReceived(killer), otherPlayer != null ? getDamageReceived(otherPlayer) : 0);
			}
			Assassins manager2 = killer.getAssassinsManager();
			if (manager2.getTask() != null) {
			if (getId() == manager2.getNpcId()) {
				if (manager2.getGameMode() == 3) {
					if (manager2.getSpeed() <= 0) {
						manager2.resetTask();
						killer.sm("You have run out of time and can no longer complete your task.");
					} else {
						manager2.completeTask();
					}
				} else if (manager2.getGameMode() == 2) {
					if (killer.getEquipment().getWeaponId() == manager2.getWeapon()) {
						manager2.completeTask();
					} else {
						killer.sm("You must be using a "+manager2.getWeaponName()+" to kill this monster.");
					}
				} else {
						manager2.completeTask();
				}
			}
			}
			if (drops == null)
				return;
			if (killer.slayerTask.getTaskMonstersLeft() > 0) {
				for (String m : killer.slayerTask.getTask().slayable) {
					if (getDefinitions().name.equals(m)) {
						killer.slayerTask.onMonsterDeath(killer, this);
						break;
					}
				}
			}if (Utils.random(100) < 20) {
				Drop charm = new Drop(Rarity.UNCOMMON, com.rs.game.player.content.ItemConstants.charmIds[Utils.random(0, 4)], 1, 1);
				if (killer.getInventory().containsItem(10489, 1) && (killer.getInventory().getFreeSlots() >= 1)) {
					killer.getPackets().sendGameMessage("Your charm magnet picks up a " + ItemDefinitions.getItemDefinitions(charm.getItemId()).getName().toLowerCase() + ".", true);
					killer.getInventory().addItem(charm.getItemId(), 1);
				} else {
					sendDrop(killer, charm);
				}
			} if (killer.getGameMode() == 0) {
			Drop finalDrop = null;
			Drop rareDrop = null;
			int roll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();
			if (hasAccessToRareTable(killer)) {
				int rareroll = (Utils.random(5000));
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 25 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 75 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 250 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}	for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					sendDrop(killer, drop);
					//killer.getPackets().sendGameMessage("<col=FF0000>[DEBUG]</col> This drop is gamemode 0 rates");
				} else {
					if (roll < 75 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 150 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 275 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 1000 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null) {
				if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
					killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
				}
				sendDrop(killer, rareDrop);
			}if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				sendDrop(killer, finalDrop);
			} else if (killer.getGameMode() == 1) {
				Drop finalDrop = null;
			Drop rareDrop = null;
			int roll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();
			if (hasAccessToRareTable(killer)) {
				int rareroll = (Utils.random(5000));
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 125 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 250 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 325 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}	for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					sendDrop(killer, drop);
					//killer.getPackets().sendGameMessage("<col=FF0000>[DEBUG]</col> This drop is gamemode 1 rates");
				} else {
					if (roll < 175 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 300 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 425 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 1025 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null) {
				if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
					killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
				}
				sendDrop(killer, rareDrop);
			}if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				sendDrop(killer, finalDrop);
			} else if (killer.getGameMode() == 2) {
			Drop finalDrop = null;
			Drop rareDrop = null;
			int roll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();
			if (hasAccessToRareTable(killer)) {
				int rareroll = (Utils.random(5000));
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 25 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 75 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 450 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}	for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					sendDrop(killer, drop);
					//killer.getPackets().sendGameMessage("<col=FF0000>[DEBUG]</col> This drop is gamemode 2 rates");
				} else {
					if (roll < 250 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 350 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 550 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 1200 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null) {
				if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
					killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
				}
				sendDrop(killer, rareDrop);
			}if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				sendDrop(killer, finalDrop);
			}else if (killer.getGameMode() == 3) {
			Drop finalDrop = null;
			Drop rareDrop = null;
			int roll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();
			if (hasAccessToRareTable(killer)) {
				int rareroll = (Utils.random(5000));
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 25 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 75 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 250 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}	for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					sendDrop(killer, drop);
					//killer.getPackets().sendGameMessage("<col=FF0000>[DEBUG]</col> This drop is gamemode 3 rates");
				} else {
					if (roll < 250 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 350 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 680 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 2500 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null) {
				if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
					killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
				}
				sendDrop(killer, rareDrop);
			}if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				sendDrop(killer, finalDrop);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	
	public void drop1() {
		try {
			ArrayList<Drop> drops = NPCDrops.getDrops(id);
			
			Player killer = getMostDamageReceivedSourcePlayer();
			if (killer == null)
				return;
			/*
				String moof = "Has just slain";
				AdventureLog.createConnection();
				AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+killer.getUsername()+"','"+AdventureLog.Timer()+"','"+moof+"','"+this.getName()+"');");
				AdventureLog.destroyConnection();
				System.out.println("[SQLMANAGER] Query Executed.");				
			
				killer.npcs++;
				*/
			if (onLowPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 25);
			killer.getPackets().sendGameMessage("You've been awarded 25 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			} else if (onMedPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 70);
			killer.getPackets().sendGameMessage("You've been awarded 70 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			} else if (onHighPointsTable(getDefinitions().name)) {
			killer.setPvmPoints(killer.getPvmPoints() + 200);
			killer.getPackets().sendGameMessage("You've been awarded 200 PvM points for your effort, you now have "+ killer.getPvmPoints() + " Points.");
			}
			
			Player otherPlayer = killer.getSlayerManager().getSocialPlayer();
			SlayerManager manager = killer.getSlayerManager();
			if (manager.isValidTask(getName())) {
			    manager.checkCompletedTask(getDamageReceived(killer), otherPlayer != null ? getDamageReceived(otherPlayer) : 0);
			}
			Assassins manager2 = killer.getAssassinsManager();
			if (manager2.getTask() != null) {
			if (getId() == manager2.getNpcId()) {
				if (manager2.getGameMode() == 3) {
					if (manager2.getSpeed() <= 0) {
						manager2.resetTask();
						killer.sm("You have run out of time and can no longer complete your task.");
					} else {
						manager2.completeTask();
					}
				} else if (manager2.getGameMode() == 2) {
					if (killer.getEquipment().getWeaponId() == manager2.getWeapon()) {
						manager2.completeTask();
					} else {
						killer.sm("You must be using a "+manager2.getWeaponName()+" to kill this monster.");
					}
				} else {
						manager2.completeTask();
				}
			}
			}
			if (drops == null)
				return;

			// SlayerTask task = killer.getSlayerTask();
			if (killer.slayerTask.getTaskMonstersLeft() > 0) {
				for (String m : killer.slayerTask.getTask().slayable) {
					if (getDefinitions().name.equals(m)) {
						killer.slayerTask.onMonsterDeath(killer, this);
						break;
					}
				}
			}
			if (Utils.random(100) < 20) {
				Drop charm = new Drop(Rarity.UNCOMMON, com.rs.game.player.content.ItemConstants.charmIds[Utils.random(0, 4)], 1, 1);
				if (killer.getInventory().containsItem(10489, 1) && (killer.getInventory().getFreeSlots() >= 1)) {
					killer.getPackets().sendGameMessage("Your charm magnet picks up a " + ItemDefinitions.getItemDefinitions(charm.getItemId()).getName().toLowerCase() + ".", true);
					killer.getInventory().addItem(charm.getItemId(), 1);
				} else {
					sendDrop(killer, charm);
				}
			}

			Drop finalDrop = null;
			Drop rareDrop = null;

			int roll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();

			if (hasAccessToRareTable(killer)) {
				int rareroll = (Utils.random(5000));
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 150 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 300 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 800 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}

			for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					sendDrop(killer, drop);
				} else {
					if (roll < 250 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 350 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 650 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 2500 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}
			if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null) {
				if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
					killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
				}
				sendDrop(killer, rareDrop);
			}
			if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				sendDrop(killer, finalDrop);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}	


    public boolean onHighPointsTable(String npcName) {
	switch (npcName) {
	case "Queen black dragon":
	case "Queen_black_dragon":
	case "Queen Black Dragon":
	case "Tztok-Jad":
	case "Corporeal Beast":
	case "madara":
	case "Madara":
	case "Odischamp":
	case "odischamp":
	case "Firecapezorz":
	case "firecapezorz":
	case "FightpitPKer":
	case "fightpitpker":
	case "Nex":
	case "Hope devourer":
	case "Boss Korasi":
	case "Yk'Lagor the Thunderous":
	case "Mercenary mage":
	case "Har'Lakk the Riftsplitter":
	    return true;
	}
	return false;
    }

    public boolean onLowPointsTable(String npcName) {
	switch (npcName) {
	case "Dark beast":
	case "Ganodermic beast":
	case "Abyssal demon":
	case "Mutated jadinko male":
	case "Mutated jadinko guard":
	case "Green dragon":
	case "Frost dragon":
	case "Blue dragon":
	case "Red dragon":
	case "Steel dragon":
	case "Iron dragon":
	case "Dark_beast":
	case "Ganodermic_beast":
	case "Abyssal_demon":
	case "Mutated_jadinko_male":
	case "Mutated_jadinko_guard":
	case "Green_dragon":
	case "Frost_dragon":
	case "Blue_dragon":
	case "Red_dragon":
	case "Steel_dragon":
	case "Iron_dragon":
	case "Glacor":
	case "Skeletal Wyvern":
	    return true;
	}
	return false;
    }

    public boolean onMedPointsTable(String npcName) {
	switch (npcName) {
	case "Kree'arra":
	case "Tormented demon":
	case "Kalphite queen":
	case "King black dragon":
	case "WildyWyrm":
	case "Blink":
	case "General Graardor":
	case "Tormented_demon":
	case "Kalphite_queen":
	case "King Black Dragon":
	case "King_black_dragon":
	case "General_Graardor":
	case "Sunfreet":
	case "Leeuni":
	    return true;
	}
	return false;
    }
	
	public static boolean accessToRareTable(Player player, int npcId) {
		if (npcId == 13447 || npcId == 13448 || npcId == 13449 || npcId == 13450 || npcId == 6203 || npcId == 6222 || 
			npcId == 6247 || npcId == 6260 || npcId == 13216 || npcId == 11872 || npcId == 12900 || npcId == 1615 || npcId == 12878
			|| npcId == 8335 || npcId == 9911 || npcId == 3070)
			return true;
		int chance = Utils.getRandom(1000);
		boolean isSlayer = false;
		if (!isSlayer && NPCDefinitions.getNPCDefinitions(npcId).combatLevel < 30)
			return false;
		chance -= NPCDefinitions.getNPCDefinitions(npcId).combatLevel/4;
		if (player.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(player.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
			chance -= 10;
		}
		if (chance <= 10) {
			return true;
		}
		return false;
	}

	public boolean hasAccessToRareTable(Player killer) {
		int chance = Utils.getRandom(1000);
		boolean isSlayer = false;
		if (!isSlayer && getDefinitions().combatLevel < 30)
			return false;
		chance -= getDefinitions().combatLevel/4;
		if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
			chance -= 10;
		}
		if (chance <= 10) {
			if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth")) {
				killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
			}
			return true;
		}
		return false;
	}

	public void sendDrop(Player player, Drop drop) {
		int size = getSize();
		String dropName = ItemDefinitions.getItemDefinitions(drop.getItemId()).getName().toLowerCase();
		Item item;

		if(drop.getMaxAmount() > 1)
			item = new Item(drop.getItemId(), drop.getMinAmount()+Utils.getRandom(drop.getMaxAmount()));
		else
			item = new Item(drop.getItemId(), 1);

	//	if (drop.getRarity() == Rarity.RARE || drop.getRarity() == Rarity.VERYRARE)
	//		player.getPackets().sendGameMessage("DROPPED ITEM: Name: "+item.getDefinitions().getName()+" Rarity: "+drop.getRarity()+", Item: ("+drop.getItemId()+", "+item.getAmount()+")");

		if (item.getId() == 995) {
			player.getInventory().addItemMoneyPouch(item);
			return;
		}
		if (player.getInventory().containsItem(18337, 1)// Bonecrusher
				&& item.getDefinitions().getName().toLowerCase()
						.contains("bones")) {
			player.getSkills().addXp(Skills.PRAYER,
					Burying.Bone.forId(drop.getItemId()).getExperience());
			return;
		}
		if (player.getInventory().containsItem(19675, 1)// Herbicide
				&& item.getDefinitions().getName().toLowerCase()
						.contains("grimy")) {
			if (player.getSkills().getLevelForXp(Skills.HERBLORE) >= HerbCleaning.getHerb(item.getId()).getLevel()) {
				player.getSkills().addXp(Skills.HERBLORE, HerbCleaning.getHerb(drop.getItemId()) .getExperience() * 2);
				return;
			}
		}
		if (this.getId() == 6230 || this.getId() == 6231 || this.getId() == 6229
				|| this.getId() == 6232 || this.getId() == 6240 || this.getId() == 6241
				|| this.getId() == 6242 || this.getId() ==  6233 || this.getId() == 6234
				|| this.getId() == 6243 || this.getId() == 6244 || this.getId() == 6245
				|| this.getId() == 6246 || this.getId() == 6238 || this.getId() == 6239
				|| this.getId() == 6227 || this.getId() == 6625 || this.getId() == 6223
				|| this.getId() == 6222) {
			player.armadyl++;
	        player.getPackets().sendIComponentText(601, 8,  ""+ player.armadyl +"");
		}
		if (this.getId() == 6278 || this.getId() == 6277 || this.getId() == 6276
				|| this.getId() == 6283 || this.getId() == 6282 || this.getId() == 6280
				|| this.getId() == 6281 || this.getId() == 6279 || this.getId() == 6275
				|| this.getId() == 6271 || this.getId() == 6272 || this.getId() == 6273
				|| this.getId() == 6274 || this.getId() == 6269 || this.getId() ==  6270
				|| this.getId() == 6268 || this.getId() == 6265 || this.getId() == 6263
				|| this.getId() == 6261 || this.getId() == 6260) {
			player.bandos++;
			player.getPackets().sendIComponentText(601, 9,  ""+ player.bandos +"");
		}
		if (this.getId() == 6257 || this.getId() == 6255 || this.getId() == 6256
				|| this.getId() == 6258 || this.getId() == 6259 || this.getId() == 6254
				|| this.getId() == 6252 || this.getId() == 6250 || this.getId() == 6248
				|| this.getId() == 6247) {
			player.saradomin++;
			 player.getPackets().sendIComponentText(601, 10,  ""+ player.saradomin +"");
		}
		if (this.getId() == 6221 || this.getId() == 6219 || this.getId() == 6220
				|| this.getId() == 6217 || this.getId() == 6216 || this.getId() == 6215
				|| this.getId() == 6214 || this.getId() == 6213 || this.getId() == 6212
				|| this.getId() == 6211 || this.getId() == 6218 || this.getId() == 6208
				|| this.getId() == 6206 || this.getId() == 6204 || this.getId() == 6203) {
			player.zamorak++;
			player.getPackets().sendIComponentText(601, 11,  ""+ player.zamorak +"");
		}
		/*LootShare/CoinShare*/
		FriendChatsManager fc = player.getCurrentFriendChat();
		if(player.lootshareEnabled()) {
			if(fc != null) {
				CopyOnWriteArrayList<Player> players = fc.getPlayers();
				CopyOnWriteArrayList<Player> playersWithLs = new CopyOnWriteArrayList<Player>();
				for(Player p : players) {
					if(p.lootshareEnabled() && p.getRegionId() == player.getRegionId()) //If players in FC have LS enabled and are also in the same map region.
						playersWithLs.add(p);
				}
			//	if (ItemManager.getPrice(item.getId()) >= 1000000) {
			//	int playeramount = playersWithLs.size();
			//	int dividedamount = (ItemManager.getPrice(item.getId()) / playeramount);
			//	for(Player p : playersWithLs) {
			//		p.getInventory().addItemMoneyPouch(new Item(995, dividedamount));
			//		p.sendMessage(String.format("<col=115b0d>You received: %sx coins from a split of the item %s.</col>", dividedamount, dropName));
			//		return;
			//	}
			//	} else {
				Player luckyPlayer = playersWithLs.get((int)(Math.random()*playersWithLs.size())); //Choose a random player to get the drop.
				World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), luckyPlayer, true, 180);
				luckyPlayer.sendMessage(String.format("<col=115b0d>You received: %sx %s.</col>", item.getAmount(), dropName));
				for(Player p : playersWithLs) {
					if(!p.equals(luckyPlayer))
					p.sendMessage(String.format("%s received: %sx %s.", luckyPlayer.getDisplayName(), item.getAmount(), dropName));
				}
			//	}
				return;
			}
		} 
		/*End of LootShare/CoinShare*/
		player.npcLog(player, item.getId(), item.getAmount(), item.getName(), this.getName(), this.getId());

		if (!player.isIronMan) {
			World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
					getCoordFaceY(size), getPlane()), player, true, 60);
		} else {
			World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
					getCoordFaceY(size), getPlane()), player, true, 1800000000);
		}
if (dropName.contains("pernix") || dropName.contains("torva")
				|| dropName.contains("virtus") || dropName.contains("bandos")
				|| dropName.contains("hilt")
				|| dropName.contains("hati") || dropName.contains("korasi")
				|| dropName.contains("divine")
				|| (dropName.contains("saradomin")  && !dropName.contains("brew"))
				|| (dropName.contains("fire")  && !dropName.contains("staff") && !dropName.contains("rune") && !dropName.contains("orb") && !dropName.contains("talisman"))
				|| dropName.contains("visage")
				|| dropName.contains("zamorakian")
				|| dropName.contains("spectral")
				|| dropName.contains("elysian")
				|| dropName.contains("steadfast")
				|| dropName.contains("armadyl chest")
				|| dropName.contains("armadyl plate")
				|| dropName.contains("armadyl boots")
				|| dropName.contains("armadyl helmet")
				|| dropName.contains("armadyl gloves")
				|| dropName.contains("armadyl_chest")
				|| dropName.contains("armadyl_plate")
				|| dropName.contains("armadyl_boots")
				|| dropName.contains("armadyl_helmet")
				|| dropName.contains("armadyl_gloves")
				|| dropName.contains("armadyl_chainskirt")
				|| dropName.contains("armadyl chainskirt")
				|| dropName.contains("buckler")
				|| dropName.contains("glaiven")
				|| dropName.contains("ragefire")
				|| dropName.contains("spirit shield")
				|| dropName.contains("spirit_shield")
				|| dropName.contains("elixer")
				|| dropName.contains("fury")
				|| dropName.contains("arcane")
				|| dropName.contains("edict")
				|| dropName.contains("sword of")
				|| dropName.contains("ice")
				|| dropName.contains("godsword")
				|| dropName.contains("vine")
				|| dropName.contains("goliath")
				|| dropName.contains("swift")
				|| dropName.contains("spellcaster")
				|| dropName.contains("gorgonite")
				|| dropName.contains("promethium")
				|| dropName.contains("primal")
				|| dropName.contains("polypore_stick")
				|| dropName.contains("polypore stick")
				|| dropName.contains("ganodermic gloves")
				|| dropName.contains("ganodermic_gloves")
				|| dropName.contains("ganodermic boots")
				|| dropName.contains("ganodermic_boots")
				|| dropName.contains("vesta")
				|| dropName.contains("statius")
				|| dropName.contains("zuriel")
				|| dropName.contains("morrigan")
				|| dropName.contains("crystal_key")
				|| dropName.contains("crystal key")
				|| dropName.contains("clue")
				|| dropName.contains("deathtouched")
				|| dropName.contains("dragon_chain")
				|| dropName.contains("dragon chain")
				|| dropName.contains("dragon_full")
				|| dropName.contains("dragon full")
				|| dropName.contains("dragon_kite")
				|| dropName.contains("dragon kite")
				|| dropName.contains("dragon_rider")
				|| dropName.contains("dragon rider")
				|| dropName.contains("inferno")
				|| dropName.contains("gilded")
				|| dropName.contains("mask")
				|| dropName.contains("chaotic")) {
	
			World.sendWorldMessage("<col=ff8c38><img=6>News: "+ player.getDisplayName() + " has just received " + dropName + " as a " +drop.getRarity()+ " drop! </col> ", false);		
			String moof1 = "has recieved a";
			/* try {
				AdventureLog.createConnection();
				AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"','"+dropName+"');");
				System.out.println("[SQLMANAGER] Query Executed.");
				AdventureLog.destroyConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
		}
		
	}
	
	public static Item dropToItem(Drop drop) {
		Item item = null;
		if(drop.getMaxAmount() > 1)
			item = new Item(drop.getItemId(), drop.getMinAmount()+Utils.getRandom(drop.getMaxAmount()));
		else
			item = new Item(drop.getItemId(), 1);
		return item;
	}
	
	public static void sendDropDirectlyToBank(Player player, Drop drop) {
		Item item = null;
		if(drop.getMaxAmount() > 1)
			item = new Item(drop.getItemId(), drop.getMinAmount()+Utils.getRandom(drop.getMaxAmount()));
		else
			item = new Item(drop.getItemId(), 1);
		player.getBank().addItem(item.getId(), item.getAmount(), true);
	}
	
	public static void displayDropsFor(Player player, int npcId, int npcAmount) {
		ItemsContainer<Item> dropCollection = new ItemsContainer<Item>(506, true);
		
		for (int i = 0;i < npcAmount;i++) {
			ArrayList<Drop> drops = NPCDrops.getDrops(npcId);
			if (drops == null || drops.isEmpty())
				return;
			
			if (Utils.random(100) < 20) {
				Drop charm = new Drop(Rarity.UNCOMMON, com.rs.game.player.content.ItemConstants.charmIds[Utils.random(0, 4)], 1, 1);
				dropCollection.add(dropToItem(charm));
			}
			
			Drop finalDrop = null;
			Drop rareDrop = null;
	
			int roll = (Utils.random(5000));
			int rareroll = (Utils.random(5000));
			ArrayList<Drop> possibleDrops = new ArrayList<Drop>();
			ArrayList<Drop> rareDropTable = new ArrayList<Drop>();
			
			if (accessToRareTable(player, npcId)) {
				ArrayList<Drop> rareTable = NPCDrops.getDrops(50000);
				for (Drop rare : rareTable) {
					if (rareroll < 25 && rare.getRarity() == Rarity.ULTRARARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 75 && rare.getRarity() == Rarity.VERYRARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 250 && rare.getRarity() == Rarity.RARE) {
						rareDropTable.add(rare);
					} else if (rareroll < 1000 && rare.getRarity() == Rarity.UNCOMMON) {
						rareDropTable.add(rare);
					} else if (rareroll < 4500 && rare.getRarity() == Rarity.COMMON) {
						rareDropTable.add(rare);
					}
				}
			}
	
			for (Drop drop : drops) {
				if (drop.getRarity() == Rarity.ALWAYS) {
					dropCollection.add(dropToItem(drop));
				} else {
					if (roll < 25 && drop.getRarity() == Rarity.ULTRARARE) {
						possibleDrops.add(drop);
					} else if (roll < 75 && drop.getRarity() == Rarity.VERYRARE) {
						possibleDrops.add(drop);
					} else if (roll < 250 && drop.getRarity() == Rarity.RARE) {
						possibleDrops.add(drop);
					} else if (roll < 1000 && drop.getRarity() == Rarity.UNCOMMON) {
						possibleDrops.add(drop);
					} else if (roll < 4500 && drop.getRarity() == Rarity.COMMON) {
						possibleDrops.add(drop);
					}
				}
			}
			if (possibleDrops.size() > 0)
				finalDrop = possibleDrops.get(Utils.random(possibleDrops.size()));
			if (finalDrop != null)
				dropCollection.add(dropToItem(finalDrop));
			if (rareDropTable.size() > 0)
				rareDrop = rareDropTable.get(Utils.random(rareDropTable.size()));
			if (rareDrop != null)
				dropCollection.add(dropToItem(rareDrop));
		}
		
		player.getTemporaryAttributtes().put("viewingOtherBank", Boolean.TRUE);
		player.getInterfaceManager().sendInterface(762);
		player.getPackets().sendItems(95, dropCollection);
		player.getPackets().sendIComponentSettings(762, 95, 0, 516, 2622718);
		player.getPackets().sendIComponentSettings(763, 0, 0, 27, 2425982);
		player.getPackets().sendConfigByFile(4893, 1);
	}



	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0;
	}

	public WorldTile getRespawnTile() {
		return respawnTile;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget()
				&& !(combat.getTarget() instanceof Familiar))
			lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking()) // if force walk not gonna get target
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player
							.withinDistance(
									this,
									forceTargetDistance > 0 ? forceTargetDistance
											: (getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
															: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 64
																	: 8))
																	|| (!forceMultiAttacked
																			&& (!isAtMultiArea() || !player
																					.isAtMultiArea())
																					&& player.getAttackedBy() != this && (player
																							.getAttackedByDelay() > Utils.
																							currentTimeMillis() || player
																							.getFindTargetDelay() > Utils
																							.currentTimeMillis()))
																							|| !clipedProjectile(player, false)
																							|| (!forceAgressive && !Wilderness.isAtWild(this) && immunity(player)))
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}
	
	public boolean immunity(Player player) {
		if (player.stealth) {
		if (player.getSkills().getAssassinLevel(Skills.STEALTH_MOVES) <= 10) {
			if (player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2)
				return true;
			else
				return false;
		} else if (player.getSkills().getAssassinLevel(Skills.STEALTH_MOVES) >= 11 && player.getSkills().getAssassinLevel(Skills.STEALTH_MOVES) <= 98) {
			int level = player.getSkills().getCombatLevelWithSummoning() + (player.getSkills().getAssassinLevel(Skills.STEALTH_MOVES)*2);
			if (level >= getCombatLevel() * 2)
				return true;
			else
				return false;
		} else {
			return true;
		}
		} else {
			if (player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2)
				return true;
			else
				return false;
		}
	}

	public boolean checkAgressivity() {
		// if(!(Wilderness.isAtWild(this) &&
		// getDefinitions().hasAttackOption())) {
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
				return false;
		}
		// }
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().name + " - " + id + " - " + getX() + " "
				+ getY() + " " + getPlane();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		this.randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		this.name = getDefinitions().name.equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	public String getName() {
		return name != null ? name : getDefinitions().name;
	}
	
	public String getName2() {
		return getDefinitions().name != null ? getDefinitions().name : name;
	}

	public void setCombatLevel(int level) {
		combatLevel  = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return new WorldTile(getCoordFaceX(size),getCoordFaceY(size), getPlane());
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}
	
	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
    public SecondaryBar getNextSecondaryBar() {
	return nextSecondaryBar;
    }

    public void setNextSecondaryBar(SecondaryBar secondaryBar) {
	this.nextSecondaryBar = secondaryBar;
    }
	
    public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
	int size = getSize();
	//int agroRatio = getCombatDefinitions().getAgroRatio();
	ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
	for (int regionId : getMapRegionsIds()) {
	    if (checkPlayers) {
		List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
		if (playerIndexes != null) {
		    for (int playerIndex : playerIndexes) {
			Player player = World.getPlayers().get(playerIndex);
			if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || player.getAppearence().isHidden() || !Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(), player.getSize(), forceTargetDistance) || (!forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea()) && (player.getAttackedBy() != this && (player.getAttackedByDelay() > Utils.currentTimeMillis() || player.getFindTargetDelay() > Utils.currentTimeMillis()))) || !clipedProjectile(player, false) || (!forceAgressive && !Wilderness.isAtWild(this) && player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2))
			    continue;
			possibleTarget.add(player);
		    }
		}
	    }
	    if (checkNPCs) {
		List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
		if (npcsIndexes != null) {
		    for (int npcIndex : npcsIndexes) {
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc == this || npc.isDead() || npc.hasFinished() || !Utils.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(), forceTargetDistance) || !npc.getDefinitions().hasAttackOption() || ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this && npc.getAttackedByDelay() > Utils.currentTimeMillis()) || !clipedProjectile(npc, false))
			    continue;
			possibleTarget.add(npc);
		    }
		}
	    }
	}
	return possibleTarget;
    }



    public void lowerDefence(float multiplier) {
		if (bonuses != null) {
			int[] realBonuses = NPCBonuses.getBonuses(id);
			if (realBonuses != null) {
				if (bonuses[5] > realBonuses[5]/3)
					bonuses[5] -= bonuses[5]*multiplier;
				if (bonuses[6] > realBonuses[6]/3)
					bonuses[6] -= bonuses[6]*multiplier;
				if (bonuses[7] > realBonuses[7]/3)
					bonuses[7] -= bonuses[7]*multiplier;
				if (bonuses[8] > realBonuses[8]/3)
					bonuses[8] -= bonuses[8]*multiplier;
				if (bonuses[9] > realBonuses[9]/3)
					bonuses[9] -= bonuses[9]*multiplier;
			}
		}
	}
    
}
