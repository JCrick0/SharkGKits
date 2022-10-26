package dev.ukry.gkits.serializable;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.files.ConfigFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Getter @Setter
public class Category {

    private final ConfigFile categoriesConfig = SharkGKits.getInstance().getCategoriesConfig();
    private String name;
    private boolean enabled;
    private Integer slot;
    private Integer invSize;
    private ItemStack icon;
    private boolean glow;

    public void save() {
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".ENABLED", enabled);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".SLOT", slot);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".INV_SIZE", invSize);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".ICON", SharkGKits.GSON.toJson(icon));
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".GLOW" , glow);
        categoriesConfig.save();
        categoriesConfig.reload();
    }

    public void remove() {
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".ENABLED", null);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".SLOT", null);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".INV_SIZE", null);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".ICON", null);
        categoriesConfig.getConfiguration().set("CATEGORIES." + name.toUpperCase(Locale.ROOT) + ".GLOW" , null);
        categoriesConfig.save();
        categoriesConfig.reload();
    }

    public static Category valueOf(String input) {
        ConfigFile categoriesConfig = SharkGKits.getInstance().getCategoriesConfig();
        if(categoriesConfig.getConfiguration().getConfigurationSection("CATEGORIES") == null) return null;
        if(categoriesConfig.getConfiguration().getConfigurationSection("CATEGORIES").getKeys(false).isEmpty()) return null;
        if(!categoriesConfig.getConfiguration().getConfigurationSection("CATEGORIES").getKeys(false).contains(input.toUpperCase(Locale.ROOT))) return null;
        if(categoriesConfig.getConfiguration().get("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".ENABLED") == null) return null;
        return new Category(input.toUpperCase(Locale.ROOT),
                categoriesConfig.getBoolean("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".ENABLED"),
                categoriesConfig.getInt("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".SLOT"),
                categoriesConfig.getInt("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".INV_SIZE"),
                SharkGKits.GSON.fromJson(categoriesConfig.getString("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".ICON"), ItemStack.class),
                categoriesConfig.getBoolean("CATEGORIES." + input.toUpperCase(Locale.ROOT) + ".GLOW")
        );
    }

    public static Category[] values() {
        ConfigFile categoriesConfig = SharkGKits.getInstance().getCategoriesConfig();
        return categoriesConfig.getConfiguration().getConfigurationSection("CATEGORIES").getKeys(false).stream().map(Category::valueOf).filter(Objects::nonNull).toArray(Category[]::new);
    }
}