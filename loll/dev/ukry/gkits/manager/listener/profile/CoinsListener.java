package dev.ukry.gkits.manager.listener.profile;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Config;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.profile.ProfileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CoinsListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!Config.COINS_SYSTEM.getAsBoolean()) return;
        Player killed = event.getEntity();
        Player killer = killed.getKiller();
        if(killer == null) return;
        ProfileHandler handler = SharkGKits.getInstance().getProfileHandler();
        Profile krProfile = handler.getProfile(killer.getUniqueId());
        Profile kdProfile = handler.getProfile(killed.getUniqueId());
        krProfile.addCoins(Config.COINS_REWARD_AMOUNT.getAsDouble(), "");
        kdProfile.removeCoins(Config.COINS_LOST_AMOUNT.getAsDouble(), "");
    }
}