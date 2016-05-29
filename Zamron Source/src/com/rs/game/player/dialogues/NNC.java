package com.rs.game.player.dialogues;
  
import java.text.DecimalFormat;
  






import com.rs.game.player.Player;

  
public class NNC extends Dialogue {
  
    public static String FormatNumber(long count) {
        return new DecimalFormat("#,###,##0").format(count).toString();
    }
      
    public static String FormatNumber(Player player) {
        return new DecimalFormat("#,###,##0").format(player.getInventory().getNumerOf(995)).toString();
    }
    int coins = 995;
    private int npcId;
    @SuppressWarnings("unused")
	@Override
    public void start() {
        String PDS = "<col=FF0000>"+player.getDisplayName() + "</col>";
        npcId = (Integer) parameters[0];
         sendPlayerDialogue( 9827, "I'd like to Color my Display Name" );
        stage = 3;
    }
  
    @Override
    public void run(int interfaceId, int componentId) {
  
         if (stage == 3) {
             sendNPCDialogue(npcId, 9827, "Okay "+player.getDisplayName()); 
             stage = 4;
              
            } else if (stage == 4) {
        sendOptionsDialogue("Options", "Hex Color",  "Shade Color",  "Rest Both");
             stage = 5;
           
            } else  if (stage == 5) {
        if (componentId == OPTION_1) {
            player.getTemporaryAttributtes().put("hex_color1", Boolean.TRUE);
            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
            end();
        }
        if (componentId == OPTION_2) {
            player.getTemporaryAttributtes().put("Shad_color1", Boolean.TRUE);
            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
            end();
        }
        if (componentId == OPTION_3) {
            player.shadCode1 = "";
            player.hexCode1 = "";
            end();
        }
    }
 }
  
  
    @Override
    public void finish() {
  
    }
}