package dev.ukry.gkits.manager.listener;

import dev.ukry.gkits.utils.event.ArmorEquipEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class EnchantsListener implements Listener {

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack nw = event.getNewArmorPiece();
        ItemStack old = event.getOldArmorPiece();
        Player player = event.getPlayer();
        if(nw.hasItemMeta()) {
            ItemMeta meta = nw.getItemMeta();
            List<String> lore = meta.getLore().stream().map(ChatColor::stripColor).collect(Collectors.toList());
            if(lore.contains("SPEED")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            } else if(lore.contains("FIRE_RESISTANCE")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
            } else if (lore.contains("INVISIBILITY")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
            }
        }
        if(old == null) return;
        if(!old.hasItemMeta()) return;
        ItemMeta meta = old.getItemMeta();
        if(meta.getLore() == null) return;
        if(meta.getLore().isEmpty()) return;
        List<String> lore = meta.getLore().stream().map(ChatColor::stripColor).collect(Collectors.toList());
        if(lore.contains("SPEED")) {
            player.removePotionEffect(PotionEffectType.SPEED);
        } else if(lore.contains("FIRE_RESISTANCE")) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        } else if(lore.contains("INVISIBILITY")) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }
}