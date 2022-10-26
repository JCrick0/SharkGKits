package dev.ukry.gkits.utils.command;

import dev.ukry.gkits.SharkGKits;
import lombok.SneakyThrows;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

import static org.bukkit.Bukkit.getServer;

public class Command {


    @SneakyThrows
    public static void registerCommand(org.bukkit.command.Command cmd) {
        Field field = getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        CommandMap commandMap = (CommandMap) field.get(getServer());
        commandMap.register(SharkGKits.getInstance().getName(), cmd);
    }

}
