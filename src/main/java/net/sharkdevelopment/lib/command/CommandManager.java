package net.sharkdevelopment.lib.command;

import com.google.common.collect.Lists;
import net.sharkdevelopment.gkits.commands.CoinsCommand;
import net.sharkdevelopment.gkits.commands.CustomEnchantCommand;
import net.sharkdevelopment.gkits.commands.GKitCommand;
import net.sharkdevelopment.gkits.commands.KitsCommand;
import net.sharkdevelopment.lib.manager.Manager;
import net.sharkdevelopment.lib.wrapper.ClassWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 28/08/2021
 */
public enum CommandManager implements Manager {

    INSTANCE;

    private CommandMap commandMap;
    private List<BaseCommand> commands;
    private String pluginName;

    @Override
    public void onEnable(JavaPlugin plugin) {
        commandMap = getCommandMap();
        commands = Lists.newArrayList();
        pluginName = plugin.getDescription().getName();
        this.commands.add(new GKitCommand());
        this.commands.add(new CoinsCommand());
        this.commands.add(new KitsCommand());
        this.commands.add(new CustomEnchantCommand());

        this.commands.forEach(this::registerCommand);
        Bukkit.getLogger().info("[SharkGKits] Register " + this.commands.size() + " command");
    }


    private CommandMap getCommandMap() {
        return (CommandMap) new ClassWrapper(Bukkit.getServer()).getField("commandMap").get();
    }

    @Override
    public void onDisable(JavaPlugin plugin) {
        this.commands.clear();
    }


    public void registerCommand(BukkitCommand command) {
        this.commandMap.register(pluginName, command);
    }

}
