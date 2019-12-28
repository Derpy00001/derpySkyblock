package me.derpy.skyblock.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.derpy.skyblock.Skyblock;
import me.derpy.skyblock.objects.main.Island;
import me.derpy.skyblock.objects.main.Islander;
import me.derpy.skyblock.utils.Console;

public class IslandDeletionEvent extends Event implements Cancellable{
	private Islander islander;
	private Island island;
	private boolean cancelled=false;
	private static final HandlerList handlers = new HandlerList();
	public IslandDeletionEvent(boolean isAsync, Islander islander, Island island) {
		super(isAsync);
		this.islander=islander;
		this.island=island;
	}
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	public Islander getIslander() {
		return this.islander;
	}
	public Island getIsland() {
		return this.island;
	}
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return this.cancelled;
	}
	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		this.cancelled=arg0;
	}
	@Override
	protected void finalize() throws Throwable { 
		if(Skyblock.getPlugin(Skyblock.class).getConfig().getBoolean("Primary.Garbage_Collection.notify")) {
			Console.print("Object garbage collected : " + this.toString()); 
		}
	} 
}
