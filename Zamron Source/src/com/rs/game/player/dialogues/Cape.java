package com.rs.game.player.dialogues;

import java.sql.SQLException;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.player.Skills;
//import com.rs.utils.AdventureLog;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public class Cape extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"I would like to see the requirements.", "I would like the completionist cape please.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.getInterfaceManager().sendCompCape();
			}
		if (componentId == OPTION_2) {
			player.completedCompletionistCape();
			if(!player.isCompletedComp()) {
				player.setNextForceTalk(new ForceTalk("Because im so not worth it!"));
				player.setNextAnimation(new Animation(767));
		} else if (player.getInventory().containsItem(995, 10000000) && player.isCompletedComp() == true) {
			player.getInventory().deleteItem(995, 10000000);
			World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername())+ "<col=800000> Has just achieved the Completionists cape!"+ "</col> ", false);
			player.getInventory().addItem(20769, 1);	
			player.getInventory().addItem(20770, 1);
			player.getInventory().addItem(20771, 1);
			player.getInventory().addItem(20772, 1);
			String moof1 = "has achieved the";
			String moof2 = "Completionist cape!"; /*
			try {
				AdventureLog.createConnection();
				AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"', '"+moof2+"');");
				AdventureLog.destroyConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("[SQLMANAGER] Query Executed.");
			AdventureLog.destroyConnection(); */
			}
		end();
		} else if (componentId == OPTION_3) {
			end();
		}
		end();
		
		}

	public void sql() {
		
	}

	@Override
	public void finish() {

	}

}
