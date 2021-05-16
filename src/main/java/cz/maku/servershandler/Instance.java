package cz.maku.servershandler;

import cz.maku.servershandler.events.BukkitEvents;
import lombok.Getter;
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
        getServer().getPluginManager().registerEvents(new BukkitEvents(serversHandler), this);
    }

    @Override
    public void onDisable() {
        serversHandler.stop();
    }
}
