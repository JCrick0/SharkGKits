package dev.ukry.gkits.command.gkit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Config;
import dev.ukry.gkits.cooldown.CooldownHandler;
import dev.ukry.gkits.manager.events.GkitEquipEvent;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.categories.CategoriesListUI;
import dev.ukry.gkits.ui.gkit.GkitListUI;
import dev.ukry.gkits.ui.gkit.GkitPreviewUI;
import dev.ukry.gkits.utils.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GKitsCommand extends Command {

    public GKitsCommand() {
        super("gkits");
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            CC.NO_CONSOLE();
            return false;
        }
        Player player = (Player) sender;
        if(!Config.CATEGORIES.getAsBoolean()) {
            new GkitListUI(Objects.requireNonNull(Category.valueOf("DEFAULT")), (event, gkit) -> {
                if (event.getClick() == ClickType.RIGHT) {
                    new GkitPreviewUI(gkit).open(player);
                } else {
                    Profile profile = SharkGKits.getInstance().getProfileHandler().getProfile(player.getUniqueId());
                    if (profile.hasGkit(gkit)) {
                        if (CooldownHandler.hasCooldown(player.getUniqueId(), gkit.getName())) {
                            player.sendMessage(CC.RED + "You have cooldown of " + CooldownHandler.getCooldown(player.getUniqueId(), gkit.getName()));
                        } else {
                            if(Config.DROP.getAsBoolean()) {
                                for (ItemStack stack : gkit.getContents()) {
                                    if (stack == null || stack.getType() == Material.AIR) continue;
                                    int size = player.getInventory().getSize();
                                    if (size < 54) {
                                        player.getInventory().addItem(stack);
                                    } else {
                                        player.getWorld().dropItemNaturally(player.getLocation(), stack);
                                    }
                                }
                                for (ItemStack stack : gkit.getArmorContents()) {
                                    if (stack == null || stack.getType() == Material.AIR) continue;
                                    int size = player.getInventory().getSize();
                                    if (size < 54) {
                                        player.getInventory().addItem(stack);
                                    } else {
                                        player.getWorld().dropItemNaturally(player.getLocation(), stack);
                                    }
                                }
                                player.updateInventory();
                                CooldownHandler.setCooldown(player.getUniqueId(), gkit.getName(), gkit.getCooldown());
                                Locale.SUCCESSFULLY_EQUIPED
                                        .setPlayer(player)
                                        .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                                        .addReplace(s -> s.replace("%gkit%", gkit.getName()))
                                        .send();
                            } else {
                                if (player.getInventory().firstEmpty() == 0) {
                                    player.getInventory().setContents(gkit.getContents());
                                    player.getInventory().setArmorContents(gkit.getArmorContents());
                                    player.updateInventory();
                                    CooldownHandler.setCooldown(player.getUniqueId(), gkit.getName(), gkit.getCooldown());
                                    Locale.SUCCESSFULLY_EQUIPED
                                            .setPlayer(player)
                                            .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                                            .addReplace(s -> s.replace("%gkit%", gkit.getName()))
                                            .send();
                                } else {
                                    Locale.INVENTORY_FULL
                                            .setPlayer(player)
                                            .addReplace(ss -> ss.replace("%prefix%", Locale.PREFIX.getString()))
                                            .send();
                                }
                            }
                        }
                    } else {
                        Locale.NO_HAVE_OWN_GKIT
                                .setPlayer(player)
                                .addReplace(ss -> ss.replace("%prefix%", Locale.PREFIX.getString()))
                                .send();
                    }
                }
            }, player).open(player);
        } else {
            new CategoriesListUI(category -> new GkitListUI(category, (event, gkit) -> {
                if (event.getClick() == ClickType.RIGHT) {
                    new GkitPreviewUI(gkit).open(player);
                } else {
                    Profile profile = SharkGKits.getInstance().getProfileHandler().getProfile(player.getUniqueId());
                    if (profile.hasGkit(gkit)) {
                        if (CooldownHandler.hasCooldown(player.getUniqueId(), gkit.getName())) {
                            player.sendMessage(CC.RED + "You have cooldown of " + CooldownHandler.getCooldown(player.getUniqueId(), gkit.getName()));
                        } else {
                            if(Config.DROP.getAsBoolean()) {
                                for (ItemStack stack : gkit.getContents()) {
                                    if (stack == null || stack.getType() == Material.AIR) continue;
                                    int size = player.getInventory().getSize();
                                    if (size < 54) {
                                        player.getInventory().addItem(stack);
                                    } else {
                                        player.getWorld().dropItem(player.getLocation().add(0, 1, 0), stack);
                                    }
                                }
                                for (ItemStack stack : gkit.getArmorContents()) {
                                    if (stack == null || stack.getType() == Material.AIR) continue;
                                    int size = player.getInventory().getSize();
                                    if (size < 54) {
                                        player.getInventory().addItem(stack);
                                    } else {
                                        player.getWorld().dropItem(player.getLocation().add(0, 1, 0), stack);
                                    }
                                }
                                player.updateInventory();
                                CooldownHandler.setCooldown(player.getUniqueId(), gkit.getName(), gkit.getCooldown());
                                new GkitEquipEvent(gkit).call();
                                Locale.SUCCESSFULLY_EQUIPED
                                        .setPlayer(player)
                                        .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                                        .addReplace(s -> s.replace("%gkit%", gkit.getName()))
                                        .send();
                            } else {
                                if (player.getInventory().firstEmpty() == 0) {
                                    player.getInventory().setContents(gkit.getContents());
                                    player.getInventory().setArmorContents(gkit.getArmorContents());
                                    player.updateInventory();
                                    CooldownHandler.setCooldown(player.getUniqueId(), gkit.getName(), gkit.getCooldown());
                                    Locale.SUCCESSFULLY_EQUIPED
                                            .setPlayer(player)
                                            .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                                            .addReplace(s -> s.replace("%gkit%", gkit.getName()))
                                            .send();
                                } else {
                                    Locale.INVENTORY_FULL
                                            .setPlayer(player)
                                            .addReplace(ss -> ss.replace("%prefix%", Locale.PREFIX.getString()))
                                            .send();
                                }
                            }
                        }
                    } else {
                        Locale.NO_HAVE_OWN_GKIT
                                .setPlayer(player)
                                .addReplace(ss -> ss.replace("%prefix%", Locale.PREFIX.getString()))
                                .send();
                    }
                }
            }, player).open(player)).open(player);
        }
        return false;
    }
}