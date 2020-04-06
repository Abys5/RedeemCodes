package me.abyss.redeemcodes.cmd;

import me.abyss.redeemcodes.Code;
import me.abyss.redeemcodes.MessageManager;
import me.abyss.redeemcodes.RedeemCodesMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RedeemCodes
 *
 * @author Abyss
 */
public class RedeemCodesCommand implements CommandExecutor {

    private static RedeemCodesMain plugin = RedeemCodesMain.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(label.equals("redeem")){
                if (args.length == 0) {
                    MessageManager.playerBanner(player, "No Code Provided");
                    return false;
                }

                Code code = plugin.getDataManager().getData(args[0]);

                if (code == null){
                    MessageManager.playerBanner(player, "Code is not Valid");
                    return false;
                }


                if (code.getPermission().equals("")){
                    code.executeCommands(player);
                    return true;
                } else {
                    if (player.hasPermission(code.getPermission())) {
                        code.executeCommands(player);
                        return true;
                    } else {
                        MessageManager.playerBanner(player, "You don't have permission to Redeem");
                        return false;
                    }
                }

            }

            if (args.length == 0)
            {
                userHelp(player);
                return true;
            }


            if (args.length > 0) {


                switch (args[0]){
                    case "help":
                        if (player.hasPermission("redeemcodes.admin.help")) {
                            adminHelp(player);
                            return true;
                        }
                        if (player.hasPermission("redeemcodes.user.help")) {
                            userHelp(player);
                            return true;
                        }


                    case "create":
                        if (player.hasPermission("redeemcodes.admin.create")) {
                            if (args.length < 2){
                                MessageManager.playerBanner(player, "No Code Provided");
                                return false;
                            }

                            if (!plugin.getDataManager().addData(new Code(args[1], new ArrayList<>(), "", -1, new ArrayList<>()))) {
                                MessageManager.playerBanner(player, "Code already exists");
                                return false;
                            }
                            MessageManager.playerBanner(player, "Code created");
                            return true;
                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;

                    case "remove":
                        if (player.hasPermission("redeemcodes.admin.remove")) {
                            if (args.length < 2){
                                MessageManager.playerBanner(player, "No Code Provided");
                                return false;
                            }

                            if (!plugin.getDataManager().delData(args[1])) {
                                MessageManager.playerBanner(player, "Code doesn't exist");
                                return false;
                            }

                            MessageManager.playerBanner(player, "Code removed");
                            return true;
                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;

                    case "show":
                        if (player.hasPermission("redeemcodes.admin.show")){
                            if (args.length < 2) {
                                MessageManager.playerBanner(player, "No Code Provided");
                                return false;
                            }
                            showCode(player, args[1]);
                            return true;
                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;

                    case "list":
                        if (player.hasPermission("redeemcodes.admin.list")) {
                            listCodes(player);
                            return true;
                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    case "setusage":
                        if (player.hasPermission("redeemcodes.admin.edit")) {
                            if (args.length < 3) {
                                MessageManager.playerBanner(player, "No Code or Amount Provided");
                                return false;
                            }

                            if (plugin.getDataManager().getData(args[1]) == null) {
                                MessageManager.playerBanner(player, "Code isn't valid");
                                return false;
                            }

                            if (!isNumeric(args[2])){
                                MessageManager.playerBanner(player, "Amount isn't a number");
                                return false;
                            }

                            plugin.getDataManager().getData(args[1]).setUsages(Integer.parseInt(args[2]));
                            MessageManager.playerBanner(player, args[1]+" usage set to "+args[2]);

                            return true;
                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    case "addcmd":
                        if (player.hasPermission("redeemcodes.admin.edit")) {
                            if (args.length < 3) {
                                MessageManager.playerBanner(player, "No Code or Command Provided");
                                return false;
                            }

                            if (plugin.getDataManager().getData(args[1]) == null) {
                                MessageManager.playerBanner(player, "Code isn't valid");
                                return false;
                            }

                            List<String> commandList = new ArrayList<>(Arrays.asList(args));
                            commandList.remove(0);
                            commandList.remove(0);
                            String commandStr = String.join(" ", commandList);

                            if (commandStr.isEmpty()){
                                MessageManager.playerBanner(player, "Command is not valid");
                                return false;
                            }

                            plugin.getDataManager().getData(args[1]).getCommands().add(commandStr);
                            MessageManager.playerBanner(player, "Command added to "+args[1]);
                            return true;

                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    case "delcmd":
                        if (player.hasPermission("redeemcodes.admin.edit")) {
                            if (args.length < 3) {
                                MessageManager.playerBanner(player, "No Code or Amount Provided");
                                return false;
                            }



                            if (plugin.getDataManager().getData(args[1]) == null) {
                                MessageManager.playerBanner(player, "Code isn't valid");
                                return false;
                            }

                            if (!isNumeric(args[2])) {
                                MessageManager.playerBanner(player, "Command Index isn't a number");
                                return false;
                            }

                            if (plugin.getDataManager().getData(args[1]).getCommands().get(Integer.parseInt(args[2])) == null){
                                MessageManager.playerBanner(player, "Command Index doesn't exist");
                                return false;
                            }

                            plugin.getDataManager().getData(args[1]).getCommands().remove(Integer.parseInt(args[2]));
                            MessageManager.playerBanner(player, "Command #"+args[2]+" Removed from "+args[1]);
                            return true;

                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    case "setperm":
                        if (player.hasPermission("redeemcodes.admin.edit")) {
                            if (args.length < 3) {
                                MessageManager.playerBanner(player, "No Code or Permission Provided");
                                return false;
                            }



                            if (plugin.getDataManager().getData(args[1]) == null) {
                                MessageManager.playerBanner(player, "Code isn't valid");
                                return false;
                            }





                            plugin.getDataManager().getData(args[1]).setPermission(args[2]);
                            MessageManager.playerBanner(player, args[1]+" is restricted to "+args[2]);
                            return true;

                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    case "clearperm":
                        if (player.hasPermission("redeemcodes.admin.edit")) {
                            if (args.length < 2) {
                                MessageManager.playerBanner(player, "No Code");
                                return false;
                            }



                            if (plugin.getDataManager().getData(args[1]) == null) {
                                MessageManager.playerBanner(player, "Code isn't valid");
                                return false;
                            }





                            plugin.getDataManager().getData(args[1]).setPermission("");
                            MessageManager.playerBanner(player, args[1]+" is unrestricted");
                            return true;

                        }
                        MessageManager.playerBanner(player, "You don't have permission");
                        return false;


                    default:
                        MessageManager.playerBanner(player, "Check /rc help for the Commands");
                        return false;
                }
            }

        }
        return false;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void betterMessage(Player player, List<String> Message){
        String msgToSend = "";
        for (String line: Message) {
            msgToSend += line + "\n" + ChatColor.RESET;
        }
        player.sendMessage(msgToSend);
    }

    private String commandLine(String command, String options, String description) {
        return "    "+ChatColor.DARK_AQUA+"\u00BB "+ChatColor.WHITE+command+" "+ChatColor.GRAY+options+" " + ChatColor.DARK_GRAY + "- "+ChatColor.DARK_AQUA+description+ChatColor.RESET;
    }

    private void betterDisplay(Player player, List<String> msg, String header){
        List<String> message = new ArrayList<>();
        message.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH+"+---------------------------------------------+");
        message.add(ChatColor.AQUA + "" + ChatColor.BOLD+" Redeem-Codes "+ChatColor.RESET + "" + ChatColor.GRAY + header);
        message.addAll(msg);
        message.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH+"+---------------------------------------------+");

        betterMessage(player, message);
    }

    private void showCode(Player player, String code) {

        Code codeObj = plugin.getDataManager().getData(code);
        if (codeObj == null) {
            MessageManager.playerBanner(player, code + "doesn't exist");
            return;
        }

        List<String> message = new ArrayList<>();

        message.add(" ");
        String usages = codeObj.getUsages() == -1 ? ChatColor.GREEN+"Infinite" : codeObj.getUsages()+"";
        message.add("    "+ChatColor.AQUA+"Uses left: "+ChatColor.RESET+usages);
        message.add(" ");
        String perm = codeObj.getPermission().equals("") ? ChatColor.RED+"None" : codeObj.getPermission();
        message.add("    "+ChatColor.AQUA+"Permission Required: "+ChatColor.RESET+perm);
        message.add("    ");
        message.add("    "+ChatColor.AQUA+"Commands:");
        List<String> commands = codeObj.getCommands();




        if (commands != null) {
            if (commands.size() > 0) {
                for (int i = 0; i < commands.size(); i++) {
                    message.add("    " + ChatColor.DARK_AQUA + ChatColor.BOLD + "" + i + " " + ChatColor.RESET + commands.get(i));
                }
            }
        } else {
            message.add("      "+ChatColor.RED+ChatColor.BOLD+"None");
        }

        message.add("    ");

        betterDisplay(player, message, "Code Info: "+ChatColor.GREEN+code);
    }

    private void listCodes(Player player) {
        List<String> message = new ArrayList<>();
        message.add("  ");

        plugin.getDataManager().getCodeMap().forEach(((key, code) -> {
            String usages = code.getUsages() == -1 ? ChatColor.GREEN+"Infinite" : code.getUsages()+"";
            message.add("    "+ChatColor.BOLD+code.getCode()+ChatColor.RESET+ChatColor.DARK_GRAY+" - "+ChatColor.BOLD+ChatColor.AQUA+" Usages: "+ChatColor.RESET+usages);
        }));

        betterDisplay(player, message, "Code List");
    }

    private void userHelp(Player player) {
        List<String> message = new ArrayList<>();
        message.add("  ");
        message.add(commandLine("/redeem ", "[CODE]", "Redeem a code"));

        betterDisplay(player, message, "Fields: [] = Required");
    }

    private void adminHelp(Player player) {
        List<String> message = new ArrayList<>();
        message.add(ChatColor.GRAY+" Usage: /redeemcodes or /rc");
        message.add("  ");
        message.add(commandLine("/redeem", "[CODE]", "Redeem a code"));
        message.add(commandLine("/rc create", "[CODE]", "Makes a code"));
        message.add(commandLine("/rc remove", "[CODE]", "Removes a code"));
        message.add(commandLine("/rc show", "[CODE]", "Show code info"));
        message.add(commandLine("/rc list", "", "Lists all codes"));
        message.add(commandLine("/rc setusage", "[CODE] [NUM]", "Set code usage"));
        message.add(commandLine("/rc addcmd", "[CODE] [CMD]", "Adds a command"));
        message.add(commandLine("/rc delcmd", "[CODE] [CMD INDEX]", "Removes a command"));
        message.add(commandLine("/rc setperm", "[CODE] [PERM]", "Sets permission"));
        message.add(commandLine("/rc clearperm", "[CODE]", "Clears permission"));

        betterDisplay(player, message, "Fields: [] = Required");
    }
}
