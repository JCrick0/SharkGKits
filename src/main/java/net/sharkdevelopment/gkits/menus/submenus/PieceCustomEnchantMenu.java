package net.sharkdevelopment.gkits.menus.submenus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieceCustomEnchantMenu extends Menu {

    private final ConfigCursor configCursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.PIECE-CUSTOM-ENCHANT");
    private final Gkit gkit;
    private final ItemStack[] contents;
    private final ItemStack armor;

    public PieceCustomEnchantMenu(Gkit gkit, ItemStack[] contents, ItemStack armor) {
        this.gkit = gkit;
        this.contents = contents;
        this.armor = armor;

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
        return CC.translate(configCursor.getString("TITLE"));
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        int i = 0;
        for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchants()) {
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {

                    return new ItemMaker(customEnchant.getItemStack().clone())
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                    ItemStack stack = armor.clone();
                    ItemMeta meta = stack.getItemMeta();
                    if (meta.getLore() != null) {
                        List<String> lore = meta.getLore();
                        lore.add(customEnchant.getName());
                        meta.setLore(lore);
                        stack.setItemMeta(meta);
                    } else {
                        List<String> lore = Lists.newArrayList();
                        lore.add(customEnchant.getName());
                        meta.setLore(lore);
                        stack.setItemMeta(meta);
                    }

                    List<ItemStack> items = Arrays.stream(contents).collect(Collectors.toList());
                    items.set(items.indexOf(armor), stack);
                    ItemStack[] contents = items.toArray(new ItemStack[0]);

                    gkit.setArmorContents(contents);

                    player.sendMessage(CC.translate("&bAdded custom enchant!"));
                }
            });
            i++;
        }

        return buttons;
    }


}
