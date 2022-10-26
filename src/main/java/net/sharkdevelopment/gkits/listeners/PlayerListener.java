package net.sharkdevelopment.gkits.listeners;

import com.google.common.collect.Lists;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.managers.profile.Profile;
import net.sharkdevelopment.gkits.menus.KitManageMenu;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.DateUtils;
import net.sharkdevelopment.lib.config.ConfigCursor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerListener implements Listener {

    private final ConfigCursor cursor;

    public PlayerListener() {
        this.cursor = new ConfigCursor(SharkGKits.getInstance().getMessagesConfig(), "");
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();
        if (cursorItem == null || currentItem == null || cursorItem.getType().equals(Material.AIR) || currentItem.getType().equals(Material.AIR)) {
            return;
        }
        if (currentItem.getType().name().endsWith("_HELMET") || currentItem.getType().name().endsWith("_CHESTPLATE") || currentItem.getType().name().endsWith("_LEGGINGS") || currentItem.getType().name().endsWith("_BOOTS")) {
            CustomEnchant.getCustomEnchants().stream().filter(customEnchant -> cursorItem.isSimilar(customEnchant.getItemStack())).limit(1L).forEach(ce -> {
                //event.setCancelled(true);
                CustomEnchant.applyCustomEnchant(currentItem, ce);
                player.setItemOnCursor(null);
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0f, 1.0f);
            });
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) && player.isSneaking()) {
            Profile profile = Profile.getProfile(player);
            assert profile != null;
            for (Map.Entry<Boolean, Gkit> entry : profile.getEditingContents().entrySet()) {
                if (entry.getKey()) {
                    if (event.getItem() == null) {
                        player.sendMessage(CC.translate(cursor.getString("HAND-ITEM-NULL")));
                        return;
                    }

                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.sendMessage(CC.translate(cursor.getString("CHANGE-GAMEMODE")));
                        return;
                    }

                    entry.getValue().setArmorContents(player.getInventory().getArmorContents());
                    entry.getValue().setContents(player.getInventory().getContents());

                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);

                    profile.getEditingContents().clear();

                    player.sendMessage(CC.translate(cursor.getString("CONTENTS-SAVED")));

                    new KitManageMenu(entry.getValue()).openMenu(player);
                }
            }
            for (Map.Entry<Boolean, Gkit> entry : profile.getEditingLore().entrySet()) {
                if (entry.getKey()) {
                    if (event.getItem().getType().equals(Material.WRITTEN_BOOK)) {
                        if (event.getItem() == null) {
                            player.sendMessage(CC.translate(cursor.getString("HAND-ITEM-NULL")));
                            return;
                        }

                        ItemStack writtenBook = player.getItemInHand();
                        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();

                        List<String> pages = bookMeta.getPages();
                        String[] content = new String[pages.size()];
                        pages.toArray(content);
                        String[] lore = content[0].split("\n");
                        List<String> description = Lists.newArrayList();
                        description.addAll(Arrays.asList(lore));
                        description.replaceAll(s -> s.replace("<cooldown>", DateUtils.formatDuration(entry.getValue().getCooldown())));
                        entry.getValue().setDescription(description);

                        player.getInventory().forEach(itemStack -> {
                            if (itemStack.getType().equals(Material.WRITTEN_BOOK)) itemStack.setType(Material.AIR);
                        });

                        player.sendMessage(CC.translate(cursor.getString("DESCRIPTION-SAVED")));

                        new KitManageMenu(entry.getValue()).openMenu(player);

                        profile.getEditingLore().clear();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfile(player);

        assert profile != null;

        for (Map.Entry<Boolean, Gkit> entry : profile.getEditingPermission().entrySet()) {
            if (entry.getKey()) {
                entry.getValue().setPermission(event.getMessage());
                player.sendMessage(CC.translate(cursor.getString("PERMISSION-CHANGED").replace("<permission>", event.getMessage())));

                profile.getEditingPermission().clear();

                new KitManageMenu(entry.getValue()).openMenu(player);
            }
        }
    }
}
