package dev.ukry.gkits.utils.event;

import org.bukkit.event.Cancellable;

public abstract class BaseEventCancellable extends BaseEvent implements Cancellable {

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
