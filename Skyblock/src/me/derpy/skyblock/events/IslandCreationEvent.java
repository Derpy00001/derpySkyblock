package me.derpy.skyblock.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.derpy.skyblock.Skyblock;
import me.derpy.skyblock.objects.main.Islander;
import me.derpy.skyblock.utils.Console;

public class IslandCreationEvent extends Event implements Cancellable{
	private Islander islander;
	private int id;
	private boolean cancelled=false;
	private static final HandlerList handlers = new HandlerList();
	public IslandCreationEvent(boolean isAsync, Islander islander, int islandId) {
		super(isAsync);
		this.islander=islander;
		this.id=islandId;
		Console.print(Integer.toString(this.id)+"|"+Integer.toString(islandId));
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
	public int getIslandTypeId() {
		Console.print(Integer.toString(this.id));
		return this.id;
	}
	public void setIslandTypeId(int i) {
		this.id=i;
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
