package net.sharkdevelopment.gkits.menus;

import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorEquipEvent;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.managers.profile.Profile;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.DateUtils;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class SelectKitMenu extends Menu {

    private final ConfigCursor configCursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.SELECT-KIT");

    public SelectKitMenu() {
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
        return 9 * configCursor.getInt("ROWS");
    }

    @Override
    public Map<Integer, Button> getButtons(Player var1) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (Gkit gkit : Gkit.getGkits().values()) {
            buttons.put(gkit.getSlot(), new Button() {
                @Override
                public ItemStack getButtonItem(Player var1) {
                    return gkit.getIcon();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    if (clickType.equals(ClickType.LEFT)) {
                        if (player.hasPermission(gkit.getPermission())) {
                            Profile profile = Profile.getProfile(player);
                            assert profile != null;

                            if (profile.getKitCooldown().containsKey(gkit)) {
                                if (profile.getKitCooldown().get(gkit) > System.currentTimeMillis()) {
                                    long time = profile.getKitCooldown().get(gkit) - System.currentTimeMillis();
                                    player.sendMessage(CC.translate("&bYou need to wait " + DateUtils.formattedTimeLeft(time) + "!"));
                                    return;
                                }
                            }

                            PlayerInventory inventory = player.getInventory();
                            for (ItemStack item : gkit.getContents()) {
                                if (item != null) {
                                    if (item.getType() != Material.AIR) {
                                        item = item.clone();
                                        for (Map.Entry excess : inventory.addItem(new ItemStack[]{item.clone()}).entrySet()) {
                                            player.getWorld().dropItemNaturally(player.getLocation(), (ItemStack) excess.getValue());
                                        }
                                    }
                                }
                            }

                            if (gkit.getArmorContents() != null) {
                                for (int i = Math.min(3, gkit.getArmorContents().length); i >= 0; i--) {
                                    ItemStack stack = gkit.getArmorContents()[i];
                                    if (stack != null) {
                                        if (stack.getType() != Material.AIR) {
                                            int armourSlot = i + 36;
                                            ItemStack previuous = inventory.getItem(armourSlot);

                                            stack = stack.clone();
                                            if (previuous != null && previuous.getType() != Material.AIR) {
                                                previuous.setType(Material.AIR);
                                                player.getWorld().dropItemNaturally(player.getLocation(), stack);
                                            } else {
                                                ArmorEquipEvent event = new ArmorEquipEvent(player, gkit.getArmorContents());
                                                Bukkit.getPluginManager().callEvent(event);
                                                inventory.setItem(armourSlot, stack);
                                            }
                                        }
                                    }
                                }
                            }

                            long time = System.currentTimeMillis() + gkit.getCooldown();
                            profile.getKitCooldown().put(gkit, time);
                            player.updateInventory();
                        }
                    }
                }
            });
        }
        return buttons;
    }
}
