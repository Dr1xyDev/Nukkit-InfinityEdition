/*
 * nukkit Infinity by @Dr1xyDev 
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class HubCommand extends VanillaCommand {

    private static final int DEFAULT_PORT = 19132;

    public HubCommand(String name) {
        super(name,
                "Transfer yourself or another player to a server",
                "/hub <ip> [port]  |  /send <player> <ip> [port]");
        this.setPermission("nukkit.command.hub");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (commandLabel.equalsIgnoreCase("send")) {
            return executeSend(sender, args);
        }

        return executeHub(sender, args);
    }

    private boolean executeHub(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + "This command can only be used in-game.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(TextFormat.RED + "Usage: /hub <ip> [port]");
            return false;
        }

        Player player = (Player) sender;
        String ip = args[0];
        int port = DEFAULT_PORT;

        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
                if (port < 1 || port > 65535) {
                    sender.sendMessage(TextFormat.RED + "Invalid port number.");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + "Invalid port number.");
                return true;
            }
        }

        sender.sendMessage(TextFormat.YELLOW + "Connecting to " + ip + ":" + port + "...");
        transfer(player, ip, port, sender);
        return true;
    }

    private boolean executeSend(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nukkit.command.send")) {
            sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(TextFormat.RED + "Usage: /send <player> <ip> [port]");
            return false;
        }

        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(TextFormat.RED + "Player not found: " + args[0]);
            return true;
        }

        String ip = args[1];
        int port = DEFAULT_PORT;

        if (args.length >= 3) {
            try {
                port = Integer.parseInt(args[2]);
                if (port < 1 || port > 65535) {
                    sender.sendMessage(TextFormat.RED + "Invalid port number.");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + "Invalid port number.");
                return true;
            }
        }

        target.sendMessage(TextFormat.YELLOW + "You are being transferred to " + ip + ":" + port + "...");
        transfer(target, ip, port, sender);
        return true;
    }

    private void transfer(Player player, String ip, int port, CommandSender feedback) {
        boolean success = player.transferToServer(ip, port);
        if (!success) {
            feedback.sendMessage(TextFormat.RED + "Failed to start proxy for " + player.getName() + ". Is the proxy system initialized?");
        } else {
            if (feedback != player) {
                feedback.sendMessage(TextFormat.GREEN + "Transferred " + player.getName() + " to " + ip + ":" + port);
            }
        }
    }
}
