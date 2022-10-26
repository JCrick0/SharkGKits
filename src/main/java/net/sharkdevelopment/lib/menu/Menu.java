package net.sharkdevelopment.lib.menu;

import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.menus.KitManageMenu;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Map.Entry;

public abstract class Menu {

    protected final SharkGKits plugin = SharkGKits.getInstance();
    public static Map<String, Menu> currentlyOpenedMenus = Maps.newHashMap();
    private Map<Integer, Button> buttons = Maps.newHashMap();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean closedByMenu = false;
    private boolean placeholder = false;
    private Button placeholderButton = Button.placeholder(
            Material.STAINED_GLASS_PANE,
            (byte) 15,
            " "
    );

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);
        if (item.getType() != Material.SKULL) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void openMenu(Player player) {
        this.buttons = this.getButtons(player);
        Menu previousMenu = (Menu) currentlyOpenedMenus.get(player.getName());
        Inventory inventory = null;
        int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
        boolean update = false;
        String title = this.getTitle(player);
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        if (player.getOpenInventory() != null) {
            if (previousMenu == null) {
                player.closeInventory();
            } else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                if (
                        previousSize == size &&
                                player.getOpenInventory().getTopInventory().getTitle().equals(title)
                ) {
                    inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                } else {
                    previousMenu.setClosedByMenu(true);
                    player.closeInventory();
                }
            }
        }

        if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
        }

        inventory.setContents(new ItemStack[inventory.getSize()]);
        currentlyOpenedMenus.put(player.getName(), this);

        for (Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(
                    buttonEntry.getKey(),
                    this.createItemStack(player, (Button) buttonEntry.getValue())
            );
        }

        if (this.isPlaceholder()) {
            for (int index = 0; index < size; ++index) {
                if (this.buttons.get(index) == null) {
                    this.buttons.put(index, this.placeholderButton);
                    inventory.setItem(index, this.placeholderButton.getButtonItem(player));
                }
            }
        }

        if (update) {
            player.updateInventory();
        } else {
            player.openInventory(inventory);
        }

        this.onOpen(player);
        this.setClosedByMenu(false);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((double) (highest + 1) / 9.0) * 9.0);
    }

    public void updateInventory(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.setContents(new ItemStack[inventory.getSize()]);
        currentlyOpenedMenus.put(player.getName(), this);

        for (Entry<Integer, Button> buttonEntry : this.getButtons(player).entrySet()) {
            inventory.setItem(
                    buttonEntry.getKey(),
                    this.createItemStack(player, (Button) buttonEntry.getValue())
            );
        }

        player.updateInventory();
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public int getSize() {
        return -1;
    }

    public abstract String getTitle(Player var1);

    public abstract Map<Integer, Button> getButtons(Player var1);

    public void onOpen(Player player) {
        currentlyOpenedMenus.put(player.getName(), this);
    }

    public void onClose(Player player) {
    }

    public void fillEmptySlots(Map<Integer, Button> buttons, final ItemStack itemStack) {
        int bound = this.getSize();

        for (int slot = 0; slot < bound; ++slot) {
            if (buttons.get(slot) == null) {
                buttons.put(
                        slot,
                        new Button() {
                            @Override
                            public ItemStack getButtonItem(Player player) {
                                return itemStack;
                            }
                        }
                );
            }
        }
    }

    public SharkGKits getPlugin() {
        return this.plugin;
    }

    public boolean isAutoUpdate() {
        return this.autoUpdate;
    }

    public boolean isUpdateAfterClick() {
        return this.updateAfterClick;
    }

    public boolean isClosedByMenu() {
        return this.closedByMenu;
    }

    public boolean isPlaceholder() {
        return this.placeholder;
    }

    public Button getPlaceholderButton() {
        return this.placeholderButton;
    }

    public void setButtons(Map<Integer, Button> buttons) {
        this.buttons = buttons;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public void setUpdateAfterClick(boolean updateAfterClick) {
        this.updateAfterClick = updateAfterClick;
    }

    public void setClosedByMenu(boolean closedByMenu) {
        this.closedByMenu = closedByMenu;
    }

    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }

    public void setPlaceholderButton(Button placeholderButton) {
        this.placeholderButton = placeholderButton;
    }

    public Map<Integer, Button> getButtons() {
        return this.buttons;
    }
}
