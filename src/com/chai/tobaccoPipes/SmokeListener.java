package com.chai.tobaccoPipes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class SmokeListener implements Listener
{
	@EventHandler
	public void onSmoke(PlayerInteractEvent event)
	{
		// Get stuff
		Player ply = event.getPlayer();
		ItemStack item = event.getItem();
		Action act = event.getAction();

		// Is it a valid item/action
		if (item != null && item.getType() == Material.WOODEN_HOE && ply.getCooldown(Material.WOODEN_HOE) <= 0
				&& (act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK))
		{
			// Did we right click an interactible?
			if (act == Action.RIGHT_CLICK_BLOCK)
			{
				Block block = event.getClickedBlock();
				if (block.getType().isInteractable())
				{
					// Cancel event
					return;
				}
			}

			// Get meta and data
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();

			// Is it a smoking pipe?
			if (container.has(Main.getKey(), PersistentDataType.INTEGER))
			{
				// Get damage
				Damageable damage = (Damageable) meta;
				int dmg = damage.getDamage();

				// Check for refill in offhand
				ItemStack offItem = ply.getInventory().getItemInOffHand();
				if (dmg > 1 && offItem != null && offItem.getType() == Main.getTobacco())
				{
					// "Repair" the pipe
					damage.setDamage(Math.max(dmg - 15, 1));
					item.setItemMeta((ItemMeta)damage);
					ply.getInventory().setItemInMainHand(item);

					// Take the "tobacco"
					offItem.setAmount(offItem.getAmount() - 1);
					ply.getInventory().setItemInOffHand(offItem);

					// Cancel event
					event.setCancelled(true);
					return;
				}

				// Is the pipe empty?
				if (dmg >= 58)
				{
					// Cancel event
					event.setCancelled(true);
					return;
				}

				// Create smoke fx
				World world = ply.getWorld();
				Location loc = ply.getEyeLocation();

				world.playSound(ply.getLocation(), Sound.BLOCK_GRASS_HIT, 0.75f, 0.8f);

				Vector vec = ply.getEyeLocation().getDirection();
				vec.multiply(0.3D);
				loc.add(vec);
				vec.multiply(0.066D);

				Random rand = new Random();
				for (int i = 0; i < 10; ++i)
				{
					Vector newVec = (new Vector(rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D));
					newVec.multiply(0.01D);
					Vector mergeVec = vec.add(newVec);
					world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 0, mergeVec.getX(), mergeVec.getY(), mergeVec.getZ());
				}

				// Cooldown
				ply.setCooldown(Material.WOODEN_HOE, 20);

				// Durability
				damage.setDamage(Math.min(dmg + 2, 58));
				item.setItemMeta((ItemMeta)damage);
				if (event.getHand() == EquipmentSlot.HAND)
				{
					ply.getInventory().setItemInMainHand(item);
				}
				else
				{
					ply.getInventory().setItemInOffHand(item);
				}

				// Swing arm
				if (event.getHand() == EquipmentSlot.HAND)
				{
					ply.swingMainHand();
				}
				else
				{
					ply.swingOffHand();
				}

				// Cancel event
				event.setCancelled(true);
			}
		}
	}
}