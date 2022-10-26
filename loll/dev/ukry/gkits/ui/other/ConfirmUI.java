package dev.ukry.gkits.ui.other;

import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ConfirmUI extends FastInv {

    public ConfirmUI(Consumer<InventoryClickEvent> confirm, Consumer<InventoryClickEvent> deny) {
        super(27, "You are sure?");
        setItem(11, new ItemBuilder(Material.WOOL).data(5).name(CC.GREEN + "Confirm").build(), confirm);
        setItem(15, new ItemBuilder(Material.WOOL).data(14).name(CC.RED + "Deny").build(), deny);
    }
}