package me.abyss.redeemcodes;

import me.abyss.redeemcodes.command.redeemCodesCMD;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RedeemCodesMain extends JavaPlugin {

    private static RedeemCodesMain plugin;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        plugin = this;
        int pluginId = 6974; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(plugin, pluginId);

        dataManager = new DataManager();
        getServer().getPluginCommand("redeemcodes").setExecutor(new redeemCodesCMD());


        Logger logger = this.getLogger();
        new UpdateChecker(this, 76786).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                MessageManager.Log("There is not a new update available.");
            } else {
                MessageManager.Log("There is a new update available.");
            }
        });

    }

    @Override
    public void onDisable() {
        dataManager.saveData();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static RedeemCodesMain getPlugin() {
        return plugin;
    }
}
