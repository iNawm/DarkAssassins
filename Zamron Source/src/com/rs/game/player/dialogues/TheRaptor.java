package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;

public class TheRaptor extends Dialogue {

private int npcId;

@Override
public void start() {
sendEntityDialogue(SEND_2_TEXT_CHAT,
new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
"Happy Halloween, Would you like some candy?"}, IS_NPC, npcId, 659);
}

@Override
public void run(int interfaceId, int componentId) {
if (stage == -1) {
sendOptionsDialogue("You want some party candy?", "Yes please!",
"No thanks, I'm fat!");
stage = 1;
} else if (stage == 1) {
if (componentId == OPTION_1) {
player.setNextForceTalk(new ForceTalk("Trick or Treat!"));
player.setNextAnimation(new Animation(10530));
player.setNextGraphics(new Graphics(1864));
player.getInventory().addItem(14082, 1);
end();
}
else if (componentId == OPTION_2) {
end();
}
}
}

@Override
public void finish() {

}
}