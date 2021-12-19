package com.chai.tobaccoPipes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NoModifyListener implements Listener
{
	@EventHandler
	public void onAnvil(PrepareAnvilEvent event)
	{
		ItemStack item = event.getInventory().getItem(0);
		ItemStack sacrifice = event.getInventory().getItem(1);
		
		if (item != null && item.getType() == Material.WOODEN_HOE)
		{
			// Get meta
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			
			// Is it a smoking pipe?
			if (container.has(Main.getKey(), PersistentDataType.INTEGER))
			{
				if (sacrifice != null)
				{
					// Cancel repair
					event.setResult(new ItemStack(Material.AIR));
					Player ply = (Player) event.getViewers().get(0);
					ply.updateInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onEnchant(PrepareItemEnchantEvent event)
	{
		ItemStack item = event.getItem();
		
		if (item != null && item.getType() == Material.WOODEN_HOE)
		{
			// Get meta
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			
			// Is it a smoking pipe?
			if (container.has(Main.getKey(), PersistentDataType.INTEGER))
			{
				// Cancel enchant
				event.setCancelled(true);
			}
		}
	}
}