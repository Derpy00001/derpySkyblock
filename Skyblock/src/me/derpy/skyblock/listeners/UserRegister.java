package me.derpy.skyblock.listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.derpy.skyblock.creators.IslandLoader;
import me.derpy.skyblock.exceptions.UserHasNoIslandException;
import me.derpy.skyblock.extra.Extras;
import me.derpy.skyblock.extra.scoreboard.Board;
import me.derpy.skyblock.extra.scoreboard.BoardManager;
import me.derpy.skyblock.objects.main.Islander;
public class UserRegister implements Listener{
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		OfflinePlayer p = e.getPlayer();
		if(p!=null) {
			if(Islander.getUser(p).hasIsland()) {
				try {
					Islander islander = Islander.getUser(p);
					IslandLoader.load(islander.getIsland().getName());
				} catch (UserHasNoIslandException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Extras.Scoreboard.boardEnabled()) {
			Board.createBoard(Islander.getUser(e.getPlayer()));
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if(BoardManager.getBoards().containsKey(Islander.getUser(e.getPlayer()))) {
			BoardManager.removeBoard(Islander.getUser(e.getPlayer()));
		}
	}
}
