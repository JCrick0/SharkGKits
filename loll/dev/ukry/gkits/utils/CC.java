package dev.ukry.gkits.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CC {

    public static final ChatColor BLUE = ChatColor.BLUE;
    public static final ChatColor AQUA = ChatColor.AQUA;
    public static final ChatColor YELLOW = ChatColor.YELLOW;
    public static final ChatColor RED = ChatColor.RED;
    public static final ChatColor GRAY = ChatColor.GRAY;
    public static final ChatColor GOLD = ChatColor.GOLD;
    public static final ChatColor GREEN = ChatColor.GREEN;
    public static final ChatColor WHITE = ChatColor.WHITE;
    public static final ChatColor BLACK = ChatColor.BLACK;
    public static final ChatColor BOLD = ChatColor.BOLD;
    public static final ChatColor ITALIC = ChatColor.ITALIC;
    public static final ChatColor UNDER_LINE = ChatColor.UNDERLINE;
    public static final ChatColor STRIKE_THROUGH = ChatColor.STRIKETHROUGH;
    public static final ChatColor RESET = ChatColor.RESET;
    public static final ChatColor MAGIC = ChatColor.MAGIC;
    public static final ChatColor DARK_BLUE = ChatColor.DARK_BLUE;
    public static final ChatColor DARK_AQUA = ChatColor.DARK_AQUA;
    public static final ChatColor DARK_GRAY = ChatColor.DARK_GRAY;
    public static final ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
    public static final ChatColor DARK_PURPLE = ChatColor.DARK_PURPLE;
    public static final ChatColor DARK_RED = ChatColor.DARK_RED;
    public static final ChatColor PINK = ChatColor.LIGHT_PURPLE;
    public static final String MENU_BAR = GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------";
    public static final String CHAT_BAR = GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------";
    public static final String SB_BAR = GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------";

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate(List<String> input) {
        return input.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static void NO_CONSOLE() {
        log(RED, "No Console!");
    }

    public static void LOG(ChatColor color, String input) {
        log(color, input);
    }

    public static void log(ChatColor color, String input) {
        Bukkit.getConsoleSender().sendMessage(color + input);
    }

    public static void LOG(String input) {
        log(input);
    }

    public static void log(String input) {
        Bukkit.getConsoleSender().sendMessage(translate(input));
    }
}
