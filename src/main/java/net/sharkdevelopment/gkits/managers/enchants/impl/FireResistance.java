package net.sharkdevelopment.gkits.managers.enchants.impl;

import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorEquipEvent;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorUnEquipEvent;
import net.sharkdevelopment.lib.maker.ItemMaker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class FireResistance extends CustomEnchant {

    public FireResistance() {
        super("Fire Resistance",
                ItemMaker.of(Material.POTION)
                        .data((short) 16419)
                        .displayName("&bFire resistance")
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
                    if (lore.contains("Fire Resistance")) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
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
                    if (lines.contains("Fire Resistance")) event.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                });
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
