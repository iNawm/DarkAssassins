package com.rs.game.player.dialogues;

/**
 * Test dialogue.
 *
 * @author Arham Siddiqui
 */
public class Test extends Dialogue {
    @Override
    public void start() {
        int animation = (int) parameters[0];
        sendPlayerDialogue(animation, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        end();
    }

    @Override
    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
