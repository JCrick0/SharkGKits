package net.sharkdevelopment.lib;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

import static net.sharkdevelopment.lib.Assert.assertNotNull;

/**
 * @author Leandro Figueroa (LeandroSSJ)
 * viernes, abril 02, 2021
 */

public class CC {
    public static String translate(String input) {
        assertNotNull(input);
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = Lists.newArrayList();

        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = Lists.newArrayList();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return toReturn;
    }
}
