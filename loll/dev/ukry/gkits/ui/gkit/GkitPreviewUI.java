package dev.ukry.gkits.ui.gkit;

import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GkitPreviewUI extends FastInv {

    private Consumer<InventoryCloseEvent> closeEventConsumer;

    public GkitPreviewUI(Gkit gkit) {
        super(54, CC.GREEN + "Preview of " + gkit.getName());
        ItemStack[] c = gkit.getDisplayContents();
        for(int i = 0; i < c.length; i++) {
            ItemStack s = c[i];
            if(s == null) continue;
            if(s.getType() == Material.REDSTONE) {
                setItem(i, s, e -> {
                    Player player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    Bukkit.dispatchCommand(player, "gkit");
                });
            } else {
                setItem(i, s);
            }
        }
//        Arrays.stream(gkit.getDisplayContents()).forEach(this::addItem);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(closeEventConsumer == null) return;
        event.setCancelled(!event.getWhoClicked().isOp());
    }

    public GkitPreviewUI setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
        this.closeEventConsumer = closeEvent;
        return this;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(closeEventConsumer == null) return;
        closeEventConsumer.accept(event);
    }
}