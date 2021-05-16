package cz.maku.servershandler.events;

import cz.maku.servershandler.ServersHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitEvents implements Listener {

    private final ServersHandler serversHandler;

    public BukkitEvents(ServersHandler serversHandler) {
        this.serversHandler = serversHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        serversHandler.getServer().setOnline(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        serversHandler.getServer().setOnline(Bukkit.getOnlinePlayers().size() - 1);
    }

}
