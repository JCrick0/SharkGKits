package dev.ukry.gkits.ui.categories;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Config;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.utils.item.ItemBuilder;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class CategoriesListUI extends FastInv {

    private Consumer<InventoryCloseEvent> closeEventConsumer;

    public CategoriesListUI(Consumer<Category> function) {
        super(Config.GUI_CATEGORIES_ROWS.getAsInt() * 9, Locale.GUI_CATEGORIES_TITLE.getString());
        for(Category category : Category.values()) {
            if(category.isGlow()) {
                setItem(category.getSlot(), new ItemBuilder(category.getIcon()).glow().build(), e -> function.accept(category));
            } else {
                setItem(category.getSlot(), category.getIcon(), e -> function.accept(category));
            }
        }
        if(!SharkGKits.getInstance().getConfig().getBoolean("gkits.refill-glass.enabled")) return;
        ItemStack[] contents = getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if(stack == null) {
                contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name(" ")
                        .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                        .build();
            } else {
                if(stack.getType() == Material.AIR) {
                    contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .name(" ")
                            .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                            .build();
                }
            }
        }
        getInventory().setContents(contents);
    }

    public CategoriesListUI setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
        this.closeEventConsumer = closeEvent;
        return this;
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        if(closeEventConsumer == null) return;
        closeEventConsumer.accept(event);
    }
}