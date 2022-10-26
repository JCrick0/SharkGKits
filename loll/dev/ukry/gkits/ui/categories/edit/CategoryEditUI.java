package dev.ukry.gkits.ui.categories.edit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.categories.CategoriesListUI;
import dev.ukry.gkits.ui.categories.CategoryLoreUI;
import dev.ukry.gkits.ui.categories.CategorySelectSlotUI;
import dev.ukry.gkits.utils.CC;
import dev.ukry.gkits.utils.item.ItemBuilder;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CategoryEditUI extends FastInv {

    private static FileConfiguration config = SharkGKits.getInstance().getConfig();

    public CategoryEditUI(Category category) {
        super(config.getInt("edit-gui.category.rows") * 9, CC.translate(config.getString("edit-gui.category.title")
                .replace("%category%", category.getName())));

        /*setItems(getBorders(), new ItemBuilder(
                Material.valueOf(config.getString("edit-gui.category.material")))
                .name("")
                .data(config.getInt("edit-gui.category.data"))
                .build());
         */

        //Toggle category
        setItem(config.getInt("edit-gui.toggle-category.slot"),
                new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .name(config.getString("edit-gui.toggle-category.name"))
                        .lore(CC.GREEN + "Actual: " + category.isEnabled()).build(), e -> {
            category.setEnabled(!category.isEnabled());
            category.save();
            new CategoryEditUI(category).open((Player) e.getWhoClicked());
        });
        //Change display name
        setItem(config.getInt("edit-gui.change-name.slot"),
                new ItemBuilder(Material.NAME_TAG)
                        .name(config.getString("edit-gui.change-name.name"))
                        .lore(CC.translate("&aActual: " + category.getIcon().getItemMeta().getDisplayName())).build(), e -> {
            Player player = (Player) e.getWhoClicked();
            if(player.hasMetadata("EDITING_CATEGORY_NAME")) {
                player.closeInventory();
                player.sendMessage(CC.RED + "You already editing a one category!");
            } else {
                player.setMetadata("EDITING_CATEGORY_NAME", new FixedMetadataValue(SharkGKits.getInstance(), category.getName()));
                player.closeInventory();
                player.sendMessage(CC.GREEN + "Ready, now write in the chat the new displayname!");
            }
        });
        //Change inventory size
        setItem(config.getInt("edit-gui.change-size.slot"),
                new ItemBuilder(Material.BOOK)
                        .name(config.getString("edit-gui.change-size.name"))
                        .lore(CC.GREEN + "Actual: " + category.getInvSize()).build(), e -> {
            Player player = (Player) e.getWhoClicked();
            if(player.hasMetadata("EDITING_CATEGORY_SIZE")) {
                player.closeInventory();
                player.sendMessage(CC.RED + "You already editing a one category!");
            } else {
                player.setMetadata("EDITING_CATEGORY_SIZE", new FixedMetadataValue(SharkGKits.getInstance(), category.getName()));
                player.closeInventory();
                player.sendMessage(CC.GREEN + "Ready, now write in the chat the new size(rows).");
            }
        });
        setItem(config.getInt("edit-gui.change-inv.slot"),
                new ItemBuilder(Material.STICK)
                        .name(config.getString("edit-gui.change-inv.name"))
                        .lore("Actual: " + category.getSlot()).build(), event -> {
            new CategorySelectSlotUI(category).open((Player) event.getWhoClicked());
        });
        //Change icon
        setItem(config.getInt("edit-gui.change-icon.slot"),
                new ItemBuilder(category.getIcon())
                        .name(config.getString("edit-gui.change-icon.name"))
                        .build(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.setMetadata("CLICK_", new FixedMetadataValue(SharkGKits.getInstance(), category.getName()));
            player.sendMessage(CC.GREEN + "Now click in your inventory!");
        });
        setItem(config.getInt("edit-gui.glow.slot"),
                new ItemBuilder(Material.ENCHANTMENT_TABLE)
                        .name(config.getString("edit-gui.glow.name"))
                        .lore("Glow: " + category.isGlow()).build(), event -> {
            category.setGlow(!category.isGlow());
            category.save();
            new CategoryEditUI(category).open((Player) event.getWhoClicked());
        });
        //Back
        setItem(config.getInt("edit-gui.change-lore.slot"),
                new ItemBuilder(Material.PAPER)
                        .name(config.getString("edit-gui.change-lore.name"))
                        .build(), event -> {
            new CategoryLoreUI(category).open((Player) event.getWhoClicked());
        });
        setItem(config.getInt("edit-gui.button-back.slot"),
                new ItemBuilder(Material.REDSTONE)
                        .name(config.getString("edit-gui.button-back.name"))
                        .build(), e -> {
            Player player = (Player) e.getWhoClicked();
            new CategoriesListUI(c -> new CategoryEditUI(c).open(player)).open(player);
        });

    }
}