package com.orca.FreezePlayers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class Freeze implements CommandExecutor {

    private final JavaPlugin plugin;

    public Freeze(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /freeze <player|@a|@a[team=teamName]>");
            return true;
        }

        String targetArg = args[0];

        // Handle @a (all players)
        if (targetArg.equalsIgnoreCase("@a")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                toggleFreeze(player, sender);
            }
            sender.sendMessage(ChatColor.GREEN + "All players have been toggled.");
            return true;
        }

        // Handle @a[team=teamName]
        if (targetArg.startsWith("@a[team=") && targetArg.endsWith("]")) {
            String teamName = targetArg.substring(8, targetArg.length() - 1); // Extract team name
            boolean found = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Team team = player.getScoreboard().getEntryTeam(player.getName());
                if (team != null && team.getName().equalsIgnoreCase(teamName)) {
                    toggleFreeze(player, sender);
                    found = true;
                }
            }
            if (found) {
                sender.sendMessage(ChatColor.GREEN + "All players on team '" + teamName + "' have been toggled.");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "No players found on team '" + teamName + "'.");
            }
            return true;
        }

        // Handle individual player
        Player target = Bukkit.getPlayer(targetArg);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        toggleFreeze(target, sender);
        return true;
    }

    private void toggleFreeze(Player target, CommandSender sender) {
        if (target.getScoreboardTags().contains("bypass")) {
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " has bypass permissions.");
            return;
        }

        if (target.getScoreboardTags().contains("STUCK")) {
            target.removeScoreboardTag("STUCK");
            target.sendMessage(ChatColor.GREEN + "You have been unfrozen.");
            sender.sendMessage(ChatColor.GREEN + target.getName() + " has been unfrozen.");
        } else {
            target.addScoreboardTag("STUCK");
            if (sender instanceof Player) {
                target.sendMessage(ChatColor.RED + "You have been frozen by an admin.");
            } else {
                target.sendMessage(ChatColor.RED + "You cannot move right now.");
            }
            sender.sendMessage(ChatColor.GREEN + target.getName() + " has been frozen.");
        }
    }
}
