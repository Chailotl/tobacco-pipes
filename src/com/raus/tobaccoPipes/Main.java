package com.raus.tobaccoPipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin
{
	private static Main instance;
	private static NamespacedKey key;
	
	private static Material tobacco;
	
	public Main()
	{
		instance = this;
	}
	
	@Override
	public void onEnable()
	{
		// Namespace
		key = new NamespacedKey(this, "tobacco_pipes");
		
		// Config stuff
		saveDefaultConfig();
		tobacco = Material.getMaterial(getConfig().getString("tobacco"));
		
		// Register command
		this.getCommand("pipes").setExecutor(new ReloadCommand());
		
		// Listener
		getServer().getPluginManager().registerEvents(new SmokeListener(), this);
		getServer().getPluginManager().registerEvents(new NoModifyListener(), this);
		
		// Tobbaco pipes
		ItemStack item = new ItemStack(Material.WOODEN_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "Tobacco Pipe");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
		Damageable dmg = (Damageable) meta;
		dmg.setDamage(58); // Should start empty
		item.setItemMeta(meta);
		
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape(" S", "B ");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('B', Material.BOWL);
		Bukkit.addRecipe(recipe);
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public static NamespacedKey getKey()
	{
		return key;
	}
	
	public static Material getTobacco()
	{
		return tobacco;
	}
	
	public void rebuild()
	{
		reloadConfig();
		tobacco = Material.getMaterial(getConfig().getString("tobacco"));
	}
}