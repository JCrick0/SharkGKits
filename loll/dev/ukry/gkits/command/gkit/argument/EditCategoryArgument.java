package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.ui.categories.CategoriesListUI;
import dev.ukry.gkits.ui.categories.edit.CategoryEditUI;
import org.bukkit.entity.Player;

public class EditCategoryArgument {

    public static boolean execute(Player player, String[] args) {
        if(args.length == 1) {
            new CategoriesListUI(category -> new CategoryEditUI(category).open(player)).open(player);
        } else {
            String categoryName = args[2];
            Category category = Category.valueOf(categoryName);
            if(category != null) {
                new CategoryEditUI(category).open(player);
            } else {
                Locale.EDIT_CATEGORY_ARGUMENT_NO_EXIST
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%category%", categoryName))
                        .send();
            }
        }
        return true;
    }
}