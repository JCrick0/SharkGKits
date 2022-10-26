package net.sharkdevelopment.gkits.menus.submenus;

import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Map;

public class ArmorPieceSelectMenu extends Menu {

    private final ConfigCursor configCursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.KIT-PIECES");
    private final Gkit gkit;

    public ArmorPieceSelectMenu(Gkit gkit) {
        this.gkit = gkit;

        this.setAutoUpdate(true);
        this.setUpdateAfterClick(true);

        if (configCursor.getBoolean("FILL-BUTTONS.ENABLED")) {
            this.setPlaceholder(true);
            this.setPlaceholderButton(new Button() {
                @Override
                public ItemStack getButtonItem(Player var1) {
                    return ItemMaker.of(Material.STAINED_GLASS_PANE).data((short) configCursor.getInt("FILL-BUTTONS.DATA")).build();
                }
            });
        }
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate(configCursor.getString("TITLE").replace("<gkit-name>", gkit.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        int i = 0;
        for (ItemStack stack : gkit.getArmorContents()) {
            ItemStack item = stack.clone();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.emptyList());
            item.setItemMeta(meta);

            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return item;
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                    new PieceCustomEnchantMenu(gkit, gkit.getArmorContents(), stack).openMenu(player);
                }
            });
            i++;
        }

        return buttons;
    }
}
