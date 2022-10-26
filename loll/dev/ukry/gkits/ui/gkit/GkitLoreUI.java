package dev.ukry.gkits.ui.gkit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.ui.other.ConfirmUI;
import dev.ukry.gkits.ui.gkit.edit.GKitEditUI;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.metadata.FixedMetadataValue;

public class GkitLoreUI extends FastInv {

    public GkitLoreUI(Gkit gkit) {
        super(54, CC.GREEN + "Change lore of: " + gkit.getIcon().getItemMeta().getDisplayName());
        String[] lores = gkit.getDescription();
        for(int i = 0; i < lores.length; i++) {
            String lore = lores[i];
            int finalI = i;
            int finalI1 = i;
            setItem(i, new ItemBuilder(Material.PAPER).name(lore).build(), event -> {
                if(event.getClick() == ClickType.LEFT) {
                    Player player = (Player) event.getWhoClicked();
                    player.setMetadata("EDIT_LORE", new FixedMetadataValue(SharkGKits.getInstance(), gkit.getName()));
                    player.setMetadata("LORE_EDIT", new FixedMetadataValue(SharkGKits.getInstance(), finalI));
                    player.closeInventory();
                    player.sendMessage(CC.GREEN + "Ready, now write in the chat the new text");
                } else {
                    new ConfirmUI(confirmEvent -> {
                        String[] strings = gkit.getDescription();
                        String[] ns = new String[strings.length - 1];
                        for(int b = 0, k = 0; b < strings.length; b++) {
                            if(b == finalI1) {
                                continue;
                            }
                            ns[k++] = strings[b];
                        }
                        gkit.setDescription(ns);
                        new GkitLoreUI(gkit).open((Player) event.getWhoClicked());
                    }, denyEvent -> {
                        new GkitLoreUI(gkit).open((Player) event.getWhoClicked());
                    }).open((Player) event.getWhoClicked());
                }
            });
        }
        setItem(43, new ItemBuilder(Material.WOOL).name(CC.GREEN + "Add Lore").build(), event -> {
            String[] strings = new String[gkit.getDescription().length + 1];
            System.arraycopy(gkit.getDescription(), 0, strings, 0, gkit.getDescription().length);
            strings[strings.length - 1] = CC.GREEN + "New Line!";
            gkit.setDescription(strings);
            new GkitLoreUI(gkit).open((Player) event.getWhoClicked());
        });
        setItem(40, new ItemBuilder(Material.BOOK).name(CC.GREEN + "Actual").lore(lores).build());
        setItem(53, new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build(), event -> new GKitEditUI(gkit).open((Player) event.getWhoClicked()));
    }
}