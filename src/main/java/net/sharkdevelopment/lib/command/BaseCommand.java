package net.sharkdevelopment.lib.command;

import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.lib.CC;
import lombok.Getter;
import lombok.Setter;
import net.sharkdevelopment.lib.task.TaskUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 28/08/2021
 */
public abstract class BaseCommand extends BukkitCommand {

    @Getter private final boolean isPlayerOnly;
    @Setter @Getter private boolean executeAsync;


    public BaseCommand(String name) {
        this(name, new ArrayList<>());
    }

    public BaseCommand(String name, List<String> aliases) {
        this(name, aliases, false);
    }

    public BaseCommand(String name, String permission) {
        this(name, new ArrayList<>(), permission);
    }

    public BaseCommand(String name, boolean isPlayerOnly) {
        this(name, new ArrayList<>(), null, isPlayerOnly);
    }

    public BaseCommand(String name, List<String> aliases, String permission) {
        this(name, aliases, permission, false);
    }

    public BaseCommand(String name, List<String> aliases, boolean isPlayerOnly) {
        this(name, aliases, null, isPlayerOnly);
    }

    public BaseCommand(String name, String permission, boolean isPlayerOnly) {
        this(name, new ArrayList<>(), permission, isPlayerOnly);
    }

    public BaseCommand(String name, List<String> aliases, String permission, boolean isPlayerOnly) {
        super(name);

        this.setAliases(aliases);
        this.setPermission(permission);
        this.isPlayerOnly = isPlayerOnly;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(CC.translate("Command online in player"));
            return true;
        }

        if(this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            sender.sendMessage(CC.translate("No permission."));
            return true;
        }

        if(isExecuteAsync()) {
            TaskUtil.runTaskAsync(() -> this.execute(sender, args));
        } else {
            this.execute(sender, args);
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
}