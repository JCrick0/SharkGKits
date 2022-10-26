package dev.ukry.gkits.command;

import dev.ukry.gkits.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantsCommand extends Command {


    public EnchantsCommand() {
        super("enchants");
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            CC.NO_CONSOLE();
            return false;
        }
        Player player = (Player) sender;
        if(args.length != 1) {
            player.sendMessage(CC.RED + "/" + label + " <speed/fire_resistance/invisibility>");
            return false;
        } else {
            if(args[0].equalsIgnoreCase("speed")) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(CC.AQUA + "SPEED");
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else if(args[0].equalsIgnoreCase("fire_resistance")) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(CC.AQUA + "FIRE_RESISTANCE");
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else if(args[0].equalsIgnoreCase("invisibility")) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(CC.AQUA + "INVISIBILITY");
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                player.sendMessage(CC.RED + "/" + label + " <speed/fire_resistance/invisibility>");
                return false;
            }
        }
        return false;
    }
}