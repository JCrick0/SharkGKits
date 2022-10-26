package dev.ukry.gkits.utils.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class BaseEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public void call() {
        Bukkit.getServer().getPluginManager().callEvent(this);
    }
}
