package net.sharkdevelopment.gkits.managers.profile.listeners;

import net.sharkdevelopment.gkits.managers.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListeners implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Profile profile = Profile.getProfile(event.getUniqueId());

        if (profile == null) {
            profile = new Profile(event.getUniqueId());
        }

        profile.save();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Profile profile = Profile.getProfile(event.getPlayer());
        assert profile != null;
        profile.save();
    }
}
