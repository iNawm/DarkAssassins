package com.rs.game.player.dialogues;


import com.rs.utils.ShopsHandler;


/**
 * @Author Chaz - Jul 14, 2013
 * <p/>
 * Bob the axe trader/item repairer's dialogue.
 */
public class Bob extends Dialogue {


    private int npcId;


    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "I'd like to trade.",
                "Can you repair my items for me?");
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                switch (componentId) {
                    case OPTION_1:
                        stage = 0;
                        sendPlayerDialogue(9827, "I'd like to trade.");
                        break;
                    case OPTION_2:
                        stage = 1;
                        sendPlayerDialogue(9827, "Can you repair my items for me?");
                        break;
                }
                break;
            case 0:
                stage = 3;
                sendNPCDialogue(npcId, 9827,
                        "Great! I buy and sell pickaxes and hatchets. "
                                + "There are plenty to choose from.");
                break;
            case 1:
                stage = 2;
                sendNPCDialogue(npcId, 9827,
                        "Of course I can, though materials may cost you. Just "
                                + "hand me the item and I'll take a look.");
                break;
            case 2:
                /**
                 * Item Repairing needs to be added. I'll add it later it's
                 * not really a big deal at the moment.
                 */
                break;
            case 3:
            default:
                end();
                break;
        }
    }


    @Override
    public void finish() {
        // TODO Auto-generated method stub


    }


}