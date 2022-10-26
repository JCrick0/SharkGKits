package net.sharkdevelopment.gkits.menus.submenus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.menus.KitManageMenu;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SlotMenu extends Menu {

    private final ConfigCursor cursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.SELECT-SLOT");
    private final ConfigCursor configCursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.SELECT-SLOT.ITEMS");
    private final Gkit gkit;

    public SlotMenu(Gkit gkit) {
        this.gkit = gkit;

        this.setAutoUpdate(true);
        this.setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player var1) {
        return CC.translate(cursor.getString("TITLE"));
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player var1) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        List<Integer> filled = Lists.newArrayList();
        for (Gkit kit : Gkit.getGkits().values()) {
            buttons.put(kit.getSlot(), new Button() {
                @Override
                public ItemStack getButtonItem(Player var1) {
                    return ItemMaker.of(Material.REDSTONE)
                            .displayName("&cFilled slot!")
                            .build();
                }
            });
            filled.add(kit.getSlot());
        }

        for (int slot = 0; slot < this.getSize(); ++slot) {
            if (filled.contains(slot)) continue;
            buttons.put(slot, new Button() {
                @Override
                public ItemStack getButtonItem(Player var1) {
                    return ItemMaker.of(Material.valueOf(configCursor.getString("SELECT-SLOT.MATERIAL")))
                            .data((short) configCursor.getInt("SELECT-SLOT.DATA"))
                            .displayName(configCursor.getString("SELECT-SLOT.NAME"))
                            .lore(configCursor.getStringList("SELECT-SLOT.LORE"))
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    gkit.setSlot(slot);
                    player.sendMessage(String.valueOf(slot));
                    player.closeInventory();
                    new KitManageMenu(gkit).openMenu(player);
                }
            });
        }
        return buttons;
    }
}
