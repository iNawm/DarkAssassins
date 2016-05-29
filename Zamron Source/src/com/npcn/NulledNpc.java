/**
 * @author Staff. tense0087 - skype
 */
package com.npcn;

import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.NPCCombatDefinitionsL;

/**
 * @author Staff. tense0087 - skype
 */
public class NulledNpc {
	
	private int id;
	private int NulledId;
	
	public void init_npc() {
		
	}
	
	public NulledNpc(int normalid) {
		this.id = normalid;
		GenerateNulledMob();
	}
	
	public int[][] GenerateUberBossGameNpc() {
		NPCCombatDefinitions defs = NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
		int hp = defs.getHitpoints();
		return new int[][] {{}};
	}
	
	public int GetNulledId() {
		return NulledId;
	}

	private void GenerateNulledMob() {
		this.NulledId = 65536 + id;
		
	}
	
	public int GenerateNewMobEntity(int times) {
		if(times == 0)
			return NulledId;
		return (NulledId + 65536) * times;
	}
}
