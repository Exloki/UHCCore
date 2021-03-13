package me.ofearr.uhccore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class UHCBoardManager {

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    static Main plugin = Main.plugin;

    public static void setScoreboard(Player player){

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective("UHCBoard" , "UHCBoard");

        objective.setDisplayName(TranslateColour(plugin.getConfig().getString("Settings.score-board-display-name")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team currentPlayers = board.registerNewTeam("current_players");
        currentPlayers.addEntry(ChatColor.GOLD.toString());
        currentPlayers.setPrefix(TranslateColour("&a&lAlive Players: "));
        currentPlayers.setSuffix(TranslateColour("&e" + plugin.currentPlayers.size()));
        objective.getScore(ChatColor.GOLD.toString()).setScore(7);

        Team currentBorder = board.registerNewTeam("current_border");
        currentBorder.addEntry(ChatColor.BLUE.toString());
        currentBorder.setPrefix(TranslateColour("&a&lCurrent Border: &f" + plugin.currentPlayers.size()));
        currentBorder.setSuffix(" ");
        objective.getScore(ChatColor.BLUE.toString()).setScore(5);

        Team currentEvent = board.registerNewTeam("current_event");
        currentEvent.addEntry(ChatColor.RED.toString());
        currentEvent.setPrefix(TranslateColour("&c&lGrace Period&f: "));
        currentEvent.setSuffix(TranslateColour("&a300s"));
        objective.getScore(ChatColor.RED.toString()).setScore(3);

        player.setScoreboard(board);


    }
}
