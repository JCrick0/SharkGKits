package net.sharkdevelopment.lib.menu.buttons;

import net.sharkdevelopment.lib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DisplayButton extends Button {

  private ItemStack itemStack;
  private boolean cancel;

  @Override
  public ItemStack getButtonItem(Player player) {
    return this.itemStack == null ? new ItemStack(Material.AIR) : this.itemStack;
  }

  @Override
  public boolean shouldCancel(Player player, int slot, ClickType clickType) {
    return this.cancel;
  }

  public DisplayButton(ItemStack itemStack, boolean cancel) {
    this.itemStack = itemStack;
    this.cancel = cancel;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public boolean isCancel() {
    return this.cancel;
  }

  public void setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public void setCancel(boolean cancel) {
    this.cancel = cancel;
  }
}
