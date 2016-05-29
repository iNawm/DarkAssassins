package com.rs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;




public class Panel extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -576896006401515960L;
	
	private static JTextArea staffOnline;
	private static JTextArea playersOnline;
	private static JTextArea prints;
	private static JTextArea playerChat;
	
	private JScrollPane printScroll;
	private JScrollPane chatScroll;
	
	private JButton shutdown;
	private JButton worldMessage;
	private JButton Item;
	private JButton Unlock;
	private JButton mute;
	private JButton Prestige;
	private JButton Points;
	private JButton Donator;
	private JButton Extreme;
	private JButton TItems;
	private JButton Support;
	private JButton PNPC;
	private JButton unmute;
	private JButton kick;
	private JButton jail;
	private JButton unjail;
	private JButton setRights;
	private JButton master;
	
	private static boolean firstLine;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
		}
		new Panel();
	}
	
	Panel() {
		super("Zamron Control GUI");
		sendStaffOnline();
		sendPlayersOnline();
		sendServerPrints();
		sendPlayerChat();
		sendButtons();
		sendUI(this);
	}

	private void sendStaffOnline() {
		staffOnline = new JTextArea("Staff Online: \n");
		staffOnline.setBounds(10, 10, 150, 360);
		staffOnline.setEditable(false);
		add(staffOnline);
	}
	
	private void sendPlayersOnline() {
		playersOnline = new JTextArea("Players Online: 0");
		playersOnline.setBounds(10, 380, 150, 22);
		playersOnline.setEditable(false);
		add(playersOnline);
	}
	
	
	private void sendServerPrints() {
		prints = new JTextArea("Awaiting printed lines... \n");
		printScroll = new JScrollPane(prints);
		printScroll.setBounds(170, 230, 612, 177);
		prints.setEditable(false);
		add(printScroll);
	}
	
	private void sendPlayerChat() {
		playerChat = new JTextArea("Player Chat Log: \n");
		chatScroll = new JScrollPane(playerChat);
		chatScroll.setBounds(170, 10, 300, 215);
		playerChat.setEditable(false);
		add(chatScroll);
	}
	
	private void sendButtons() {
		shutdown = new JButton("Shutdown");
		shutdown.setBounds(480, 10, 90, 25);
		shutdown.addActionListener(this);
		add(shutdown);
		
		worldMessage = new JButton("World Message");
		worldMessage.setBounds(480, 45, 90, 25);
		worldMessage.addActionListener(this);
		add(worldMessage);
		
		mute = new JButton("Mute Player");
		mute.setBounds(480, 80, 90, 25);
		mute.addActionListener(this);
		add(mute);
		
		unmute = new JButton("Unmute Player");
		unmute.setBounds(480, 115, 90, 25);
		unmute.addActionListener(this);
		add(unmute);
		
		kick = new JButton("Kick Player");
		kick.setBounds(480, 150, 90, 25);
		kick.addActionListener(this);
		add(kick);
		
		Points = new JButton("Points Player");
		Points.setBounds(480, 185, 90, 25);
		Points.addActionListener(this);
		add(Points);
		
		jail = new JButton("Jail Player");
		jail.setBounds(580, 10, 90, 25);
		jail.addActionListener(this);
		add(jail);
		
		unjail = new JButton("UnJail Player");
		unjail.setBounds(580, 45, 90, 25);
		unjail.addActionListener(this);
		add(unjail);
		
		setRights = new JButton("Drop Log");
		setRights.setBounds(580, 80, 90, 25);
		setRights.addActionListener(this);
		add(setRights);
		
		master = new JButton("Master Player");
		master.setBounds(580, 115, 90, 25);
		master.addActionListener(this);
		add(master);
		
		Item = new JButton("Give Item");
		Item.setBounds(580, 150, 90, 25);
		Item.addActionListener(this);
		add(Item);
		
		Unlock = new JButton("Unlock");
		Unlock.setBounds(680, 10, 90, 25);
		Unlock.addActionListener(this);
		add(Unlock);
		
		Prestige = new JButton("Set Prestige");
		Prestige.setBounds(680, 45, 90, 25);
		Prestige.addActionListener(this);
		add(Prestige);
		
		Donator = new JButton("Give Donator");
		Donator.setBounds(680, 80, 90, 25);
		Donator.addActionListener(this);
		add(Donator);
		
		Extreme = new JButton("Give Extreme");
		Extreme.setBounds(680, 115, 90, 25);
		Extreme.addActionListener(this);
		add(Extreme);
		
		Support = new JButton("Support player");
		Support.setBounds(680, 150, 90, 25);
		Support.addActionListener(this);
		add(Support);
		
		TItems = new JButton("Take Items player");
		TItems.setBounds(580, 185, 90, 25);
		TItems.addActionListener(this);
		add(TItems);
		
				
		PNPC = new JButton("PNPC");
		PNPC.setBounds(680, 185, 90, 25);
		PNPC.addActionListener(this);
		add(PNPC);
	}

	private void sendUI(Panel ui) {
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setResizable(false);
		ui.setSize(800, 450);
		ui.setLayout(null);
		ui.setLocationRelativeTo(null);
		ui.setVisible(true);
	}
	
	public static void listenForPlayersOnline() {
		playersOnline.setText("Players Online: " + Integer.toString(World.getPlayers().size()));
	}
	
	public static void listenForStaffOnline() {
		staffOnline.setText("Staff Online: \n");
		for (Player player : World.getPlayers()) {
			if (player.getRights() >= 1) {
				staffOnline.append(player.getUsername() + "\n");
			}
		}
	}
	
	public static void listenForChat(String username, String chat) {
		playerChat.append(username + ": " + chat + "\n");
	}
	
	public static void listenForPrintedLines(String lines) {
		if (firstLine) {
			prints.append(lines + "\n");
			return;
		}
		prints.setText(lines + "\n");
		firstLine = true;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == shutdown) {
			String delay = JOptionPane.showInputDialog(null, "How many seconds until shutdown?", 
					"P-S Server Shutdown", JOptionPane.PLAIN_MESSAGE);
			World.safeShutdown(false, Integer.parseInt(delay));
		} else if (e.getSource() == worldMessage) {
			String message = JOptionPane.showInputDialog(null, "Please enter your message.", 
					"P-S World Message", JOptionPane.PLAIN_MESSAGE);
			World.sendWorldMessage("<col=FF0000>[SYSTEM]: " + message + "</col>", false);
		} else if (e.getSource() == mute) {
			String username = JOptionPane.showInputDialog(null, "Please enter your username.", 
					"P-S Mute Player", JOptionPane.PLAIN_MESSAGE);
			String victimUsername = JOptionPane.showInputDialog(null, "Please enter the username of the victim.", 
					"P-S Mute Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(victimUsername);
			if (target != null) {
				target.setMuted(Utils.currentTimeMillis()
						+ ( (48 * 60 * 60 * 1000)));
				target.getPackets().sendGameMessage(
						"You've been muted for 48 hours by " + username + ".");
			} else {
				victimUsername = Utils.formatPlayerNameForProtocol(victimUsername);
				if(!SerializableFilesManager.containsPlayer(victimUsername)) {
					Logger.log("Mute System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " doesn't exist.");
					return;
				}
				Logger.log("Mute System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " is not online.");
			}
		} else if (e.getSource() == unmute) {
			String username = JOptionPane.showInputDialog(null, "Please enter your username.", 
					"P-S Unmute Player", JOptionPane.PLAIN_MESSAGE);
			String victimUsername = JOptionPane.showInputDialog(null, "Please enter the username of the victim.", 
					"P-S Unmute Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(victimUsername);
			if (target != null) {
				target.setMuted(0);
				target.getPackets().sendGameMessage(
						"You've been unmuted by " + username + ".");
				SerializableFilesManager.savePlayer(target);
			} else {
				victimUsername = Utils.formatPlayerNameForProtocol(victimUsername);
				if(!SerializableFilesManager.containsPlayer(victimUsername)) {
					Logger.log("Mute System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " doesn't exist.");
					return;
				}
				Logger.log("Mute System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " is not online.");
			}
		} else if (e.getSource() == kick) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username of the player to kick.", 
					"P-S Kick Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			target = World.getPlayerByDisplayName(username);
			if (target == null) {
				Logger.log("Mute System", Utils.formatPlayerNameForDisplay(username) + " is not logged in.");
				return;
			}
			target.getSession().getChannel().close();
			Logger.log("Mute System", "Kicked player: " + target.getDisplayName() + ".");
		} else if (e.getSource() == jail) {
			String username = JOptionPane.showInputDialog(null, "Please enter your username.", 
					"P-S Jail Player", JOptionPane.PLAIN_MESSAGE);
			String victimUsername = JOptionPane.showInputDialog(null, "Please enter the username of the victim.", 
					"P-S Jail Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(victimUsername);
			if (target != null) {
				target.setJailed(Utils.currentTimeMillis()
						+ (24 * 60 * 60 * 1000));
				target.getControlerManager()
					.startControler("JailControler");
				target.getPackets().sendGameMessage(
						"You've been Jailed for 24 hours by " + username +".");
				SerializableFilesManager.savePlayer(target);
			} else {
				victimUsername = Utils.formatPlayerNameForProtocol(victimUsername);
				if(!SerializableFilesManager.containsPlayer(victimUsername)) {
					Logger.log("Jail System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " doesn't exist.");
					return;
				}
				Logger.log("Jail System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " is not online.");
			}
		} else if (e.getSource() == unjail) {
			String username = JOptionPane.showInputDialog(null, "Please enter your username.", 
					"P-S UnJail Player", JOptionPane.PLAIN_MESSAGE);
			String victimUsername = JOptionPane.showInputDialog(null, "Please enter the username of the victim.", 
					"P-S UnJail Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(victimUsername);
			if (target != null) {
				target.setJailed(0);
				target.getControlerManager()
					.startControler("JailControler");
				target.getPackets().sendGameMessage(
						"You've been Jailed for 24 hours by " + username +".");
				SerializableFilesManager.savePlayer(target);
			} else {
				victimUsername = Utils.formatPlayerNameForProtocol(victimUsername);
				if(!SerializableFilesManager.containsPlayer(victimUsername)) {
					Logger.log("Jail System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " doesn't exist.");
					return;
				}
				Logger.log("Jail System", "Account name " + Utils.formatPlayerNameForDisplay(victimUsername) + " is not online.");
			}
		} else if (e.getSource() == setRights) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to remove items.",  "P-S remove items Player", JOptionPane.PLAIN_MESSAGE);
			String id = JOptionPane.showInputDialog("Item Id");
			World.sendWorldMessage("<img=6><col=FF0000>News: " + username + " has received a " + id + " as a rare drop!", false);
			
		}else if (e.getSource() == Item) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give an item.", 
					"P-S Give item Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				String id = JOptionPane.showInputDialog("Item Id");
				String quantity = JOptionPane.showInputDialog("Item Amount");
				int item = Integer.parseInt(id);
				int amount = Integer.parseInt(quantity);
				target.getInventory().addItem(item, amount);
				System.out.println("Console: Given Item To "
						+ username + ".");
				JOptionPane.showMessageDialog(null, "Given Item To "+username, "Console", JOptionPane.PLAIN_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Unlock) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to unlock.", 
					"P-S Unlock Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				target.setCompletedFightCaves();
				World.sendWorldMessage("<img=6><col=ff8c38>News: "+target.getDisplayName()+" Just completed fight caves.", true);
				target.setCompletedFightKiln();
				World.sendWorldMessage("<img=6><col=ff8c38>News: "+target.getDisplayName()+" Just completed Fightkiln.", true);	
	
				target.domCount = 150;
				target.penguins = 15;
				target.sinkholes = 5; 
				 target.totalTreeDamage = 1000; 
				 target.barrowsLoot = 55;
				target.rfd5 = true; 
				target.prestigeNumber = 5; 
				 target.implingCount = 120; 

				 target.killedQueenBlackDragon2 = true; 
				 target.advancedagilitylaps = 100; 
				 target.heroSteals = 150;
				target.cutDiamonds = 500;
				 target.barCrawlCompleted = true;  
				 target.runiteOre = 50;
				  target.cookedFish = 500; 
				  target.burntLogs = 150;
				 target.choppedIvy = 150;
				 target.infusedPouches = 300;  
				 target.crystalChest = 20;				
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Prestige) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to Prestige.", 
					"P-S Prestige Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				String points = JOptionPane.showInputDialog("Prestige Level");
				int ammount = Integer.parseInt(points);
				target.SetprestigePoints(ammount);
				System.out.println("Console: Given Prestige To " + username + ".");
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Donator) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give donator", 
					"P-S Donator Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				target.setDonator(true);
				target.setRights(0);
				SerializableFilesManager.savePlayer(target);
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Extreme) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give Extreme.", 
					"P-S Extreme Donator", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				target.setDonator(true);
				target.setExtremeDonator(true);
				target.setRights(0);
				SerializableFilesManager.savePlayer(target);
				
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Support) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give Supporter.", 
					"P-S Support Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				target.isForumModerator = true;
				SerializableFilesManager.savePlayer(target);
				
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == PNPC) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give Supporter.", 
					"P-S PNPC Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				String rawr = JOptionPane.showInputDialog("NPC ID");
				int npcid = Integer.parseInt(rawr);
				target.getAppearence().transformIntoNPC(npcid);
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == Points) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to give points to.", 
					"P-S Master Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				String Spoints = JOptionPane.showInputDialog("Slayer Points");
				String Dpoints = JOptionPane.showInputDialog("Dung Tokens");
				String Lpoints = JOptionPane.showInputDialog("Loyalty Points");
				String Vpoints = JOptionPane.showInputDialog("Vote Coins");
				String PKpoints = JOptionPane.showInputDialog("PK Points");
				String pvM = JOptionPane.showInputDialog("PvM Points");
				String Lap = JOptionPane.showInputDialog("Laps");
				String triv = JOptionPane.showInputDialog("Trivia");
				int ammountS = Integer.parseInt(Spoints);
				int ammountD = Integer.parseInt(Dpoints);
				int ammountL = Integer.parseInt(Lpoints);
				int ammountV = Integer.parseInt(Vpoints);
				int ammountPK = Integer.parseInt(PKpoints);
				int ammountPM = Integer.parseInt(Lap);
				int ammountLP = Integer.parseInt(Lap);
				int trivvia = Integer.parseInt(triv);
				target.setSlayerPoints(ammountS);
				target.setDungTokens(ammountD);
				target.setLoyaltyPoints(ammountL);
				target.setPvmPoints(ammountPM);
				target.laPs(ammountLP);
				target.laps1(trivvia);
				target.setTriviaPoints(ammountLP);
				target.getInventory().addItem(18201, ammountV);
				target.getInventory().addItem(12852, ammountPK);
				System.out.println("Console: Given Points To " + username + ".");
			} else {
				JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
				System.out.println("Console: "
					+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
				}
		}else if (e.getSource() == TItems) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to remove items.", 
					"P-S remove items Player", JOptionPane.PLAIN_MESSAGE);
	Player target = World.getPlayerByDisplayName(username);
	if (target != null) {
		String id = JOptionPane.showInputDialog("Item Id");
		String quantity = JOptionPane.showInputDialog("Item Amount");
		int item = Integer.parseInt(id);
		int amount = Integer.parseInt(quantity);
		target.getInventory().deleteItem(item, amount);
		System.out.println("Console: Taken Item "+item+" From "
				+ username + ".");
		JOptionPane.showMessageDialog(null, "Taken Item "+item+" From "+username, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, username+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: "
			+ Utils.formatPlayerNameForDisplay(username) + " Doesn't Exist!");
		}
		} else if (e.getSource() == master) {
			String username = JOptionPane.showInputDialog(null, "Please enter the username you wish to master.", 
					"P-S Master Player", JOptionPane.PLAIN_MESSAGE);
			Player target = World.getPlayerByDisplayName(username);
			if (target != null) {
				for (int skill = 0; skill < 25; skill++)
					target.getSkills().addXp(skill, 150000000);
				SerializableFilesManager.savePlayer(target);
			} else {
				username = Utils.formatPlayerNameForProtocol(username);
				if(!SerializableFilesManager.containsPlayer(username)) {
					Logger.log("Master", "Account name " + Utils.formatPlayerNameForDisplay(username) + " doesn't exist.");
					return;
				}
				Logger.log("Master", "Account name " + Utils.formatPlayerNameForDisplay(username) + " is not online.");
			}
		}
		
	}

}
