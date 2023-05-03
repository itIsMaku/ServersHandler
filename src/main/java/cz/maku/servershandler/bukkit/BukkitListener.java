package cz.maku.servershandler.bukkit;

import cz.maku.servershandler.ServersHandler;
import cz.maku.servershandler.server.SynchronizedServer;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class BukkitListener implements Listener {

    private final ServersHandler serversHandler;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SynchronizedServer synchronizedServer = serversHandler.local();
        synchronizedServer.setOnlinePlayers(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SynchronizedServer synchronizedServer = serversHandler.local();
        synchronizedServer.setOnlinePlayers(Bukkit.getOnlinePlayers().size() - 1);
    }

}
