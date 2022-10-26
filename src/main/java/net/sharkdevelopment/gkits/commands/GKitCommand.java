package net.sharkdevelopment.gkits.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sharkdevelopment.gkits.managers.event.impl.ArmorEquipEvent;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.managers.profile.Profile;
import net.sharkdevelopment.gkits.menus.KitManageMenu;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.Clickable;
import net.sharkdevelopment.lib.DateUtils;
import net.sharkdevelopment.lib.TextComponentBuilder;
import net.sharkdevelopment.lib.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Map;

public class GKitCommand extends BaseCommand {

    public GKitCommand() {
        super("gkit", "sharkgkits.admin.gkits");
    }

    @Override
    @SuppressWarnings("all")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                String[] message = {
                        "&7&m----------------------------",
                        "&bSharkGKits &7- &fHelp",
                        " &7* &f/gkit list",
                        " &7* &f/gkit cooldown <name> <cooldown> &7(Example: /gkit cooldown gkit-test 10h)",
                        " &7* &f/gkit create <name>",
                        " &7* &f/gkit edit <name>",
                        " &7* &f/gkit delete <name>",
                        " &7* &f/gkit give <name>",
                        " &7* &f/gkit resetcooldown <player>",
                        "&7&m----------------------------",
                };

                Arrays.stream(message).forEach(string -> player.sendMessage(CC.translate(string)));
            } else {
                switch (args[0].toLowerCase()) {
                    case "create":
                        if (args.length == 2) {
                            String name = args[1];
                            assert name != null;

                            if (Gkit.getByName(name) != null) {
                                player.sendMessage(CC.translate("&cThat kit already exists!"));
                                return;
                            }

                            Gkit createdKit = new Gkit(name);
                            Gkit.setDefaultParams(createdKit);
                            new KitManageMenu(createdKit).openMenu(player);
                        }
                    case "edit":
                        if (args.length == 2) {
                            String name = args[1];
                            assert name != null;

                            Gkit editKit = Gkit.getByName(name);

                            if (editKit == null) {
                                player.sendMessage(CC.translate("&cThat kit doesn't exists!"));
                                return;
                            }

                            new KitManageMenu(editKit).openMenu(player);
                        }
                        break;
                    case "give":
                        if (args.length == 2) {
                            Profile profile = Profile.getProfile(player);
                            assert profile != null;

                            String name = args[1];
                            assert name != null;

                            Gkit giveKit = Gkit.getByName(name);

                            if (giveKit == null) {
                                player.sendMessage(CC.translate("&cThat kit doesn't exists!"));
                                return;
                            }

                            if (profile.getKitCooldown().containsKey(giveKit)) {
                                if (profile.getKitCooldown().get(giveKit) > System.currentTimeMillis()) {
                                    long time = profile.getKitCooldown().get(giveKit) - System.currentTimeMillis();
                                    player.sendMessage(CC.translate("&bYou need to wait " + DateUtils.formattedTimeLeft(time) + "!"));
                                    return;
                                }
                            }

                            PlayerInventory inventory = player.getInventory();
                            for (ItemStack item : giveKit.getContents()) {
                                if (item != null) {
                                    if (item.getType() != Material.AIR) {
                                        item = item.clone();
                                        for (Map.Entry excess : inventory.addItem(new ItemStack[]{item.clone()}).entrySet()) {
                                            player.getWorld().dropItemNaturally(player.getLocation(), (ItemStack) excess.getValue());
                                        }
                                    }
                                }
                            }

                            if (giveKit.getArmorContents() != null) {
                                for (int i = Math.min(3, giveKit.getArmorContents().length); i >= 0; i--) {
                                    ItemStack stack = giveKit.getArmorContents()[i];
                                    if (stack != null) {
                                        if (stack.getType() != Material.AIR) {
                                            int armourSlot = i + 36;
                                            ItemStack previuous = inventory.getItem(armourSlot);

                                            stack = stack.clone();
                                            if (previuous != null && previuous.getType() != Material.AIR) {
                                                previuous.setType(Material.AIR);
                                                player.getWorld().dropItemNaturally(player.getLocation(), stack);
                                            } else {
                                                ArmorEquipEvent event = new ArmorEquipEvent(player, giveKit.getArmorContents());
                                                Bukkit.getPluginManager().callEvent(event);
                                                inventory.setItem(armourSlot, stack);
                                            }
                                        }
                                    }
                                }
                            }

                            long time = System.currentTimeMillis() + giveKit.getCooldown();
                            profile.getKitCooldown().put(giveKit, time);
                            player.updateInventory();
                        }

                        break;
                    case "delete":
                        if (args.length == 2) {
                            String name = args[1];
                            assert name != null;

                            Gkit deleteKit = Gkit.getByName(name);

                            if (deleteKit == null) {
                                player.sendMessage(CC.translate("&cThat kit doesn't exists!"));
                                return;
                            }
                            player.sendMessage(CC.translate("&bKit named '" + deleteKit.getName() + "' deleted successfully"));
                            deleteKit.delete();
                        }
                        break;
                    case "list":
                        player.sendMessage(CC.translate("&7&m---------------------"));
                        player.sendMessage(CC.translate(" &b&lGKits &7- &flist"));

                        Gkit.getGkits().forEach((name, kit) -> {
                            TextComponentBuilder kitlisted = new TextComponentBuilder(CC.translate(" &7* &f" + kit.getName() + " "));
                            TextComponentBuilder delete = new TextComponentBuilder(CC.translate("&7(&cDelete&7) "))
                                    .setHoverEvent(HoverEvent.Action.SHOW_TEXT, "&cClick here to delete this kit")
                                    .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/gkit delete " + kit.getName());
                            TextComponentBuilder edit = new TextComponentBuilder(CC.translate("&7(&aEdit&7)"))
                                    .setHoverEvent(HoverEvent.Action.SHOW_TEXT, "&aClick here to edit this kit")
                                    .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/gkit edit " + kit.getName());

                            BaseComponent[] baseComponents = new BaseComponent[]{
                                    kitlisted.toText(),
                                    delete.toText(),
                                    edit.toText()
                            };

                            player.spigot().sendMessage(baseComponents);
                        });
                        player.sendMessage(CC.translate("&7&m---------------------"));
                        break;
                    /*case "setenchant":
                        if (player.getItemInHand() != null) {
                            final String typeNameString = player.getItemInHand().getType().name();
                            if (typeNameString.endsWith("_HELMET")
                                    || typeNameString.endsWith("_CHESTPLATE")
                                    || typeNameString.endsWith("_LEGGINGS")
                                    || typeNameString.endsWith("_BOOTS")) {
                                new PieceCustomEnchantMenu(player.getItemInHand()).openMenu(player);
                            }
                        }
                        break;*/
                    case "cooldown":
                        if (args.length == 3) {
                            String name = args[1];
                            assert name != null;

                            Gkit gkit = Gkit.getByName(name);

                            long duration = DateUtils.formatLong(args[2]);

                            if (duration == -1) {
                                player.sendMessage(CC.translate("&CInvalid duration"));
                                return;
                            }

                            if (duration < 1000L) {
                                player.sendMessage(CC.translate("&cCooldown must be in ticks"));
                                return;
                            }

                            gkit.setCooldown(duration);
                            player.sendMessage(CC.translate("&aSuccessfully"));
                        }
                        break;
                    case "resetcooldown":
                        if (args.length == 1) {
                            player.sendMessage(CC.translate("&7&m---------------------"));
                            player.sendMessage(CC.translate("&bUsage&7: &f/gkit resetcooldown <player>"));
                            player.sendMessage(CC.translate("&7&m---------------------"));
                            return;
                        } else if (args.length == 2) {
                            String name = args[1];
                            assert name != null;

                            Player target = Bukkit.getPlayer(name);
                            assert target != null;

                            Profile targetProfile = Profile.getProfile(player);
                            assert targetProfile != null;

                            targetProfile.getKitCooldown().clear();
                            player.sendMessage(CC.translate("&bCooldowns reset successful"));
                        }
                        break;
                }
            }
        }
    }
}
