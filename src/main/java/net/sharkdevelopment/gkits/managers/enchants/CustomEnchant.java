package net.sharkdevelopment.gkits.managers.enchants;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.sharkdevelopment.gkits.SharkGKits;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomEnchant implements Listener {

    @Getter
    public static List<CustomEnchant> customEnchants = Lists.newArrayList();

    @Getter
    private String name;

    @Getter
    @Setter
    private ItemStack itemStack;

    public CustomEnchant(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack.clone();

        customEnchants.add(this);
    }

    public static CustomEnchant getByName(String name) {
        for (CustomEnchant customEnchant : customEnchants) {
            if (customEnchant.getName().equalsIgnoreCase(name)) return customEnchant;
        }
        return null;
    }

    public static void applyCustomEnchant(ItemStack itemStack, CustomEnchant customEnchant) {
        List<String> lore = Lists.newArrayList();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            lore.addAll(itemMeta.getLore());
        }
        if (!lore.isEmpty()) {
            for (int index = 0; index < lore.size(); ++index) {
                String line = lore.get(index);
                if (line.contains(ChatColor.stripColor(customEnchant.getName()))) {
                    lore.remove(index);
                }
            }
        }
        if (lore.contains(customEnchant.getName())) {
            return;
        }
        lore.add(customEnchant.getName());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void disable() {
        HandlerList.unregisterAll(this);
        this.onDisable();
    }

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, SharkGKits.getInstance());
        this.onEnable();
    }
}
