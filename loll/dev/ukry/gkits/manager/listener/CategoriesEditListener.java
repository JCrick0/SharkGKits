package dev.ukry.gkits.manager.listener;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Config;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.categories.edit.CategoryEditUI;
import dev.ukry.gkits.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CategoriesEditListener implements Listener {

    @EventHandler
    public void onChangeLore(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!player.hasMetadata("_EDIT_LORE")) return;
        String msg = event.getMessage();
        event.setCancelled(true);
        Category category = Category.valueOf(player.getMetadata("_EDIT_LORE").get(0).asString());
        if(!msg.equalsIgnoreCase("cancel")) {
            int i = player.getMetadata("_LORE_EDIT").get(0).asInt();
            ItemStack stack = category.getIcon();
            ItemMeta meta = category.getIcon().getItemMeta();
            String[] lores = meta.getLore().toArray(new String[0]);
            lores[i] = msg;
            meta.setLore(Arrays.asList(lores));
            stack.setItemMeta(meta);
            category.setIcon(stack);
            category.save();
            player.sendMessage(CC.GREEN + "Successfully updated!");
        } else {
            player.sendMessage(CC.RED + "Cancelled!");
        }
        player.removeMetadata("_EDIT_LORE", SharkGKits.getInstance());
        player.removeMetadata("_LORE_EDIT", SharkGKits.getInstance());
        new CategoryEditUI(category).open(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!Config.CATEGORIES.getAsBoolean()) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if(event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if(!player.hasMetadata("CLICK_")) return;
        if(event.getCurrentItem().getType() == Material.AIR) return;
        Category category = Category.valueOf(player.getMetadata("CLICK_").get(0).asString());
        category.setIcon(new ItemStack(event.getCurrentItem()));
        category.save();
        player.removeMetadata("CLICK_", SharkGKits.getInstance());
        new CategoryEditUI(category).open(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(!player.hasMetadata("CLICK")) return;
        player.removeMetadata("CLICK", SharkGKits.getInstance());
        player.sendMessage(CC.RED + "Cancelled");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(!Config.CATEGORIES.getAsBoolean()) return;
        Player player = event.getPlayer();
        if(player.hasMetadata("EDITING_CATEGORY_NAME")) {
            String msg = event.getMessage();
            event.setCancelled(true);
            Category category = Category.valueOf(player.getMetadata("EDITING_CATEGORY_NAME").get(0).asString());
            ItemMeta meta = category.getIcon().getItemMeta();
            meta.setDisplayName(CC.translate(msg));
            category.getIcon().setItemMeta(meta);
            category.save();
            player.removeMetadata("EDITING_CATEGORY_NAME", SharkGKits.getInstance());
        } else if(player.hasMetadata("EDITING_CATEGORY_SIZE")) {
            String msg = event.getMessage();
            event.setCancelled(true);
            Category category = Category.valueOf(player.getMetadata("EDITING_CATEGORY_SIZE").get(0).asString());
            try {
                category.setInvSize(Integer.parseInt(msg) * 9);
                category.save();
                player.sendMessage(CC.GREEN + "You have changed the inventory size to " + msg + " rows");
            } catch (NumberFormatException e) {
                player.sendMessage(CC.RED + msg + " no is a number!");
            }
            player.removeMetadata("EDITING_CATEGORY_SIZE", SharkGKits.getInstance());
        }
    }
}