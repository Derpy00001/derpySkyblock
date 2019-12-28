package me.derpy.skyblock.objects.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.derpy.skyblock.Skyblock;
import me.derpy.skyblock.utils.Console;

public class GuiHolderUserInv implements InventoryHolder{
	//Custom holder to tag inventories as a skyblock inventory
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void finalize() throws Throwable { 
		if(Skyblock.getPlugin(Skyblock.class).getConfig().getBoolean("Primary.Garbage_Collection.notify")) {
			Console.print("Object garbage collected : " + this.toString()); 
		}
	} 
}
