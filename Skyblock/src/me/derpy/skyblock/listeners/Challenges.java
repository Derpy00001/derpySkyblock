package me.derpy.skyblock.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import me.derpy.skyblock.enums.ChallengeType;
import me.derpy.skyblock.events.IslandLevelChangeEvent;
import me.derpy.skyblock.exceptions.UserHasNoIslandException;
import me.derpy.skyblock.objects.data.challenges.ChallengeMaterial;
import me.derpy.skyblock.objects.data.challenges.ChallengeMob;
import me.derpy.skyblock.objects.data.challenges.IslandChallenge;
import me.derpy.skyblock.objects.main.Island;
import me.derpy.skyblock.objects.main.Islander;

public class Challenges implements Listener{
	//SLAY
	@EventHandler
	private void onDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller()!=null) {
			Islander islander = Islander.getUser(e.getEntity().getKiller());
			if(islander.hasIsland()) {
				try {
					List<IslandChallenge> challenges = islander.getIsland().getChallenges();
					for(IslandChallenge data:challenges) {
						if(!data.isCompleted()) {
							if(data.getData().getChallengeType()==ChallengeType.SLAY) {
								ChallengeMob mob = (ChallengeMob) data.getData();
								if(e.getEntityType()==mob.getMob()) {
									data.setProgress(data.getProgress()+1);
								}
							}
						}
					}
				} catch (UserHasNoIslandException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	//MINE
	@EventHandler
	private void onBreak(BlockBreakEvent e) {
		if(e.getPlayer()!=null) {
			Islander islander = Islander.getUser(e.getPlayer());
			if(islander.hasIsland()) {
				try {
					List<IslandChallenge> challenges = islander.getIsland().getChallenges();
					for(IslandChallenge data:challenges) {
						if(!data.isCompleted()) {
							if(data.getData().getChallengeType()==ChallengeType.MINE) {
								ChallengeMaterial material = (ChallengeMaterial) data.getData();
								if(e.getBlock().getType()==material.getMaterial()) {
									data.setProgress(data.getProgress()+1);
								}
							}
						}
					}
				} catch (UserHasNoIslandException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	}
	//ISLAND_LEVEL
	@EventHandler
	private void onChange(IslandLevelChangeEvent e) {
		if(e.getIsland()!=null) {
			Island island = e.getIsland();
			List<IslandChallenge> challenges = island.getChallenges();
			for(IslandChallenge data:challenges) {
				if(!data.isCompleted()) {
					if(data.getData().getChallengeType()==ChallengeType.ISLAND_LEVEL) {
						data.setProgress(e.getNewLevel());
					}
				}
			}
		}
	}
}
