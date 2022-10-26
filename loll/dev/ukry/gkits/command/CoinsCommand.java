package dev.ukry.gkits.command;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand extends Command {

    public CoinsCommand() {
        super("coins");
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            CC.NO_CONSOLE();
            return false;
        }
        Player player = (Player) sender;
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("set")) {
                if(args.length != 3) {
                    sendUsage(player, label);
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if(target == null) {
                        player.sendMessage(CC.RED + "The player " + args[1] + " not exist or not is online!");
                        return false;
                    }
                    Profile targetProfile = SharkGKits.getInstance().getProfileHandler().getProfile(target.getUniqueId());
                    try {
                        targetProfile.setCoins(Double.parseDouble(args[2]));
                    } catch (NumberFormatException e) {
                        player.sendMessage(CC.RED + "The number " + args[2] + " not is valid!");
                        return false;
                    }
                    SharkGKits.getInstance().getProfileHandler().getProfiles().replace(targetProfile.getUuid(), targetProfile);
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if(args.length != 3) {
                    sendUsage(player, label);
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if(target == null) {
                        player.sendMessage(CC.RED + "The player " + args[1] + " not exist or not is online!");
                        return false;
                    }
                    Profile targetProfile = SharkGKits.getInstance().getProfileHandler().getProfile(target.getUniqueId());
                    try {
                        targetProfile.addCoins(Double.parseDouble(args[2]), "command");
                    } catch (NumberFormatException e) {
                        player.sendMessage(CC.RED + "The number " + args[2] + " not is valid!");
                        return false;
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if(args.length != 3) {
                    sendUsage(player, label);
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if(target == null) {
                        player.sendMessage(CC.RED + "The player " + args[1] + " not exist or not is online!");
                        return false;
                    }
                    Profile targetProfile = SharkGKits.getInstance().getProfileHandler().getProfile(target.getUniqueId());
                    try {
                        targetProfile.removeCoins(Double.parseDouble(args[2]), "command");
                    } catch (NumberFormatException e) {
                        player.sendMessage(CC.RED + "The number " + args[2] + " not is valid!");
                        return false;
                    }
                }
            } else {
                sendUsage(player, label);
                return false;
            }
        } else {
            Locale.COINS_BALANCE_MESSAGE
                    .setPlayer(player)
                    .addReplace(s -> s.replace("%balance%", String.valueOf(SharkGKits.getInstance().getProfileHandler().getProfile(player.getUniqueId()).getCoins())))
                    .send();
        }
        return false;
    }

    private void sendUsage(Player player, String label) {
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.YELLOW + "/" + label + " set <player> <amount>");
        player.sendMessage(CC.YELLOW + "/" + label + " add <player> <amount>");
        player.sendMessage(CC.YELLOW + "/" + label + " remove <player> <amount>");
        player.sendMessage(CC.CHAT_BAR);
    }
}
