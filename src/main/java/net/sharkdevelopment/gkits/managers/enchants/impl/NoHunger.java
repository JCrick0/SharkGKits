package net.sharkdevelopment.gkits.managers.enchants.impl;

import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorEquipEvent;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorUnEquipEvent;
import net.sharkdevelopment.lib.maker.ItemMaker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.stream.Collectors;

public class NoHunger extends CustomEnchant {

    public NoHunger() {
        super("No Hunger",
                ItemMaker.of(Material.COOKED_BEEF)
                        .displayName("&bNo Hunger")
                        .build()
        );
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();

        ItemStack[] playerArmors = event.getArmor();
        for (ItemStack armor : playerArmors) {
            if (armor.hasItemMeta()) {
                if (armor.getItemMeta().hasLore()) {
                    List<String> lore = armor.getItemMeta().getLore().stream().map(ChatColor::stripColor).collect(Collectors.toList());
                    if (lore.contains("No Hunger")) {
                        player.setMetadata("nohunger", new FixedMetadataValue(SharkGKits.getInstance(), true));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUnEquip(ArmorUnEquipEvent event) {
        ItemStack item = event.getItem();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasLore()) {
                item.getItemMeta().getLore().forEach(lines -> {
                    if (lines.contains("No Hunger")) event.getPlayer().removeMetadata("nohunger", SharkGKits.getInstance());
                });
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasMetadata("nohunger")) {
                event.setCancelled(true);
                event.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasMetadata("nohunger")) {
            event.getEntity().removeMetadata("nohunger", SharkGKits.getInstance());
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
