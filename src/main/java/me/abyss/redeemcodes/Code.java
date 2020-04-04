package me.abyss.redeemcodes;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * RedeemCodes
 *
 * @author Abyss
 */
public class Code {

    private static final RedeemCodesMain plugin = RedeemCodesMain.getPlugin();

    private String code;
    private List<String> commands;
    private String permission = "";
    private int usages = -1;
    private List<String> playerUUIDUsed;


    public Code(String code, List<String> commands, String permission, int usages, List<String> playerNameUsed) {
        this.code = code;
        this.commands = commands;
        this.permission = permission;
        this.usages = usages;
        this.playerUUIDUsed = playerNameUsed;
    }

    public String getCode() {
        return code;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
        plugin.getDataManager().saveData();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
        plugin.getDataManager().saveData();
    }

    public List<String> getPlayerUUIDUsed() {
        return playerUUIDUsed;
    }

    public int getUsages() {
        return usages;
    }

    public void setUsages(int usages) {
        this.usages = usages;
        plugin.getDataManager().saveData();
    }

    private String parseCommand(String command, Player player) {
        command = command.replace("$player", player.getName());
        command = command.replace("$x", ""+player.getLocation().getBlockX());
        command = command.replace("$y", ""+player.getLocation().getBlockY());
        command = command.replace("$z", ""+player.getLocation().getBlockZ());
        return command;
    }

    public void executeCommands(Player player) {
        RedeemCodesMain plugin = RedeemCodesMain.getPlugin();
        if (usages == -1 || usages > 0) {

            if (playerUUIDUsed.contains(player.getUniqueId().toString())){
                MessageManager.playerBanner(player, "Code already claimed");
                return;
            }

            if (!playerUUIDUsed.contains(player.getUniqueId().toString())) {
                playerUUIDUsed.add(player.getUniqueId().toString());
                plugin.getDataManager().saveData();
            }


            commands.forEach(command -> {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), parseCommand(command, player));
            });
            if (usages > 0)
                usages--;
            return;
        }
        MessageManager.playerBanner(player, "Code is all used up!");
    }

}
