package net.sharkdevelopment.gkits.managers.event;

import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorEquipEvent;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorUnEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class ArmorListener implements Listener{

	private final List<Material> armors = Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
			Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
			Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
			Material.GOLD_BOOTS, Material.GOLD_LEGGINGS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET,
			Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET);

	@EventHandler
	public void onArmorAddInvClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick()) {
				armors.forEach(armorItem -> {
					if (event.getCursor().getType() == armorItem || event.getCurrentItem().getType() == armorItem) {
						new BukkitRunnable() {
							@Override
							public void run() {
								ItemStack[] armor = player.getInventory().getArmorContents();
								Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(player, armor));
							}
						}.runTaskLater(SharkGKits.getInstance(), 1L);
					}
				});
			}
		}
	}

	@EventHandler
	public void onArmorRemoveInvClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
				armors.forEach(armorItem -> {
					if (event.getCurrentItem().getType() == armorItem) {
						ItemStack item = event.getCurrentItem();
						Bukkit.getPluginManager().callEvent(new ArmorUnEquipEvent(player, item));
					}
				});
			}
		}
	}

	@EventHandler
	public void onArmorBreak(PlayerItemBreakEvent event) {
		Material material = event.getBrokenItem().getType();
		if (material.name().endsWith("_HELMET")
				|| material.name().endsWith("_CHESTPLATE")
				|| material.name().endsWith("_LEGGINGS")
				|| material.name().endsWith("_BOOTS")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Bukkit.getPluginManager().callEvent(
							new ArmorUnEquipEvent(event.getPlayer(), event.getBrokenItem()));
				}
			}.runTaskLater(SharkGKits.getInstance(), 1L);
		}
	}

	@EventHandler
	public void onArmorInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.hasItem()) {
				if (event.getItem().hasItemMeta()) {
					armors.forEach(armors -> {
						if (event.getItem().getType() == armors) {
							new BukkitRunnable() {
								@Override
								public void run() {
									Bukkit.getPluginManager().callEvent(
											new ArmorEquipEvent(event.getPlayer(), event.getPlayer().getEquipment().getArmorContents()));
								}
							}.runTaskLater(SharkGKits.getInstance(), 1L);
						}
					});
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(player, player.getInventory().getArmorContents()));
	}
}
