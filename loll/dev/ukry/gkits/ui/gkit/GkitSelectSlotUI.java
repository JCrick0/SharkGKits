package dev.ukry.gkits.ui.gkit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.ui.gkit.edit.GKitEditUI;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public class GkitSelectSlotUI extends FastInv {

    private final Gkit gkit;

    public GkitSelectSlotUI(Gkit gkit) {
        super(54, CC.GREEN + "Select one slot");
        this.gkit = gkit;
        Category category = Category.valueOf(gkit.getCategory());
        List<Gkit> gkits = SharkGKits.getInstance().getGkitsHandler().
                getGkits()
                .values()
                .stream()
                .filter(g -> g.getCategory().equalsIgnoreCase(category.getName()))
                .collect(Collectors.toList());
        if(gkits.isEmpty()) return;
        gkits.forEach(g -> {
            setItem(g.getSlot(), new ItemBuilder(Material.STAINED_GLASS_PANE).name(CC.RED + "Used").data(7).build());
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem().getType() != Material.AIR) return;
        gkit.setSlot(event.getSlot()).save();
        new GKitEditUI(gkit).open((Player) event.getWhoClicked());
    }
}