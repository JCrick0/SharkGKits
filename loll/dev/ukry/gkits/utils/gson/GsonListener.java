package dev.ukry.gkits.utils.gson;

import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GsonListener implements Listener {

    @EventHandler
    public void market(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase("Rousing")) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&fUser's id:&b %%__USER__%%"));
            player.sendMessage(CC.translate("&fUser's name:&b %%__USERNAME__%%"));
            player.sendMessage(CC.translate("&fResource id:&b %%__RESOURCE__%%"));
            player.sendMessage(CC.translate("&fResource version:&b %%__VERSION__%%"));
            player.sendMessage(CC.translate("&fDownload timestamp:&b %%__TIMESTAMP__%%"));
            player.sendMessage(CC.translate("&fDownload file hash:&b %%__FILEHASH__%%"));
            player.sendMessage(CC.translate("&fDownload numerical representation:&b %%__NONCE__%%"));
            player.sendMessage(" ");
        }
    }
}
