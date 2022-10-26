package net.sharkdevelopment.gkits.commands;

import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.profile.Profile;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.command.BaseCommand;
import net.sharkdevelopment.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoinsCommand extends BaseCommand {

    private final ConfigCursor cursor;

    public CoinsCommand() {
        super("coins");

        this.cursor = new ConfigCursor(SharkGKits.getInstance().getMessagesConfig(), "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = Profile.getProfile(player);
            assert profile != null;

            if (args.length == 0 && !player.hasPermission("sharkgkits.command.coins")) {
                player.sendMessage(CC.translate(SharkGKits.getInstance().getMessagesConfig().getConfig().getString("CURRENT-COINS").replace("<coins>", String.valueOf(profile.getCoins()))));
            }

            if (args.length == 0 && player.hasPermission("sharkgkits.command.coins")) {
                String[] usage = {
                        "&7&m---------------------",
                        "&bShark Gkits &7- &fCoins",
                        " &fYour coins&7: &b" + profile.getCoins(),
                        " &7- &f/coins set <player> <coins>",
                        " &7- &f/coins add <player> <coins>",
                        " &7- &f/coins remove <player> <coins>",
                        " &7- &f/coins info <player>",
                        " &7- &f/coins clear <player>",
                        "&7&m---------------------"
                };

                Arrays.stream(usage).forEach(s -> player.sendMessage(CC.translate(s)));
            }

            if(player.hasPermission("sharkgkits.command.coins")) {
                switch (args[0].toLowerCase()) {
                    case "set":
                        if (args.length == 3) {
                            Player target = Bukkit.getPlayer(args[1]);
                            assert target != null;
                            try {
                                int coins = Integer.parseInt(args[2]);

                                Profile targetProfile = Profile.getProfile(target);
                                assert targetProfile != null;
                                targetProfile.setCoins(coins);

                                player.sendMessage(CC.translate(cursor.getString("MODIFIED-TARGET-COINS")));
                                target.sendMessage(CC.translate(cursor.getString("MODIFIED-COINS-ALERT").replace("<coins>", String.valueOf(targetProfile.getCoins()))));
                            } catch (NumberFormatException exception) {
                                exception.printStackTrace();
                            }
                        }
                        break;
                    case "add":
                        if (args.length == 3) {
                            Player target = Bukkit.getPlayer(args[1]);
                            assert target != null;
                            try {
                                int coins = Integer.parseInt(args[2]);

                                Profile targetProfile = Profile.getProfile(target);
                                assert targetProfile != null;
                                targetProfile.setCoins(targetProfile.getCoins() + coins);

                                player.sendMessage(CC.translate(cursor.getString("MODIFIED-TARGET-COINS")));
                                target.sendMessage(CC.translate(cursor.getString("MODIFIED-COINS-ALERT").replace("<coins>", String.valueOf(targetProfile.getCoins()))));
                            } catch (NumberFormatException exception) {
                                exception.printStackTrace();
                            }
                        }
                        break;
                    case "remove":
                        if (args.length == 3) {
                            Player target = Bukkit.getPlayer(args[1]);
                            assert target != null;
                            try {
                                int coins = Integer.parseInt(args[2]);

                                Profile targetProfile = Profile.getProfile(target);
                                assert targetProfile != null;

                                targetProfile.setCoins(targetProfile.getCoins() - coins);

                                player.sendMessage(CC.translate(cursor.getString("MODIFIED-TARGET-COINS")));
                                target.sendMessage(CC.translate(cursor.getString("MODIFIED-COINS-ALERT").replace("<coins>", String.valueOf(targetProfile.getCoins()))));
                            } catch (NumberFormatException exception) {
                                exception.printStackTrace();
                            }
                        }
                        break;
                    case "info":
                        if (args.length == 2) {
                            Player target = Bukkit.getPlayer(args[1]);
                            assert target != null;

                            Profile targetProfile = Profile.getProfile(target);
                            assert targetProfile != null;

                            player.sendMessage(CC.translate(cursor.getString("TARGET-COINS-INFO").replace("<coins>", String.valueOf(targetProfile.getCoins()))));
                        }
                        break;
                    case "clear":
                        Player target = Bukkit.getPlayer(args[1]);
                        assert target != null;

                        Profile targetProfile = Profile.getProfile(target);
                        assert targetProfile != null;

                        targetProfile.setCoins(0);

                        player.sendMessage(CC.translate(cursor.getString("RESET-COINS-SUCCESS")));
                        target.sendMessage(CC.translate(cursor.getString("COINS-RESET-ALERT")));
                        break;
                    default:
                        String[] usage = {
                                "&7&m---------------------",
                                "&bShark Gkits &7- &fCoins",
                                " &fYour coins&7: &b" + profile.getCoins(),
                                " &7- &f/coins set <player> <coins>",
                                " &7- &f/coins add <player> <coins>",
                                " &7- &f/coins remove <player> <coins>",
                                " &7- &f/coins info <player>",
                                " &7- &f/coins clear <player>",
                                "&7&m---------------------"
                        };

                        Arrays.stream(usage).forEach(s -> player.sendMessage(CC.translate(s)));
                        break;
                }
            }

        }
    }
}
