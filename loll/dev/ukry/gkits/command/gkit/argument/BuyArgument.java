package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.handler.GkitsHandler;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;

public class BuyArgument {

    private static final GkitsHandler handler = SharkGKits.getInstance().getGkitsHandler();

    public static boolean execute(Player player, String[] args, String label) {
        if(args.length != 2) {
            player.sendMessage(CC.RED + "Usage: /" + label + " buy <gkit>");
            return false;
        }
        if(handler.getGkit(args[1]) != null) {
            Profile profile = SharkGKits.getInstance().getProfileHandler().getProfile(player.getUniqueId());
            Gkit gkit = handler.getGkit(args[1]);
            if(gkit.getPrice() < profile.getCoins()) {
                profile.removeCoins(gkit.getPrice(), "Buyed gkit " + gkit.getName());
                profile.addGkit(gkit);
                Locale.BUY_ARGUMENT_SUCCESSFULLY
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%gkit%", args[1]));
                return true;
            } else {
                Locale.BUY_ARGUMENT_NO_MONEY
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%gkit%", args[1]))
                        .send();
                return false;
            }
        } else {
            Locale.BUY_ARGUMENT_NO_EXIST
                    .setPlayer(player)
                    .addPrefix()
                    .addReplace(s -> s.replace("%gkit%", args[1]))
                    .send();
            return false;
        }
    }
}