package com.secomc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cc.koffeecreations.main.Items.InfusedArmor;
import cc.koffeecreations.main.events.ArmorEquipEvent;

public class ExampleItemListener implements Listener {

	//This Grabs the Config from the Econ plugin so we can use data from it.
	FileConfiguration config = Bukkit.getPluginManager().getPlugin("STSEcon").getConfig();
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		LivingEntity boss = (LivingEntity) e.getEntity();
		EntityDamageByEntityEvent event = e;
		
		boolean sh = false;
		boolean sc = false;
		boolean sp = false;
		boolean sb = false;
		
		boolean rh = false;
		boolean rc = false;
		boolean rp = false;
		boolean rb = false;
		
		int stc = 0;
		int rtc = 0;
		
		//Is the Entity the Dealt the Dmage a Player?
		if (e.getDamager().getType().equals(EntityType.PLAYER)) {
			
			//Loading the Player into a variable to use later
			Player p = Bukkit.getPlayer(e.getDamager().getUniqueId());
			
			//Load the players class from the Econ's Config
			String cl = config.getString("PlayerData."+p.getName()+".Class");
			
			//Check if the player is a Certain class in this case Damage
			if (cl.equalsIgnoreCase("damage")) {
				
				//Check if the item used has Meta I.E Custom Name, Lore and or Enchantments
				if (p.getInventory().getItemInMainHand().hasItemMeta()) {
					
					//Check if the used Item has Lore. These checks prevent Null pointer Errors
					if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
						
						//Load the Item used and its meta into variables to use later
						ItemStack mh = p.getInventory().getItemInMainHand();
						ItemMeta mhm = mh.getItemMeta();
						
						
						//Check the lore for specific data in this case we check if it is Str Infusion 1
						if (mhm.getLore().contains("Str Infusion 1")) {
							//Then we do whatever effect we want to wether its add damage add health or add a potion effect
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
							
							//This line tells the plugin that it no longer needs to check more things and can stop here. This prevents lag by preventing unNeeded code from running
							return;
						}
						
						//Check the lore for specific data in this case we check if it is Regen Infusion 1
						if (mhm.getLore().contains("Regen Infusion 1")) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
						}
						
						//Check the lore for specific data in this case we check if it is Might Infusion 1
						if (mhm.getLore().contains("Might Infusion 1")) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
							e.setDamage(e.getDamage()+5);
						}
						
					}
					
					
				}
				
			}
			
		}
		
		//Same sanity checks as before with the weapons
		if (e.getEntityType().equals(EntityType.PLAYER)) {
			
			//Loading player and thier armor contents into a variable to use later
			Player p = Bukkit.getPlayer(e.getEntity().getName());
			
			ItemStack[] IS = p.getInventory().getArmorContents();
			
			//Check if the player has a chestplate equiped
			if (!(p.getInventory().getChestplate() == null)) {
			
				//If they do is it an Infused Str?
				if (p.getInventory().getChestplate().equals(InfusedArmor.StrNChest())) {
					//Setting variables to save data for later when we check what each armor peice is to calculate set bonus
					sc = true;
					stc = stc+1;
					
				//If they do is it an Infused Regen?	
				}else if (p.getInventory().getChestplate().equals(InfusedArmor.RegenNChest())) {
					rc = true;
					rtc = rtc+1;
					
					
				}
				
			}
			
			//same as with the ChestPlate
			if (!(p.getInventory().getHelmet() == null)) {
				
				if (p.getInventory().getHelmet().equals(InfusedArmor.StrNHelm())) {
					sh = true;
					stc = stc+1;
					
					
				}else if (p.getInventory().getHelmet().equals(InfusedArmor.RegenNHelm())) {
					rh = true;
					rtc = rtc+1;
					
					
				}
				
			}
			
			//same as above
			if (!(p.getInventory().getLeggings() == null)) {
				
				if (p.getInventory().getLeggings().equals(InfusedArmor.StrNPants())) {
					sp = true;
					stc = stc+1;
					
					
				}else if (p.getInventory().getLeggings().equals(InfusedArmor.RegenNPants())) {
					rp = true;
					rtc = rtc+1;
					
					
				}
				
			}
			
			//same as above
			if (!(p.getInventory().getBoots() == null)) {
				
				if (p.getInventory().getBoots().equals(InfusedArmor.StrNBoots())) {
					sb = true;
					stc = stc+1;
					
					
				}else if (p.getInventory().getBoots().equals(InfusedArmor.RegenNBoots())) {
					rb = true;
					rtc = rtc+1;
					
					
				}
				
			}
			
			//Now check if its a Full Set or a partial set.
			if (sh && sc && sp && sb) {
				//If it is a Full Set of Str give them the full set bonus
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2));
			}else if (stc > 0) {
				//If they don't have a full set but have more than 0 peices calculate their total bonus in this case 100ticks of DAMAGE_REISTSANCE times however many peices the have on
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (100*stc), 1));
				
			}
			
			//same as above
			if (rh && rc && rp && rb) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 2));
			}else if (rtc > 0) {
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (100*rtc), 1));
				
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	//Example of how to add Class Restrictions on Armor
	@EventHandler
	public void onEquip(ArmorEquipEvent e) {
		
		//In this case we check if they are tank class and if they are not we run the following code
		if (!config.getString("PlayerData."+e.getPlayer().getName()+".Class").equalsIgnoreCase("Tank")) {
			
			//Check if the new Armor is a Netherite ChestPlate
			if (e.getNewArmorPiece().getType().equals(Material.NETHERITE_CHESTPLATE)) {
				//IF it IS then send them a message
				e.getPlayer().sendMessage(ChatColor.RED+"Only Tanks may wear Netherite");
				
				//Then cancel the action
				e.setCancelled(true);
				
				//Typically there would be a return; at the end of each of these but I got lazy
			}else if (e.getNewArmorPiece().getType().equals(Material.NETHERITE_HELMET)) {
				e.getPlayer().sendMessage(ChatColor.RED+"Only Tanks may wear Netherite");
				e.setCancelled(true);
			}else if (e.getNewArmorPiece().getType().equals(Material.NETHERITE_LEGGINGS)) {
				e.getPlayer().sendMessage(ChatColor.RED+"Only Tanks may wear Netherite");
				e.setCancelled(true);
			}else if (e.getNewArmorPiece().getType().equals(Material.NETHERITE_BOOTS)) {
				e.getPlayer().sendMessage(ChatColor.RED+"Only Tanks may wear Netherite");
				e.setCancelled(true);
			}
			

		}
	}
	
}
