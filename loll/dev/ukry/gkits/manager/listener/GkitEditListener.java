package dev.ukry.gkits.manager.listener;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Config;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.ui.gkit.GkitLoreUI;
import dev.ukry.gkits.ui.gkit.edit.GKitEditUI;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.ItemBuilder;
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

public class GkitEditListener implements Listener {

    private final SharkGKits plugins = SharkGKits.getInstance();

    @EventHandler
    public void onChangeLore(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!player.hasMetadata("EDIT_LORE")) return;
        String msg = event.getMessage();
        event.setCancelled(true);
        Gkit gkit = SharkGKits.getInstance().getGkitsHandler().getGkit(player.getMetadata("EDIT_LORE").get(0).asString());
        if(!msg.equalsIgnoreCase("cancel")) {
            int i = player.getMetadata("LORE_EDIT").get(0).asInt();
            String[] desc = gkit.getDescription();
            desc[i] = msg;
            gkit.setDescription(desc);
            player.sendMessage(CC.GREEN + "Successfully updated!");
        } else {
            player.sendMessage(CC.RED + "Cancelled!");
        }
        player.removeMetadata("EDIT_LORE", SharkGKits.getInstance());
        player.removeMetadata("LORE_EDIT", SharkGKits.getInstance());
        new GkitLoreUI(gkit).open(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!Config.CATEGORIES.getAsBoolean()) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if(event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if(!player.hasMetadata("CLICK")) return;
        if(event.getCurrentItem().getType() == Material.AIR) return;
        Gkit gkit = SharkGKits.getInstance().getGkitsHandler().getGkit(player.getMetadata("CLICK").get(0).asString());
        ItemStack stack = gkit.getIcon();
        ItemMeta meta = stack.getItemMeta();
        gkit.setIcon(new ItemBuilder(event.getCurrentItem().getType()).name(meta.getDisplayName()).lore(meta.getLore()).data(event.getCurrentItem().getDurability()).build());
        player.removeMetadata("CLICK", SharkGKits.getInstance());
        new GKitEditUI(gkit).open(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!Config.CATEGORIES.getAsBoolean()) return;
        Player player = (Player) event.getPlayer();
        if(!player.hasMetadata("CLICK")) return;
        player.removeMetadata("CLICK", SharkGKits.getInstance());
        player.sendMessage(CC.RED + "Cancelled");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(player.hasMetadata("GKIT_EDIT_NAME")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            Gkit gkit = plugins.getGkitsHandler().getGkit(player.getMetadata("GKIT_EDIT_NAME").get(0).asString());
            ItemMeta meta = gkit.getIcon().getItemMeta();
            meta.setDisplayName(CC.translate(msg));
            gkit.getIcon().setItemMeta(meta);
            gkit.save();
            player.removeMetadata("GKIT_EDIT_NAME", SharkGKits.getInstance());
            player.sendMessage(CC.GREEN + "Successfully updated!");
            new GKitEditUI(gkit).open(player);
        } else if(player.hasMetadata("GKIT_EDIT_PERMISSION")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            Gkit gkit = plugins.getGkitsHandler().getGkit(player.getMetadata("GKIT_EDIT_PERMISSION").get(0).asString());
            gkit.setPermission(msg);
            gkit.save();
            player.removeMetadata("GKIT_EDIT_PERMISSION", SharkGKits.getInstance());
            player.sendMessage(CC.GREEN + "Successfully updated!");
            new GKitEditUI(gkit).open(player);
        } else if(player.hasMetadata("GKIT_EDIT_COOLDOWN")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            Gkit gkit = plugins.getGkitsHandler().getGkit(player.getMetadata("GKIT_EDIT_COOLDOWN").get(0).asString());
            if(msg.contains("h")) {
                String time = msg.replace("h", "");
                try {
                    gkit.setCooldown(Integer.parseInt(time) * 60 * 60);
                    gkit.save();
                    player.sendMessage(CC.GREEN + "Successfully updated!");
                    new GKitEditUI(gkit).open(player);
                } catch (NumberFormatException e){
                    player.sendMessage(CC.RED + time + " no is a number");
                }
            } else if(msg.contains("m")) {
                String time = msg.replace("m", "");
                try {
                    gkit.setCooldown(Integer.parseInt(time) * 60);
                    gkit.save();
                    player.sendMessage(CC.GREEN + "Successfully updated!");
                    new GKitEditUI(gkit).open(player);
                } catch (NumberFormatException e){
                    player.sendMessage(CC.RED + time + " no is a number");
                }
            } else if(msg.contains("s")) {
                String time = msg.replace("s", "");
                try {
                    gkit.setCooldown(Integer.parseInt(time));
                    gkit.save();
                    player.sendMessage(CC.GREEN + "Successfully updated!");
                    new GKitEditUI(gkit).open(player);
                } catch (NumberFormatException e){
                    player.sendMessage(CC.RED + time + " no is a number");
                }
            }else if(msg.contains("d")) {
                String time = msg.replace("d", "");
                try {
                    gkit.setCooldown(Integer.parseInt(time) * 60 * 60 * 24);
                    gkit.save();
                    player.sendMessage(CC.GREEN + "Successfully updated!");
                    new GKitEditUI(gkit).open(player);
                } catch (NumberFormatException e) {
                    player.sendMessage(CC.RED + time + " no is a number");
                }
            } else {
                player.sendMessage(CC.RED + "Error, format: 10h/m/s");
                return;
            }
            player.removeMetadata("GKIT_EDIT_COOLDOWN", SharkGKits.getInstance());
        }
    }
}