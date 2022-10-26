package net.sharkdevelopment.lib.menu;

import net.sharkdevelopment.gkits.SharkGKits;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Button {

  protected final SharkGKits plugin = SharkGKits.getInstance();

  public static Button placeholderButton = Button.placeholder(
          Material.STAINED_GLASS_PANE,
          (byte) 15,
          " "
  );

  public static Button placeholder(
    final Material material,
    final byte data,
    final String... title
  ) {
    return new Button() {
      @Override
      public ItemStack getButtonItem(Player player) {
        ItemStack it = new ItemStack(material, 1, (short) data);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(StringUtils.join(title));
        it.setItemMeta(meta);
        return it;
      }
    };
  }

  public static void playFail(Player player) {
    player.playSound(player.getLocation(), Sound.DIG_GRASS, 20.0F, 0.1F);
  }

  public static void playSuccess(Player player) {
    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0F, 15.0F);
  }

  public static void playNeutral(Player player) {
    player.playSound(player.getLocation(), Sound.CLICK, 20.0F, 1.0F);
  }

  public abstract ItemStack getButtonItem(Player var1);

  public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {}

  public boolean shouldCancel(Player player, int slot, ClickType clickType) {
    return true;
  }

  public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
    return false;
  }
}
