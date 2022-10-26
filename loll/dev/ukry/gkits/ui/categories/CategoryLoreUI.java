package dev.ukry.gkits.ui.categories;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.other.ConfirmUI;
import dev.ukry.gkits.ui.categories.edit.CategoryEditUI;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class CategoryLoreUI extends FastInv {

    public CategoryLoreUI(Category category) {
        super(54, CC.GREEN + "Change lore of: " + category.getIcon().getItemMeta().getDisplayName());
        String[] lores = category.getIcon().getItemMeta().getLore().toArray(new String[0]);
        for(int i = 0; i < lores.length; i++) {
            String lore = lores[i];
            int finalI = i;
            int finalI1 = i;
            setItem(i, new ItemBuilder(Material.PAPER).name(lore).build(), event -> {
                if(event.getClick() == ClickType.LEFT) {
                    Player player = (Player) event.getWhoClicked();
                    player.setMetadata("_EDIT_LORE", new FixedMetadataValue(SharkGKits.getInstance(), category.getName()));
                    player.setMetadata("_LORE_EDIT", new FixedMetadataValue(SharkGKits.getInstance(), finalI));
                    player.closeInventory();
                    player.sendMessage(CC.GREEN + "Ready, now write in the chat the new text");
                } else {
                    new ConfirmUI(confirmEvent -> {
                        String[] ns = new String[lores.length - 1];
                        for(int b = 0, k = 0; b < lores.length; b++) {
                            if(b == finalI1) {
                                continue;
                            }
                            ns[k++] = lores[b];
                        }
                        ItemStack stack = category.getIcon();
                        ItemMeta meta = category.getIcon().getItemMeta();
                        meta.setLore(Arrays.asList(ns));
                        stack.setItemMeta(meta);
                        category.setIcon(stack);
                        category.save();
                        new CategoryLoreUI(category).open((Player) event.getWhoClicked());
                    }, denyEvent -> {
                        new CategoryLoreUI(category).open((Player) event.getWhoClicked());
                    }).open((Player) event.getWhoClicked());
                }
            });
        }
        setItem(43, new ItemBuilder(Material.WOOL).name(CC.GREEN + "Add Lore").build(), event -> {
            String[] strings = new String[lores.length + 1];
            System.arraycopy(lores, 0, strings, 0, lores.length);
            strings[strings.length - 1] = CC.GREEN + "New Line!";
            ItemStack stack = category.getIcon();
            ItemMeta meta = category.getIcon().getItemMeta();
            meta.setLore(Arrays.asList(strings));
            stack.setItemMeta(meta);
            category.setIcon(stack);
            category.save();
            new CategoryLoreUI(category).open((Player) event.getWhoClicked());
        });
        setItem(40, new ItemBuilder(Material.BOOK).name(CC.GREEN + "Actual").lore(lores).build());
        setItem(53, new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build(), event -> new CategoryEditUI(category).open((Player) event.getWhoClicked()));
    }
}