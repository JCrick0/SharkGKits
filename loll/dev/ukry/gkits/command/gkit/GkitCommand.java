package dev.ukry.gkits.command.gkit;

import dev.ukry.gkits.command.gkit.argument.*;
import dev.ukry.gkits.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GkitCommand extends Command {

    public GkitCommand() {
        super("gkit");
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            CC.NO_CONSOLE();
            return false;
        }
        Player player = (Player) sender;
        if(args.length > 0 && player.isOp()) {
            switch (args[0].toLowerCase(java.util.Locale.ROOT)) {
                case "create" : {
                    return CreateGkitArgument.execute(player, args, label);
                }
                case "edit" : {
                    return EditGkitArgument.execute(player, args, label);
                }
                case "createcategory" : {
                    return CreateCategoryArgument.execute(player, args, label);
                }
                case "removecategory" : {
                    return RemoveCategoryArgument.execute(player, args, label);
                }
                case "editcategory" : {
                    return EditCategoryArgument.execute(player, args);
                }
                case "remove" : {
                    return RemoveGkitArgument.execute(player, args, label);
                }
                case "reload" : {
                    return ReloadArgument.execute(player, args, label);
                }
                case "buy" : {
                    return BuyArgument.execute(player, args, label);
                }
                case "resetcooldown" : {
                    return ResetCooldownArgument.execute(player, args, label);
                }
                case "give" : {
                    return GiveArgument.execute(player, args, label);
                }
                case "list" : {
                    return ListGkitArgument.execute(player, args, label);
                }
                default: {
                    sendUsage(player, label);
                }
            }
        } else {
            sendUsage(player, label);
        }
        return false;
    }

    private void sendUsage(Player player, String label) {
        player.sendMessage(CC.CHAT_BAR);
        if(player.isOp()) {
            player.sendMessage(CC.YELLOW + "/" + label + " create <name>");
            player.sendMessage(CC.YELLOW + "/" + label + " createcategory <category>");
            player.sendMessage(CC.YELLOW + "/" + label + " edit <nothing/name>");
            player.sendMessage(CC.YELLOW + "/" + label + " editcategory <nothing/category>");
            player.sendMessage(CC.YELLOW + "/" + label + " remove <name>");
            player.sendMessage(CC.YELLOW + "/" + label + " removecategory <category>");
            player.sendMessage(CC.YELLOW + "/" + label + " give <player/all> <gkit/nothing>");
            player.sendMessage(CC.YELLOW + "/" + label + " list");
            player.sendMessage(CC.YELLOW + "/" + label + " reload");
            player.sendMessage(CC.YELLOW + "/" + label + " resetcooldown <user/all> <gkit>");
            /*
            player.sendMessage(CC.YELLOW + "/" + label + " setprice <gkit> <price>");
            player.sendMessage(CC.YELLOW + "/" + label + " setcategory <gkit> <category>");
            player.sendMessage(CC.YELLOW + "/" + label + " removecategory <category>");
             */
        }
        player.sendMessage(CC.YELLOW + "/" + label + " buy <name>");
        player.sendMessage(CC.CHAT_BAR);
    }
}