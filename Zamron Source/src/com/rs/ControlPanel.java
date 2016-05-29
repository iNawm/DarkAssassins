package com.rs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.rs.Launcher;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.custom.ItemManager;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.tools.ItemBonusesPacker;
import com.rs.utils.Censor;
import com.rs.utils.IPBanL;
import com.rs.utils.IPMute;
import com.rs.utils.ItemExamines;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCExamines;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.WeightManager;

/**
 * 
 * @author Cody
 *
 */

public class ControlPanel extends JFrame {

	private static final long serialVersionUID = -421120533203117621L;

	private Player player;

	private final JPanel contentPane;

	private static boolean showingOffline;
	private boolean showingObjects = false;
	private boolean showingItems = false;

	private static DefaultListModel<String> playersModel = new DefaultListModel<>();
	private static DefaultListModel<String> loggingModel = new DefaultListModel<>();
	private static DefaultListModel<String> hsPlayersList = new DefaultListModel<>();
	private static DefaultListModel<String> npcListModel = new DefaultListModel<>();

	private final JList<String> playerList = new JList<>(playersModel);
	private final JList<String> logger = new JList<>(loggingModel);
	private final JList<String> hsList = new JList<>(hsPlayersList);
	private final JList<String> npcList = new JList<>(npcListModel);

	private final JTextField usernameField = new JTextField();
	private final JTextField hoursField = new JTextField();

	private static final String IMAGE_PATH = "./data/icons/";
	private JTextField textField;
	private JTextField announcementsTextField;

	public static void init() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final ControlPanel frame = new ControlPanel();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ControlPanel() {
		setResizable(false);
		setTitle("Control Panel");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 472);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		final JPanel mainMenu = new JPanel();
		tabbedPane.addTab("Main menu", null, mainMenu, null);
		mainMenu.setLayout(null);

		final JLabel playersText = new JLabel("Players: ");
		playersText.setHorizontalAlignment(SwingConstants.CENTER);
		playersText.setBounds(24, 5, 94, 14);
		mainMenu.add(playersText);

		final JLabel lblLogger = new JLabel("Logger:");
		lblLogger.setBounds(145, 288, 46, 14);
		mainMenu.add(lblLogger);

