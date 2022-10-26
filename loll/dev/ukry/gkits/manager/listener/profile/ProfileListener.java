package dev.ukry.gkits.manager.listener.profile;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class ProfileListener implements Listener {

    @EventHandler
    public void onPlayerPreJoin(PlayerPreLoginEvent event) {
        if(SharkGKits.getInstance().getProfileHandler().getProfile(event.getUniqueId()) != null) return;
        Profile profile = new Profile(event.getUniqueId());
        SharkGKits.getInstance().getProfileHandler().addProfile(profile);
    }
}