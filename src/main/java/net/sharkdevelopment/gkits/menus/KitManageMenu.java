package net.sharkdevelopment.gkits.menus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.md_5.bungee.api.ChatColor;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.managers.profile.Profile;
import net.sharkdevelopment.gkits.menus.submenus.ArmorPieceSelectMenu;
import net.sharkdevelopment.gkits.menus.submenus.SlotMenu;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import net.sharkdevelopment.lib.task.TaskUtil;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KitManageMenu extends Menu {

    private final ConfigCursor configCursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.KIT-MANAGER.ITEMS");
    private final ConfigCursor cursor = new ConfigCursor(SharkGKits.getInstance().getMenusConfig(), "MENUS.KIT-MANAGER");
    private final Gkit gkit;

    public KitManageMenu(Gkit gkit) {
        this.gkit = gkit;

        this.setAutoUpdate(true);
        this.setUpdateAfterClick(true);

        if (cursor.getBoolean("FILL-BUTTONS.ENABLED")) {
            this.setPlaceholder(true);
            this.setPlaceholderButton(new Button() {
                @Override
                public ItemStack getButtonItem(Player var1) {
                    return ItemMaker.of(Material.STAINED_GLASS_PANE).data((short) cursor.getInt("FILL-BUTTONS.DATA")).build();
                }
            });
        }
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate(cursor.getString("TITLE").replace("<gkit-name>", gkit.getDisplayName()));
    }

    @Override
    public int getSize() {
        return 9 * cursor.getInt("ROWS");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(configCursor.getInt("CHANGE-NAME.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("CHANGE-NAME.MATERIAL")))
                        .displayName(configCursor.getString("CHANGE-NAME.NAME").replace("<gkit-name>", gkit.getDisplayName()))
                        .data((short) configCursor.getInt("CHANGE-NAME.DATA"))
                        .lore(configCursor.getStringList("CHANGE-NAME.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                new AnvilGUI.Builder()
                        .onComplete((player1, text) -> {
                            gkit.setDisplayName(CC.translate(text));
                            TaskUtil.runTaskLater(() -> {
                                new KitManageMenu(gkit).openMenu(player1);
                            }, 2L);
                            return AnvilGUI.Response.close();
                        })
                        .text("Insert display name")
                        .title("Insert the display name")
                        .plugin(SharkGKits.getInstance())
                        .open(player);
            }
        });

        buttons.put(configCursor.getInt("SLOT.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player var1) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("SLOT.MATERIAL")))
                        .data((short) configCursor.getInt("SLOT.DATA"))
                        .displayName(configCursor.getString("SLOT.NAME"))
                        .lore(configCursor.getStringList("SLOT.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                new SlotMenu(gkit).openMenu(player);
            }
        });

        buttons.put(configCursor.getInt("CHANGE-PERMISSION.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("CHANGE-PERMISSION.MATERIAL")))
                        .data((short) configCursor.getInt("CHANGE-PERMISSION.DATA"))
                        .displayName(configCursor.getString("CHANGE-PERMISSION.NAME"))
                        .lore(configCursor.getStringList("CHANGE-PERMISSION.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                Profile profile = Profile.getProfile(player);
                assert profile != null;

                player.closeInventory();
                player.sendMessage(CC.translate(SharkGKits.getInstance().getMessagesConfig().getConfig().getString("SET-PERMISSION-MESSAGE")));

                profile.getEditingPermission().put(true, gkit);

            }
        });

        buttons.put(configCursor.getInt("DISPLAY-ICON.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("DISPLAY-ICON.MATERIAL")))
                        .data((short) configCursor.getInt("DISPLAY-ICON.DATA"))
                        .displayName(configCursor.getString("DISPLAY-ICON.NAME"))
                        .lore(configCursor.getStringList("DISPLAY-ICON.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                player.closeInventory();
                player.sendMessage(CC.translate("&bPut the item you want in your hand and wait 6 seconds..."));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack icon = new ItemMaker(player.getItemInHand()).amount(1).displayName(gkit.getDisplayName()).lore(gkit.getDescription()).build();
                        gkit.setIcon(icon);
                        player.sendMessage(CC.translate("&bSuccessfully placed the item &7(" + player.getItemInHand().getType().name() + ")"));
                        new KitManageMenu(gkit).openMenu(player);
                    }
                }.runTaskLater(SharkGKits.getInstance(), 6 * 20);
            }
        });

        buttons.put(configCursor.getInt("DESCRIPTION.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = Lists.newArrayList();

                configCursor.getStringList("DESCRIPTION.LORE").forEach(string -> {
                    switch (string) {
                        case "<gkit-description>":
                            lore.addAll(gkit.getDescription());
                            break;
                        default:
                            lore.add(string);
                            break;
                    }
                });

                return ItemMaker.of(Material.valueOf(configCursor.getString("DESCRIPTION.MATERIAL")))
                        .data((short) configCursor.getInt("DESCRIPTION.DATA"))
                        .displayName(configCursor.getString("DESCRIPTION.NAME"))
                        .lore(CC.translate(lore))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                Profile profile = Profile.getProfile(player);
                assert profile != null;

                player.closeInventory();
                profile.getEditingLore().put(true, gkit);

                player.getInventory().addItem(ItemMaker.of(Material.BOOK_AND_QUILL).build());
                player.sendMessage(CC.translate("&bWhen you finished the description, left click air + shift with the book in the hand to save!"));
            }
        });

        buttons.put(configCursor.getInt("CONTENTS.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("CONTENTS.MATERIAL")))
                        .data((short) configCursor.getInt("CONTENTS.DATA"))
                        .displayName(configCursor.getString("CONTENTS.NAME"))
                        .lore(configCursor.getStringList("CONTENTS.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                Profile profile = Profile.getProfile(player);
                assert profile != null;

                player.closeInventory();
                profile.getEditingContents().put(true, gkit);

                player.sendMessage(CC.translate("&bWhen you finished the kit contents, left click air + shift to save the contents!"));
            }
        });

        buttons.put(configCursor.getInt("CUSTOM-ENCHANTS.SLOT"), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemMaker.of(Material.valueOf(configCursor.getString("CUSTOM-ENCHANTS.MATERIAL")))
                        .data((short) configCursor.getInt("CUSTOM-ENCHANTS.DATA"))
                        .displayName(configCursor.getString("CUSTOM-ENCHANTS.NAME"))
                        .lore(configCursor.getStringList("CUSTOM-ENCHANTS.LORE"))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                if (Arrays.stream(gkit.getArmorContents()).findAny().isPresent()) {
                    new ArmorPieceSelectMenu(gkit).openMenu(player);
                }
            }
        });
        return buttons;
    }
}