		final JScrollPane playerScrollList = new JScrollPane();
		playerScrollList.setBounds(14, 57, 121, 327);
		mainMenu.add(playerScrollList);
		playerList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				usernameField.setText(playerList.getSelectedValue().replaceAll(" - Lobby", ""));
			}
		});
		playerList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				usernameField.setText(playerList.getSelectedValue());
			}
		});

		playerScrollList.setViewportView(playerList);

		final JButton ipBan = new JButton("IP Ban");
		ipBan.addActionListener(new ActionListener() {
			boolean loggedIn111110 = true;
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully Permanently IP-Banned "+usernameField.getText());
					IPBanL.ban(player, loggedIn111110);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		ipBan.setBounds(145, 57, 114, 30);
		mainMenu.add(ipBan);

		final JButton permBan = new JButton("Perm Ban");
		permBan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully permanently banned "+usernameField.getText());
					player.setPermBanned(true);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		permBan.setBounds(145, 112, 114, 30);
		mainMenu.add(permBan);

		final JButton ipMute = new JButton("IP Mute");
		ipMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean loggedIn111110 = true;
				if (checkAll(false)) {
					logAction("Punishments", "Successfully permanently IPMuted "+usernameField.getText());
					IPMute.mute(player, loggedIn111110);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		ipMute.setBounds(317, 57, 114, 30);
		mainMenu.add(ipMute);

		final JButton giveAdmin = new JButton("Give Admin");
		giveAdmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully promoted "+usernameField.getText()+" to administrator.");
					player.setRights(2);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		giveAdmin.setBounds(480, 57, 114, 30);
		mainMenu.add(giveAdmin);

		final JButton giveMod = new JButton("Give Mod");
		giveMod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully promoted "+usernameField.getText()+" to moderator.");
					player.setRights(1);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		giveMod.setBounds(480, 112, 114, 30);
		mainMenu.add(giveMod);

		final JButton tempBan = new JButton("Temp ban");
		tempBan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(true)) {
					logAction("Punishments", "Successfully banned "+usernameField.getText()+" for "+getGrammar());
					player.setBanned(Utils.currentTimeMillis() + Integer.parseInt(hoursField.getText())* 60 * 60 * 1000);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		tempBan.setBounds(145, 166, 114, 30);
		mainMenu.add(tempBan);

		final JButton demotePlayer = new JButton("Demote");
		demotePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully demoted "+usernameField.getText()+" to regular player.");
					player.setRights(0);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		demotePlayer.setBounds(480, 166, 114, 30);
		mainMenu.add(demotePlayer);

		final JButton permMute = new JButton("Perm Mute");
		permMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					logAction("Punishments", "Successfully permanently muted "+usernameField.getText());
					player.setPermMuted(true);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		permMute.setBounds(317, 112, 114, 30);
		mainMenu.add(permMute);

		final JButton tempMute = new JButton("Temp Mute");
		tempMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(true)) {
					logAction("Punishments", "Successfully muted "+usernameField.getText()+" for "+getGrammar());
					player.setMuted(Utils.currentTimeMillis() + Integer.parseInt(hoursField.getText())* 60 * 60 * 1000);
					SerializableFilesManager.savePlayer(player);
				}
			}
		});
		tempMute.setBounds(317, 166, 114, 30);
		mainMenu.add(tempMute);

		for (int i = 0; i < mainMenu.getComponents().length; i++) { //needs to be above textField or they become unclickable.
			mainMenu.getComponents()[i].setFocusable(false);
		}

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(145, 304, 449, 80);
		mainMenu.add(scrollPane);

		scrollPane.setViewportView(logger);
		final JButton btnShowOffline = new JButton("Include offline");

		final JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(145, 226, 79, 14);
		mainMenu.add(lblUsername);

		usernameField.setBounds(212, 223, 193, 20);
		mainMenu.add(usernameField);
		usernameField.setColumns(10);

		final JLabel lblHoursifApplicable = new JLabel("Hours (if applicable):");
		lblHoursifApplicable.setBounds(415, 226, 130, 14);
		mainMenu.add(lblHoursifApplicable);

		hoursField.setBounds(538, 223, 56, 20);
		mainMenu.add(hoursField);
		hoursField.setColumns(10);

		JPanel Players = new JPanel();
		tabbedPane.addTab("Player Options", null, Players, null);
		Players.setLayout(null);
		
		JButton btnComp = new JButton("Completionist cape");
		btnComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					player.setCompletedFightCaves();
					player.getPackets().sendGameMessage("<img=6><col=ff8c38>News: "+player.getDisplayName()+" Just completed fight caves.");
					player.setCompletedFightKiln();
					player.getPackets().sendGameMessage("<img=6><col=ff8c38>News: "+player.getDisplayName()+" Just completed Fightkiln.");	
					player.domCount = 150;
					player.penguins = 15;
					player.sinkholes = 5; 
					player.totalTreeDamage = 1000; 
					player.barrowsLoot = 55;
					player.rfd5 = true; 
					player.prestigeNumber = 5; 
					player.implingCount = 120; 
					player.killedQueenBlackDragon2 = true; 
					player.advancedagilitylaps = 100; 
					player.heroSteals = 150;
					player.cutDiamonds = 500;
					player.barCrawlCompleted = true;  
					player.runiteOre = 50;
					player.cookedFish = 500; 
					player.burntLogs = 150;
					player.choppedIvy = 150;
					player.infusedPouches = 300;  
					player.crystalChest = 20;				
					logAction("Completionist Cape", "Successfully Unlocked Comp requirements for " + player.getUsername());

				}
			}
		});
		btnComp.setBounds(10, 44, 172, 36);
		Players.add(btnComp);
		
		JButton btnPrestige = new JButton("Prestige");
		btnPrestige.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Prestige Level");
					int ammount = Integer.parseInt(points);
					player.SetprestigePoints(ammount);
					logAction("Prestige", "Successfully set "+ player.getUsername() +"'s prestige to " + ammount);

				}
			}
		});
		btnPrestige.setBounds(10, 111, 172, 36);
		Players.add(btnPrestige);
		
		JButton btnSlayerPoints = new JButton("Slayer Points");
		btnSlayerPoints.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Slayer Points");
					int ammount = Integer.parseInt(points);
					player.setSlayerPoints(ammount);
					logAction("Points", "Successfully set "+ player.getUsername() +"'s Slayer Points to " + ammount);

				}
			}
		});
		btnSlayerPoints.setBounds(10, 181, 172, 36);
		Players.add(btnSlayerPoints);
		
		JButton btnDungTokens = new JButton("Dung Tokens");
		btnDungTokens.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Dung Points");
					int ammount = Integer.parseInt(points);
					player.setDungTokens(ammount);
					logAction("Points", "Successfully set "+ player.getUsername() +"'s Dung Tokens to " + ammount);

				}
			}
		});
		btnDungTokens.setBounds(220, 44, 172, 36);
		Players.add(btnDungTokens);
		
		JButton btnLoyalty = new JButton("Loyalty Points");
		btnLoyalty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Loyalty Points");
					int ammount = Integer.parseInt(points);
					player.setLoyaltyPoints(ammount);
					logAction("Points", "Successfully set "+ player.getUsername() +"'s Loyalty Points to " + ammount);

				}
			}
		});
		btnLoyalty.setBounds(220, 181, 172, 36);
		Players.add(btnLoyalty);
		
		JButton VoteCoins = new JButton("Vote Coins");
		VoteCoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Vote Coins");
					int ammount = Integer.parseInt(points);
					player.getInventory().addItem(18201, ammount);
					logAction("Points", "Successfully Gave "+ player.getUsername() + ammount +" Vote Coins");

				}
			}
		});
		VoteCoins.setBounds(10, 249, 172, 36);
		Players.add(VoteCoins);
		
		JButton PKPoints = new JButton("PK Tokens");
		PKPoints.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("PK TOKENS");
					int ammount = Integer.parseInt(points);
					player.getInventory().addItem(12852, ammount);
					logAction("Points", "Successfully Gave "+ player.getUsername() + ammount +" PK Tokens");

				}
			}
		});
		PKPoints.setBounds(220, 249, 172, 36);
		Players.add(PKPoints);
		
		JButton btntrivia = new JButton("Trivia Points");
		btntrivia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Triva Points");
					int ammount = Integer.parseInt(points);
					player.setTriviaPoints(ammount);
					logAction("Points", "Successfully set "+ player.getUsername() +"'s Trivia Points to " + ammount);

				}
			}
		});
		btntrivia.setBounds(422, 249, 172, 36);
		Players.add(btntrivia);
		
		JButton btnItem = new JButton("Give Item");
		btnItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String points = JOptionPane.showInputDialog("Item ID");
					String points1 = JOptionPane.showInputDialog("Quantity");
					int ammount = Integer.parseInt(points);
					int ammount1 = Integer.parseInt(points1);
					player.getInventory().addItem(ammount, ammount1);
					logAction("Item", "Successfully gavE "+ player.getUsername() +" Item ID: " + ammount + " x" +ammount1);

				}
			}
		});
		btnItem.setBounds(422, 181, 172, 36);
		Players.add(btnItem);
		
		JButton master = new JButton("Master");
		master.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, 150000000);
					SerializableFilesManager.savePlayer(player);
					logAction("Skills", "Successfully set "+ player.getUsername() +"'s skills to 99");

				}
			}
		});
		master.setBounds(422, 111, 172, 36);
		Players.add(master);
		
		JButton PNPC = new JButton("PNPC");
		PNPC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String rawr = JOptionPane.showInputDialog("NPC ID");
					int npcid = Integer.parseInt(rawr);
					player.getAppearence().transformIntoNPC(npcid);

				}
			}
		});
		PNPC.setBounds(422, 44, 172, 36);
		Players.add(PNPC);
		
		JButton ForceChat = new JButton("Force Chat");
		ForceChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkAll(false)) {
					String rawr = JOptionPane.showInputDialog("NPC ID");
					player.setNextForceTalk(new ForceTalk(rawr));
					
				}
			}
		});
		ForceChat.setBounds(220, 111, 172, 36);
		Players.add(ForceChat);
		
		
		final JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Highscores", null, panel_2, null);
		panel_2.setLayout(null);


		final JButton button = new JButton("Include offline");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showingOffline = !showingOffline;
				updateList();
				logAction("Player Manager", showingOffline ? "Showing all players, Number of accounts registered: "
						+ new File("./data/chars/characters/").list().length:
							"Only showing online players, Total players online: "+World.getPlayers().size());
				btnShowOffline.setText(showingOffline ? "Only online" : "Include offline");
				button.setText(showingOffline ? "Only online" : "Include offline");
			}
		});
		button.setBounds(12, 23, 123, 23);
		panel_2.add(button);

		btnShowOffline.setFocusable(false);
		btnShowOffline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showingOffline = !showingOffline;
				updateList();
				logAction("Player Manager", showingOffline ? "Showing all players, Number of accounts registered: "
						+ new File("./data/chars/characters/").list().length:
							"Only showing online players, Total players online: "+World.getPlayers().size());
				btnShowOffline.setText(showingOffline ? "Only online" : "Include offline");
				button.setText(showingOffline ? "Only online" : "Include offline");
			}
		});
		btnShowOffline.setBounds(12, 23, 123, 23);
		mainMenu.add(btnShowOffline);

		final JLabel label = new JLabel("Players: ");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(24, 5, 94, 14);
		panel_2.add(label);

		final JLabel attackLabel = new JLabel("1");
		attackLabel.setIcon(new ImageIcon(IMAGE_PATH+"Attack.png"));
		attackLabel.setBounds(177, 23, 76, 29);
		panel_2.add(attackLabel);

		final JLabel constitutionLabel = new JLabel("10");
		constitutionLabel.setIcon(new ImageIcon(IMAGE_PATH+"Constitution.png"));
		constitutionLabel.setBounds(241, 23, 76, 29);
		panel_2.add(constitutionLabel);

		final JLabel miningLabel = new JLabel("1");
		miningLabel.setIcon(new ImageIcon(IMAGE_PATH+"Mining.png"));
		miningLabel.setBounds(307, 23, 76, 29);
		panel_2.add(miningLabel);

		final JLabel strengthLabel = new JLabel("1");
		strengthLabel.setIcon(new ImageIcon(IMAGE_PATH+"Strength.png"));
		strengthLabel.setBounds(177, 62, 76, 29);
		panel_2.add(strengthLabel);

		final JLabel agilityLabel = new JLabel("1");
		agilityLabel.setIcon(new ImageIcon(IMAGE_PATH+"Agility.png"));
		agilityLabel.setBounds(241, 62, 76, 29);
		panel_2.add(agilityLabel);

		final JLabel smithingLabel = new JLabel("1");
		smithingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Smithing.png"));
		smithingLabel.setBounds(307, 62, 76, 29);
		panel_2.add(smithingLabel);

		final JLabel defenceLabel = new JLabel("1");
		defenceLabel.setIcon(new ImageIcon(IMAGE_PATH+"Defence.png"));
		defenceLabel.setBounds(177, 102, 76, 29);
		panel_2.add(defenceLabel);

		final JLabel herbloreLabel = new JLabel("1");
		herbloreLabel.setIcon(new ImageIcon(IMAGE_PATH+"Herblore.png"));
		herbloreLabel.setBounds(241, 102, 76, 29);
		panel_2.add(herbloreLabel);

		final JLabel fishingLabel = new JLabel("1");
		fishingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Fishing.png"));
		fishingLabel.setBounds(307, 102, 76, 29);
		panel_2.add(fishingLabel);

		final JLabel rangedLabel = new JLabel("1");
		rangedLabel.setIcon(new ImageIcon(IMAGE_PATH+"Ranged.png"));
		rangedLabel.setBounds(177, 142, 76, 29);
		panel_2.add(rangedLabel);

		final JLabel thievingLabel = new JLabel("1");
		thievingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Thieving.png"));
		thievingLabel.setBounds(241, 142, 76, 29);
		panel_2.add(thievingLabel);

		final JLabel cookingLabel = new JLabel("1");
		cookingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Cooking.png"));
		cookingLabel.setBounds(307, 142, 76, 29);
		panel_2.add(cookingLabel);

		final JLabel prayerLabel = new JLabel("1");
		prayerLabel.setIcon(new ImageIcon(IMAGE_PATH+"Prayer.png"));
		prayerLabel.setBounds(177, 182, 76, 29);
		panel_2.add(prayerLabel);

		final JLabel craftingLabel = new JLabel("1");
		craftingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Crafting.png"));
		craftingLabel.setBounds(241, 182, 76, 29);
		panel_2.add(craftingLabel);

		final JLabel firemakingLabel = new JLabel("1");
		firemakingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Firemaking.png"));
		firemakingLabel.setBounds(307, 182, 76, 29);
		panel_2.add(firemakingLabel);

		final JLabel magicLabel = new JLabel("1");
		magicLabel.setIcon(new ImageIcon(IMAGE_PATH+"Magic.png"));
		magicLabel.setBounds(177, 222, 76, 29);
		panel_2.add(magicLabel);

		final JLabel fletchingLabel = new JLabel("1");
		fletchingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Fletching.png"));
		fletchingLabel.setBounds(241, 222, 76, 29);
		panel_2.add(fletchingLabel);

		final JLabel woodcuttingLabel = new JLabel("1");
		woodcuttingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Woodcutting.png"));
		woodcuttingLabel.setBounds(307, 222, 76, 29);
		panel_2.add(woodcuttingLabel);

		final JLabel runecraftingLabel = new JLabel("1");
		runecraftingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Runecrafting.png"));
		runecraftingLabel.setBounds(177, 262, 76, 29);
		panel_2.add(runecraftingLabel);

		final JLabel slayerLabel = new JLabel("1");
		slayerLabel.setIcon(new ImageIcon(IMAGE_PATH+"Slayer.png"));
		slayerLabel.setBounds(241, 262, 76, 29);
		panel_2.add(slayerLabel);

		final JLabel farmingLabel = new JLabel("1");
		farmingLabel.setIcon(new ImageIcon(IMAGE_PATH+"Farming.png"));
		farmingLabel.setBounds(307, 262, 76, 29);
		panel_2.add(farmingLabel);

		final JLabel constructionLabel = new JLabel("1");
		constructionLabel.setIcon(new ImageIcon(IMAGE_PATH+"Construction.png"));
		constructionLabel.setBounds(177, 302, 76, 29);
		panel_2.add(constructionLabel);

		final JLabel hunterLabel = new JLabel("1");
		hunterLabel.setIcon(new ImageIcon(IMAGE_PATH+"Hunter.png"));
		hunterLabel.setBounds(241, 302, 76, 29);
		panel_2.add(hunterLabel);

		final JLabel summoningLabel = new JLabel("1");
		summoningLabel.setIcon(new ImageIcon(IMAGE_PATH+"Summoning.png"));
		summoningLabel.setBounds(307, 302, 76, 29);
		panel_2.add(summoningLabel);

		final JLabel dungeoneeringLabel = new JLabel("1");
		dungeoneeringLabel.setIcon(new ImageIcon(IMAGE_PATH+"Dungeoneering.png"));
		dungeoneeringLabel.setBounds(177, 342, 76, 29);
		panel_2.add(dungeoneeringLabel);

		final JLabel questPointsLabel = new JLabel("0");
		questPointsLabel.setIcon(new ImageIcon(IMAGE_PATH+"QuestPoints.PNG"));
		questPointsLabel.setBounds(241, 342, 76, 29);
		panel_2.add(questPointsLabel);

		final JLabel totalLevelLabel = new JLabel("Total Level:");
		totalLevelLabel.setBounds(393, 32, 189, 14);
		panel_2.add(totalLevelLabel);

		final JLabel totalExpLabel = new JLabel("Total Exp:");
		totalExpLabel.setBounds(393, 69, 201, 14);
		panel_2.add(totalExpLabel);

		final JLabel memberLabel = new JLabel("Member:");
		memberLabel.setBounds(393, 149, 165, 14);
		panel_2.add(memberLabel);

		final JLabel rightsLabel = new JLabel("Rights:");
		rightsLabel.setBounds(393, 109, 165, 14);
		panel_2.add(rightsLabel);

		final JLabel bankSizeLabel = new JLabel("Bank size:");
		bankSizeLabel.setBounds(393, 189, 176, 14);
		panel_2.add(bankSizeLabel);

		final JLabel canTradeLabel = new JLabel("Trade banned:");
		canTradeLabel.setBounds(393, 269, 201, 14);
		panel_2.add(canTradeLabel);

		final JLabel bannedLabel = new JLabel("Banned:");
		bannedLabel.setBounds(393, 309, 165, 14);
		panel_2.add(bannedLabel);

		final JLabel moneyPouchLabel = new JLabel("Money pouch:");
		moneyPouchLabel.setBounds(393, 229, 201, 14);
		panel_2.add(moneyPouchLabel);

		final JLabel mutedLabel = new JLabel("Muted:");
		mutedLabel.setBounds(393, 349, 176, 14);
		panel_2.add(mutedLabel);

		final JPanel infoPanel = new JPanel();
		tabbedPane.addTab("Information", null, infoPanel, null);
		infoPanel.setLayout(null);

		final JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(14, 57, 198, 301);
		infoPanel.add(scrollPane_2);

		updateNPCList();
		scrollPane_2.setViewportView(npcList);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 57, 121, 327);
		panel_2.add(scrollPane_1);

		hsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (loadPlayer()) {
					attackLabel.setText(player.getSkills().getLevel(Skills.ATTACK)+"");
					constitutionLabel.setText(player.getSkills().getLevel(Skills.HITPOINTS)+"");
					constitutionLabel.setText(player.getSkills().getLevel(Skills.HITPOINTS)+"");
					miningLabel.setText(player.getSkills().getLevel(Skills.MINING)+"");
					strengthLabel.setText(player.getSkills().getLevel(Skills.STRENGTH)+"");
					agilityLabel.setText(player.getSkills().getLevel(Skills.AGILITY)+"");
					smithingLabel.setText(player.getSkills().getLevel(Skills.SMITHING)+"");
					defenceLabel.setText(player.getSkills().getLevel(Skills.DEFENCE)+"");
					herbloreLabel.setText(player.getSkills().getLevel(Skills.HERBLORE)+"");
					fishingLabel.setText(player.getSkills().getLevel(Skills.FISHING)+"");
					rangedLabel.setText(player.getSkills().getLevel(Skills.RANGE)+"");
					thievingLabel.setText(player.getSkills().getLevel(Skills.THIEVING)+"");
					cookingLabel.setText(player.getSkills().getLevel(Skills.COOKING)+"");
					prayerLabel.setText(player.getSkills().getLevel(Skills.PRAYER)+"");
					craftingLabel.setText(player.getSkills().getLevel(Skills.CRAFTING)+"");
					firemakingLabel.setText(player.getSkills().getLevel(Skills.FIREMAKING)+"");
					magicLabel.setText(player.getSkills().getLevel(Skills.MAGIC)+"");
					fletchingLabel.setText(player.getSkills().getLevel(Skills.FLETCHING)+"");
					woodcuttingLabel.setText(player.getSkills().getLevel(Skills.WOODCUTTING)+"");
					runecraftingLabel.setText(player.getSkills().getLevel(Skills.RUNECRAFTING)+"");
					slayerLabel.setText(player.getSkills().getLevel(Skills.SLAYER)+"");
					farmingLabel.setText(player.getSkills().getLevel(Skills.FARMING)+"");
					constructionLabel.setText(player.getSkills().getLevel(Skills.CONSTRUCTION)+"");
					hunterLabel.setText(player.getSkills().getLevel(Skills.HUNTER)+"");
					summoningLabel.setText(player.getSkills().getLevel(Skills.SUMMONING)+"");
					dungeoneeringLabel.setText(player.getSkills().getLevel(Skills.DUNGEONEERING)+"");
					questPointsLabel.setText(player.getLoyaltyPoints()+"");
					totalLevelLabel.setText("Total Level: "+player.getSkills().getTotalLevel());
					totalExpLabel.setText("Total Exp: "+player.getSkills().getTotalXp(player));
					rightsLabel.setText("Rights: "+player.getRights());
					memberLabel.setText("Member: "+(player.isDonator() ? "true" : "false"));
					bankSizeLabel.setText("Bank size: "+player.getBank().getBankSize()+"");
					moneyPouchLabel.setText("Money pouch: "+player.getMoneyPouch().getCoinsAmount());
					canTradeLabel.setText("Trade banned: "+(player.isTradeLocked() ? "true" : "false"));
					bannedLabel.setText("Banned: "+(player.isPermBanned() ? "true" : "false"));
					mutedLabel.setText("Muted: "+(player.isMuted() ? "true" : "false"));
				}
			}
		});
		scrollPane_1.setViewportView(hsList);

		final JLabel idLabel = new JLabel("Id :");
		idLabel.setBounds(241, 59, 322, 14);
		infoPanel.add(idLabel);

		final JLabel informationLabel = new JLabel("NPC information");
		informationLabel.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		informationLabel.setBounds(241, 12, 353, 36);
		infoPanel.add(informationLabel);

		final JLabel nameLabel = new JLabel("Name :");
		nameLabel.setBounds(241, 84, 353, 14);
		infoPanel.add(nameLabel);

		final JLabel combatLevelLabel = new JLabel("Combat level:");
		combatLevelLabel.setBounds(241, 109, 322, 14);
		infoPanel.add(combatLevelLabel);

		final JLabel examineLabel = new JLabel("Examine:");
		examineLabel.setBounds(241, 134, 353, 14);
		infoPanel.add(examineLabel);

		final JLabel attackAnimationLabel = new JLabel("Attack animation: ");
		attackAnimationLabel.setBounds(241, 159, 322, 14);
		infoPanel.add(attackAnimationLabel);

		final JLabel defenceAnimationLabel = new JLabel("Defence animation: ");
		defenceAnimationLabel.setBounds(241, 184, 322, 14);
		infoPanel.add(defenceAnimationLabel);

		final JLabel deathAnimationLabel = new JLabel("Death animation: ");
		deathAnimationLabel.setBounds(241, 209, 322, 14);
		infoPanel.add(deathAnimationLabel);

		final JLabel respawnTimeLabel = new JLabel("Respawn time: ");
		respawnTimeLabel.setBounds(241, 234, 322, 14);
		infoPanel.add(respawnTimeLabel);

		final JLabel firstOptionLabel = new JLabel("First option:");
		firstOptionLabel.setBounds(241, 259, 322, 14);
		infoPanel.add(firstOptionLabel);

		final JLabel secondOptionLabel = new JLabel("Second option:");
		secondOptionLabel.setBounds(241, 284, 322, 14);
		infoPanel.add(secondOptionLabel);

		final JLabel thirdOptionLabel = new JLabel("Third option:");
		thirdOptionLabel.setBounds(241, 309, 322, 14);
		infoPanel.add(thirdOptionLabel);

		final JLabel fourthOptionLabel = new JLabel("Fourth option:");
		fourthOptionLabel.setBounds(241, 334, 322, 14);
		infoPanel.add(fourthOptionLabel);

		final JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				npcListModel.clear();
				if (!showingItems && !showingObjects) {
					for (int i = 0; i <= Utils.getNPCDefinitionsSize(); i++) {
						if (NPCDefinitions.getNPCDefinitions(i).getName().toLowerCase().contains(textField.getText())) {
							npcListModel.addElement(i+" - "+NPCDefinitions.getNPCDefinitions(i).name);
						}
					}
					return;
				} else if (showingItems && !showingObjects) {
					for (int i = 0; i <= Utils.getItemDefinitionsSize(); i++) {
						if (ItemDefinitions.getItemDefinitions(i).getName().toLowerCase().contains(textField.getText())) {
							npcListModel.addElement(i+" - "+ItemDefinitions.getItemDefinitions(i).name);
						}
					}
					return;
				} else {
					for (int i = 0; i <= Utils.getObjectDefinitionsSize(); i++) {
						if (ObjectDefinitions.getObjectDefinitions(i).getName().toLowerCase().contains(textField.getText())) {
							npcListModel.addElement(i+" - "+ObjectDefinitions.getObjectDefinitions(i).getName());
						}
					}
					return;
				}
			}
		});
		searchButton.setBounds(135, 373, 77, 23);
		infoPanel.add(searchButton);

		final JButton btnShowObjects = new JButton("Show Items");
		btnShowObjects.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (showingItems && !showingObjects) {
					showingItems = !showingItems;
					showingObjects = !showingObjects;
					btnShowObjects.setText("Show npcs");
					informationLabel.setText("Object information");
					idLabel.setText("Id : ");
					nameLabel.setText("Name : ");
					combatLevelLabel.setText("Animation : ");
					examineLabel.setText("Examine : ");
					attackAnimationLabel.setText("Size (X) : ");
					defenceAnimationLabel.setText("Size (Y) : ");
					deathAnimationLabel.setText("Projectile clipped : ");
					respawnTimeLabel.setText("Clip type : ");
					firstOptionLabel.setText("First option : ");
					secondOptionLabel.setText("Second option : ");
					thirdOptionLabel.setText("Third option : ");
					fourthOptionLabel.setText("Fourth option : ");
					updateNPCList();
					return;
				} if (showingObjects && !showingItems) {
					showingObjects = !showingObjects;
					btnShowObjects.setText("Show items");
					informationLabel.setText("NPC information");
					idLabel.setText("Id : ");
					nameLabel.setText("Name : ");
					combatLevelLabel.setText("Combat level : ");
					examineLabel.setText("Examine : ");
					attackAnimationLabel.setText("Attack animation : ");
					attackAnimationLabel.setText("Attack animation : ");
					defenceAnimationLabel.setText("Defence animation : ");
					deathAnimationLabel.setText("Death animation : ");
					respawnTimeLabel.setText("Respawn time : ");
					firstOptionLabel.setText("First option : ");
					secondOptionLabel.setText("Second option : ");
					thirdOptionLabel.setText("Third option : ");
					fourthOptionLabel.setText("Fourth option : ");
					updateNPCList();
					return;
				} else {//currently displaying NPC's..
					showingItems = !showingItems;
					btnShowObjects.setText("Show objects");		
					informationLabel.setText("Item information");
					idLabel.setText("Id : ");
					nameLabel.setText("Name : ");
					combatLevelLabel.setText("Size : ");
					examineLabel.setText("Examine : ");
					attackAnimationLabel.setText("Price : ");
					attackAnimationLabel.setText("Weight : ");
					defenceAnimationLabel.setText("Tradeable : ");
					deathAnimationLabel.setText("Noted : ");
					respawnTimeLabel.setText("Lended item : ");
					firstOptionLabel.setText("First option : ");
					secondOptionLabel.setText("Second option : ");
					thirdOptionLabel.setText("Third option : ");
					fourthOptionLabel.setText("Fourth option : ");
					updateNPCList();
					return;
				}
			}
		});
		btnShowObjects.setBounds(14, 23, 198, 23);
		infoPanel.add(btnShowObjects);

		for (int i = 0; i < infoPanel.getComponents().length; i++) { //needs to be above textField or they become unclickable.
			infoPanel.getComponents()[i].setFocusable(false);
		}

		textField = new JTextField();
		textField.setBounds(14, 374, 111, 20);
		infoPanel.add(textField);
		textField.setColumns(10);

		JPanel toolsPanel = new JPanel();
		tabbedPane.addTab("Tools", null, toolsPanel, null);
		toolsPanel.setLayout(null);

		JButton btnNewButton = new JButton("Pack item bonuses");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ItemBonusesPacker.main(null);
					logAction("Item bonuses", "Successfully packed item bonuses.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(10, 44, 172, 36);
		toolsPanel.add(btnNewButton);

		JButton btnUnpackIb = new JButton("Reload prices");
		btnUnpackIb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemManager.inits();
				logAction("Price manager", "Successfully reloaded item prices.");
			}
		});
		btnUnpackIb.setBounds(10, 111, 172, 36);
		toolsPanel.add(btnUnpackIb);

		JButton btnPackNpcBonuses = new JButton("Pack NPC bonuses");
		btnPackNpcBonuses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NPCBonuses.loadUnpackedNPCBonuses();
				logAction("NPC bonuses", "Successfully packed NPC bonuses");
			}
		});
		btnPackNpcBonuses.setBounds(10, 181, 172, 36);
		toolsPanel.add(btnPackNpcBonuses);

		JButton btnPackMusicHints = new JButton("Reload item weights");
		btnPackMusicHints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WeightManager.init();
				logAction("Weight manager", "Successfully reloaded item weights.");
			}
		});
		btnPackMusicHints.setBounds(220, 44, 172, 36);
		toolsPanel.add(btnPackMusicHints);

		JButton btnPackCensoredWords = new JButton("Pack censored words");
		btnPackCensoredWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Censor.loadUnpackedCensoredWords();
				logAction("Profanity filter", "Successfully packed censored words.");
			}
		});
		btnPackCensoredWords.setBounds(220, 111, 172, 36);
		toolsPanel.add(btnPackCensoredWords);

		JButton btnPackNpcSpawns = new JButton("Pack NPC spawns");
		btnPackNpcSpawns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NPCSpawns.packNPCSpawns();
				logAction("NPC Spawns", "Successfully packed NPC Spawns.");
			}
		});
		btnPackNpcSpawns.setBounds(220, 181, 172, 36);
		toolsPanel.add(btnPackNpcSpawns);

		JButton btnPackShops = new JButton("Pack shops");
		btnPackShops.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShopsHandler.loadUnpackedShops();
				logAction("Shops handler", "Successfully packed the shops.");
			}
		});
		btnPackShops.setBounds(10, 249, 172, 36);
		toolsPanel.add(btnPackShops);

		JButton btnPackObjectSpawns = new JButton("Pack object spawns");
		btnPackObjectSpawns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObjectSpawns.packObjectSpawns();
				logAction("Object spawns", "Successfully packed object spawns.");
			}
		});
		btnPackObjectSpawns.setBounds(220, 249, 172, 36);
		toolsPanel.add(btnPackObjectSpawns);

		JButton btnShutdownServer = new JButton("Shutdown server");
		btnShutdownServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Launcher.shutdown();
				logAction("Initializer", "Shutting down server...");
			}
		});
		btnShutdownServer.setBounds(422, 249, 172, 36);
		toolsPanel.add(btnShutdownServer);

		JButton btnToggleYell = new JButton("Toggle yell");
		btnToggleYell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.YELL_DISABLED = !Settings.YELL_DISABLED;
				logAction("Yell handler", "The yell system has been "+(Settings.YELL_DISABLED ? "dis" : "en")+"abled.");
			}
		});
		btnToggleYell.setBounds(422, 181, 172, 36);
		toolsPanel.add(btnToggleYell);

		JButton btnPackNpcCombat = new JButton("Pack NPC combat defs");
		btnPackNpcCombat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NPCCombatDefinitionsL.loadUnpackedNPCCombatDefinitions();
				logAction("NPC combat definitions", "Successfully packed npc combat definitions.");
			}
		});
		btnPackNpcCombat.setBounds(422, 111, 172, 36);
		toolsPanel.add(btnPackNpcCombat);

		JButton btnPackItemExamine = new JButton("Pack item examines");
		btnPackItemExamine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemExamines.loadUnpackedItemExamines();
				logAction("Item examines", "Successfully packed item examines.");
			}
		});
		btnPackItemExamine.setBounds(422, 44, 172, 36);
		toolsPanel.add(btnPackItemExamine);

		 final JComboBox MESSAGE_ALL_BOX = new JComboBox();
		toolsPanel.add(MESSAGE_ALL_BOX);
		MESSAGE_ALL_BOX.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[Alert]", "[Update]", "[Server]", "[Justin]", "[Control Panel]", "[Panel]", "[Console]", "[Prompt]", "[Error]", "[None]" }));
		MESSAGE_ALL_BOX.setBounds(220, 320, 172, 18);
		
		 final JComboBox MESSAGE_ALL_COLOR_BOX = new JComboBox(MESSAGE_COLOR);
		MESSAGE_ALL_COLOR_BOX.setBounds(422, 320, 158, 18);
		toolsPanel.add(MESSAGE_ALL_COLOR_BOX);
		MESSAGE_ALL_COLOR_BOX.setModel(new DefaultComboBoxModel(MESSAGE_COLOR));

		JButton btnNewButton_1 = new JButton("Send");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (announcementsTextField.getText() == null) {
					return;
				}
				
				for (Player p : World.getPlayers()) {
					// Getting the Message name
					String SelectedName = MESSAGE_ALL_BOX.getSelectedItem() + ": ";
					if (SelectedName.equalsIgnoreCase("[None]: ")) {
						SelectedName = "";
					}

					// Getting the color
					String color = MESSAGE_ALL_COLOR_BOX.getSelectedItem().toString();
					SelectedName = SelectedName + getColor(color);
					p.sm("<col=FF0000>"+SelectedName + announcementsTextField.getText());
				}
			}
		});
		btnNewButton_1.setBounds(487, 345, 89, 27);
		toolsPanel.add(btnNewButton_1);

		for (int i = 0; i < toolsPanel.getComponents().length; i++) {
			toolsPanel.getComponents()[i].setFocusable(false);
		}

		announcementsTextField = new JTextField();	
		announcementsTextField.setBounds(24, 345, 453, 27);
		toolsPanel.add(announcementsTextField);
		announcementsTextField.setColumns(10);

		JLabel lblWorldAnnouncement = new JLabel("World announcement:");
		lblWorldAnnouncement.setBounds(24, 320, 158, 14);
		toolsPanel.add(lblWorldAnnouncement);
		
		
		npcList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {		
				final String[] info = npcList.getSelectedValue().split(" - ");
				if (!showingItems && !showingObjects) { //if we're displaying NPC's
					NPCDefinitions npcDefs = NPCDefinitions.getNPCDefinitions(Integer.parseInt(info[0]));
					final NPCCombatDefinitions defs = NPC.getCombatDefinitions1();
					idLabel.setText("Id : "+info[0]);
					nameLabel.setText("Name : "+info[1]);
					combatLevelLabel.setText("Combat level : "+npcDefs.combatLevel);
					examineLabel.setText("Examine : "+NPCExamines.getExamine(Integer.parseInt(info[0])));
					attackAnimationLabel.setText("Attack animation : "+defs.getAttackEmote());
					attackAnimationLabel.setText("Attack animation : "+defs.getAttackEmote());
					defenceAnimationLabel.setText("Defence animation : "+defs.getDefenceEmote());
					deathAnimationLabel.setText("Death animation : "+defs.getDeathEmote());
					respawnTimeLabel.setText("Respawn time : "+defs.getRespawnDelay()+" seconds");
					firstOptionLabel.setText("First option : ");
					secondOptionLabel.setText("Second option : ");
					thirdOptionLabel.setText("Third option : ");
					fourthOptionLabel.setText("Fourth option : ");
					return;
				} else if (showingItems && !showingObjects) { //Items...
					ItemDefinitions itemDefs = ItemDefinitions.getItemDefinitions(Integer.parseInt(info[0]));
					idLabel.setText("Id : "+info[0]);
					nameLabel.setText("Name : "+info[1]);
					combatLevelLabel.setText("Oversized : "+itemDefs.isOverSized());
					examineLabel.setText("Examine : "+ItemExamines.getExamine(new Item(Integer.parseInt(info[0]))));
					attackAnimationLabel.setText("Price : "+ItemManager.getPrice(Integer.parseInt(info[0])));
					attackAnimationLabel.setText("Weight : "+WeightManager.getWeight(Integer.parseInt(info[0]))+" KG");
					defenceAnimationLabel.setText("Tradeable : "+ItemConstants.isTradeable(new Item(Integer.parseInt(info[0]))));
					deathAnimationLabel.setText("Noted : "+itemDefs.isNoted());
					respawnTimeLabel.setText("Lended item : "+itemDefs.isLended());
					firstOptionLabel.setText("First option : "+itemDefs.inventoryOptions[0]);
					secondOptionLabel.setText("Second option : "+itemDefs.inventoryOptions[1]);
					thirdOptionLabel.setText("Third option : "+itemDefs.inventoryOptions[2]);
					fourthOptionLabel.setText("Fourth option : "+itemDefs.inventoryOptions[3]);
					return;
				} else { //Objects
					ObjectDefinitions objectDefs = ObjectDefinitions.getObjectDefinitions(Integer.parseInt(info[0]));
					idLabel.setText("Id : "+info[0]);
					nameLabel.setText("Name : "+info[1]);
					combatLevelLabel.setText("Animation : "+objectDefs.objectAnimation);
					examineLabel.setText("Examine : ");
					attackAnimationLabel.setText("Size (X) : "+objectDefs.sizeX);
					defenceAnimationLabel.setText("Size (Y) : "+objectDefs.sizeY);
					deathAnimationLabel.setText("Projectile clipped : "+objectDefs.isProjectileCliped());
					respawnTimeLabel.setText("Clip type : "+objectDefs.getClipType());
					firstOptionLabel.setText("First option : "+objectDefs.getOption(0));
					secondOptionLabel.setText("Second option : "+objectDefs.getOption(1));
					thirdOptionLabel.setText("Third option : "+objectDefs.getOption(2));
					fourthOptionLabel.setText("Fourth option : "+objectDefs.getOption(3));
					return;
				}
			}
		});

		for (int i = 0; i < panel_2.getComponents().length; i++) { //needs to be above textField or they become unclickable.
			panel_2.getComponents()[i].setFocusable(false);
		}

	}
	
	public final String[] MESSAGE_COLOR = { "Red", "Cyan", "Green", "Yellow", "Magenta", "Orange", "Dark Red", "Dark Blue", "White", "Black", "None" };
	
	public String[][] MESSAGE_COLORS = {
			{"Red","<col=FF0000>"},
			{"Cyan","<col=00FFFF>"},
			{"Green","<col=00FF00>"},
			{"Yellow","<col=FFFF00>"},
			{"Magenta","<col=ff00ff>"},
			{"Orange","<col=FFA500>"},
			{"Dark Red","<col=8B0000>"},
			{"Dark Blue","<col=0000ff>"},
			{"White","<col=FFFFFF>"},
			{"Black","<col=000000>"}};

		public String getColor(String color) {
			for (int i = 0; i < MESSAGE_COLORS.length; i++) {
				if (color.equalsIgnoreCase(MESSAGE_COLORS[i][0])) {
					return MESSAGE_COLORS[i][1];
				}
			}
			return "@bla@";
		}
		
	protected void updateNPCList() {
		npcListModel.clear();
		if (showingItems) {
			for (int i = 0; i <= Utils.getItemDefinitionsSize(); i++) {
				npcListModel.addElement(i+" - "+ItemDefinitions.getItemDefinitions(i).name);
			}
		}
		else if (showingObjects) {
			for (int i = 0; i <= Utils.getObjectDefinitionsSize(); i++) {
				npcListModel.addElement(i+" - "+ObjectDefinitions.getObjectDefinitions(i).getName());
			}
		} else {
			for (int i = 0; i <= Utils.getNPCDefinitionsSize(); i++) {
				npcListModel.addElement(i+" - "+NPCDefinitions.getNPCDefinitions(i).name);
			}
		}
	}

	protected String getGrammar() {
		return hoursField.getText()+ (hoursField.getText().equalsIgnoreCase("1") ? " hour" : " hours");
	}

	private boolean loadPlayer() {
		player =  World.getPlayerByDisplayName(hsList.getSelectedValue());
		if (player == null) {
			player = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(hsList.getSelectedValue()));
			player.setUsername(Utils.formatPlayerNameForProtocol(hsList.getSelectedValue()));
			return true;
		}
		return true;
	}

	protected boolean checkAll(boolean requiresHours) {
		player = World.getPlayerByDisplayName(usernameField.getText());
		if (usernameField.getText().equalsIgnoreCase("")) {
			logAction("Error", "Select a player from the left or type their name in the username field.");
			return false;
		} else if (!SerializableFilesManager.containsPlayer(Utils.formatPlayerNameForProtocol(usernameField.getText()))) {
			logAction("Error", "It seems that player dosen't exist.");
			return false;
		} if (requiresHours) {
			try {
				if (hoursField.getText().equalsIgnoreCase("")) {
					logAction("Error", "For this punishment please fill in the hours field.");
					return false;
				}
				Integer.parseInt(hoursField.getText());
				if (hoursField.getText().startsWith("0")) {
					logAction("Error", "The hours field must start with an Integer of 1 - 9");
					return false;
				}
			} catch (final NumberFormatException e) {
				logAction("Error", "You may only use Integers in the hours text box.");
				return false;
			}
		} if (player == null) {
			player = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(usernameField.getText()));
			player.setUsername(Utils.formatPlayerNameForProtocol(usernameField.getText()));
			return true;
		}
		return true;
	}

	public static void updateList() {
		playersModel.clear();
		hsPlayersList.clear();
		if (showingOffline) {
			init(new File("./data/characters/"));
			return;
		}
		for (final Player p : World.getPlayers()) {
			playersModel.addElement(p.getUsername());
			hsPlayersList.addElement(p.getUsername());
		}
		for (final Player p : World.getLobbyPlayers()) {
			playersModel.addElement(p.getUsername()+" - Lobby");
			hsPlayersList.addElement(p.getUsername()+" - Lobby");
		}
	}


	public void logAction(String file, String log) {
		loggingModel.addElement("["+file+"]"+" "+log);
		logger.ensureIndexIsVisible(logger.getModel().getSize()-1);
		return;
	}

	public static void init(final File folder) { //shows players that are offline.
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				init(fileEntry);
			} else {
				playersModel.addElement(fileEntry.getName().replaceAll(".p", ""));
				hsPlayersList.addElement(fileEntry.getName().replaceAll(".p", ""));
			}
		}
	}
}