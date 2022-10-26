package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCategoryArgument {

    public static boolean execute(Player player, String[] args, String label) {
        if(args.length == 2) {
            if(Category.valueOf(args[1]) != null) {
                Locale.CREATE_CATEGORY_ARGUMENT_EXIST
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%category%", args[1]))
                        .send();
                return false;
            } else {
                List<Category> categories = Arrays.stream(Category.values())
                        .sorted(Collections.reverseOrder(Comparator.comparingInt(Category::getSlot)))
                        .collect(Collectors.toList());
                new Category(
                        args[1],
                        true,
                        categories.isEmpty() ? 0 : categories.get(0).getSlot() + 1,
                        45, new ItemBuilder(Material.BOOK).name(args[1]).build(), false).save();
                Locale.CREATE_CATEGORY_ARGUMENT_SUCCESSFULLY
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%category%", args[1]))
                        .send();
            }
        } else {
            player.sendMessage(CC.YELLOW + "/" + label + " createcategory <category>");
        }
        return true;
    }
}