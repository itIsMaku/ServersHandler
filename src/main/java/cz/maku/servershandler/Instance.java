package cz.maku.servershandler;

import cz.maku.servershandler.events.BukkitEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Instance extends JavaPlugin {

    @Getter
    private static Instance instance;
    @Getter
    private ServersHandler serversHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.serversHandler = new ServersHandler(this);
        serversHandler.upload();
        serversHandler.download();
        serversHandler.publish(true);
        serversHandler.subscribe();
        getServer().getPluginManager().registerEvents(new BukkitEvents(serversHandler), this);
        //Bukkit.getScheduler().runTaskTimer(this, () -> {
        //    serversHandler.getServers().forEach((name, server) -> System.out.println("§b" + name + " - " + server.getOnline() + "/" + server.getMaxOnline()));
        //}, 0, 40);
    }

    @Override
    public void onDisable() {
        serversHandler.stop();
    }
}
