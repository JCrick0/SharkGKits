package dev.ukry.gkits.ui.categories;

import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.categories.edit.CategoryEditUI;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author UKry
 * @Project categorys
 **/

public class CategorySelectSlotUI extends FastInv {

    private final Category category;

    public CategorySelectSlotUI(Category category) {
        super(54, CC.GREEN + "Select one slot");
        this.category = category;
        List<Category> categorys = Arrays.stream(Category.values()).collect(Collectors.toList());
        if(categorys.isEmpty()) return;
        categorys.forEach(g -> {
            setItem(g.getSlot(), new ItemBuilder(Material.STAINED_GLASS_PANE).name(CC.RED + "Used").data(7).build());
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem().getType() != Material.AIR) return;
        category.setSlot(event.getSlot());
        category.save();
        new CategoryEditUI(category).open((Player) event.getWhoClicked());
    }
}