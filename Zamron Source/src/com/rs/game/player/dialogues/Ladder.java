package com.rs.game.player.dialogues;

import com.rs.game.player.controlers.RomeIntroduction;
import com.rs.game.player.dialogues.Dialogue;

/**
 * Climbing down the ladder. Part of the Rome tutorial.
 * Created by Arham 4 on 3/15/14.
 */
public class Ladder extends Dialogue {
    @Override
    public void start() {
        sendPlayerDialogue(9850, "Ah! I feel much better.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendPlayerDialogue(9830, "I should probably go down the ladder and find out what happened.");
                stage = -2;
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
