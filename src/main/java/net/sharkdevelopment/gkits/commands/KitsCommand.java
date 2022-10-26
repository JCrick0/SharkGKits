package net.sharkdevelopment.gkits.commands;

import net.sharkdevelopment.gkits.menus.SelectKitMenu;
import net.sharkdevelopment.lib.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KitsCommand extends BaseCommand {

    public KitsCommand() {
        super("kits");
        this.setAliases(Arrays.asList("kit", "kitlist"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                new SelectKitMenu().openMenu(player);
            }
        }
    }
}
