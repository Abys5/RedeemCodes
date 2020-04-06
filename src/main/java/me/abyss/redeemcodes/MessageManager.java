package me.abyss.redeemcodes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * RedeemCodes
 *
 * @author Abyss
 */
public class MessageManager {
    public static void playerBanner(Player player, String msg){
        player.sendMessage(ChatColor.GRAY+""+ChatColor.BOLD+"["+ChatColor.AQUA+ChatColor.BOLD+"RedeemCode"+ChatColor.GRAY+ChatColor.BOLD+"] "+ChatColor.RESET+msg);
    }

    public static void log(String msg) {
        RedeemCodesMain.getPlugin().getLogger().info(msg);
    }
}
