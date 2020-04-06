package me.abyss.redeemcodes;

import me.abyss.redeemcodes.cmd.RedeemCodesCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedeemCodesMain extends JavaPlugin {

    private static RedeemCodesMain plugin;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        plugin = this;
        int pluginId = 6974;
        Metrics metrics = new Metrics(plugin, pluginId);

        dataManager = new DataManager();
        getServer().getPluginCommand("redeemcodes").setExecutor(new RedeemCodesCommand());


        new UpdateChecker(this, 76786).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                MessageManager.log("There is not a new update available.");
            } else {
                MessageManager.log("There is a new update available.");
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
