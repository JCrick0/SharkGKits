package dev.ukry.gkits.ui.gkit.edit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.ui.gkit.GkitListUI;
import dev.ukry.gkits.ui.gkit.GkitLoreUI;
import dev.ukry.gkits.ui.gkit.GkitSelectSlotUI;
import dev.ukry.gkits.utils.CC;
import dev.ukry.gkits.utils.item.ItemBuilder;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class GKitEditUI extends FastInv {

    public GKitEditUI(Gkit gkit) {
        super(45, "Edit the gkit " + gkit.getName());
        //Change loot
        setItem(12, new ItemBuilder(Material.STONE_AXE).name("Change loot").build(), event -> {
            Player player = (Player) event.getWhoClicked();
            ItemStack[] displayContents = new ItemStack[54];
            ItemStack[] armorContents = player.getInventory().getArmorContents();
            ItemStack[] contents = player.getInventory().getContents();
            /*System.arraycopy(contents, 0, displayContents, armorContents.length, contents.length);
            System.arraycopy(armorContents, 0, displayContents, 0, armorContents.length);*/
            //36/44|45|53 Refill
            System.arraycopy(contents, 0, displayContents,0, contents.length);
            System.arraycopy(armorContents, 0, displayContents, 46, armorContents.length);
            for(int i = 36; i < 46; i++) {
                displayContents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
            }
            displayContents[50] = new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build();
            displayContents[51] = new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build();
            displayContents[52] = new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build();
            displayContents[53] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
            gkit.setContents(contents);
            gkit.setArmorContents(armorContents);
            gkit.setDisplayContents(displayContents);
            gkit.save();
            player.sendMessage(CC.GREEN + "Successfully updated!");
        });
        //Change display name
        setItem(13,
                new ItemBuilder(Material.NAME_TAG)
                        .name("Change displayname")
                        .lore(CC.GREEN + "Actual: " + gkit.getIcon().getItemMeta().getDisplayName())
                        .build(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.setMetadata("GKIT_EDIT_NAME", new FixedMetadataValue(SharkGKits.getInstance(), gkit.getName()));
            player.closeInventory();
            player.sendMessage(CC.GREEN + "Ready, now write in the chat the new name!");
        });
        //Change permission
        setItem(14,
                new ItemBuilder(Material.BOOK)
                        .name("Change permission")
                        .lore(CC.GREEN + "Actual: " + gkit.getPermission())
                        .build(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.setMetadata("GKIT_EDIT_PERMISSION", new FixedMetadataValue(SharkGKits.getInstance(), gkit.getName()));
            player.closeInventory();
            player.sendMessage(CC.GREEN + "Ready, now write the new permission in the chat");
        });
        setItem(20, new ItemBuilder(Material.REDSTONE_TORCH_ON).name("toogle").lore(CC.GREEN + "Actual: " + gkit.isEnabled()).build(), event -> {
            gkit.setEnabled(!gkit.isEnabled());
            gkit.save();
            Bukkit.getScheduler().runTaskLater(SharkGKits.getInstance(), () -> new GKitEditUI(gkit).open((Player) event.getWhoClicked()), 5L);
        });
        setItem(21, new ItemBuilder(Material.WATCH).name("Change cooldown").lore(CC.GREEN + "Actual cooldown: " + gkit.getCooldown() + " seconds").build(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.setMetadata("GKIT_EDIT_COOLDOWN", new FixedMetadataValue(SharkGKits.getInstance(), gkit.getName()));
            player.closeInventory();
            player.sendMessage(CC.GREEN + "Ready, now write the new cooldown in the chat");
        });
        setItem(22, new ItemBuilder(Material.STICK).name("Set slot").lore(CC.GREEN + "Actual: " + gkit.getSlot()).build(), event -> {
            new GkitSelectSlotUI(gkit).open((Player) event.getWhoClicked());
        });
        setItem(23, new ItemBuilder(gkit.getIcon().getType()).data(gkit.getIcon().getDurability()).name("Change icon").build(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.setMetadata("CLICK", new FixedMetadataValue(SharkGKits.getInstance(), gkit.getName()));
            player.sendMessage(CC.GREEN + "Now click in your inventory!");
        });
        setItem(24, new ItemBuilder(Material.ENCHANTMENT_TABLE).name("Glow").lore("Glow: " + gkit.getIcon().containsEnchantment(new ItemBuilder.Glow())).build(), event -> {
            gkit.setIcon(gkit.getIcon().containsEnchantment(new ItemBuilder.Glow()) ? new ItemBuilder(gkit.getIcon()).removeGlow().build() : new ItemBuilder(gkit.getIcon()).glow().build());
            gkit.save();
            new GKitEditUI(gkit).open((Player) event.getWhoClicked());
        });
        setItem(31, new ItemBuilder(Material.PAPER).name("Change lore").build(), event -> {
            new GkitLoreUI(gkit).open((Player) event.getWhoClicked());
        });
        setItem(34, new ItemBuilder(Material.REDSTONE).name("Back").build(), event -> {
            new GkitListUI(Category.valueOf(gkit.getCategory()), g -> new GKitEditUI(g).open((Player) event.getWhoClicked())).open((Player) event.getWhoClicked());
        });
    }
}