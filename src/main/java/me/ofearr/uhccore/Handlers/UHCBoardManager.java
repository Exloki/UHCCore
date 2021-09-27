package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class UHCBoardManager {

    private static UHCCore plugin;

    public UHCBoardManager(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public void setDefaultScoreboard(Player player){

        Scoreboard playerScoreboard = player.getScoreboard();
        boolean updatedVals = false;

        if(playerScoreboard.getTeam("current_players") != null){
            Team currentPlayers = playerScoreboard.getTeam("current_players");
            currentPlayers.setPrefix(StringUtil.TranslateColour("&a&lAlive Players: "));
            currentPlayers.setSuffix(StringUtil.TranslateColour("&e" + plugin.currentPlayers.size()));

            updatedVals = true;
        }


        if(playerScoreboard.getTeam("current_border") != null){
            Team currentBorder = playerScoreboard.getTeam("current_border");
            currentBorder.setPrefix(StringUtil.TranslateColour("&a&lCurrent Border: &f"));
            currentBorder.setSuffix(StringUtil.TranslateColour("&fNONE"));

            updatedVals = true;
        }

        if(playerScoreboard.getTeam("current_event") != null){
            Team currentBorder = playerScoreboard.getTeam("current_event");
            currentBorder.setPrefix(StringUtil.TranslateColour("&c&lGrace Period&f: "));
            currentBorder.setSuffix(StringUtil.TranslateColour("&a300s"));

            updatedVals = true;
        }


        if(updatedVals){
            player.setScoreboard(playerScoreboard);
        }

    }

    public void updateScoreboardValue(Player player, String team, Object value){

        Scoreboard playerScoreboard = player.getScoreboard();
        boolean updatedVals = false;

        if((playerScoreboard.getTeam("current_players") != null) && team.equalsIgnoreCase("current_players")){
            Team currentPlayers = playerScoreboard.getTeam("current_players");
            currentPlayers.setSuffix(StringUtil.TranslateColour("&e" + value.toString()));

            updatedVals = true;
        }

        if((playerScoreboard.getTeam("current_border") != null) && team.equalsIgnoreCase("current_border")){
            Team currentBorder = playerScoreboard.getTeam("current_border");
            currentBorder.setSuffix(StringUtil.TranslateColour("&f" + value.toString()));

            updatedVals = true;
        }

        if(updatedVals){
            player.setScoreboard(playerScoreboard);
        }

    }

    public void setScoreboardCurrentEvent(Player player, String eventName, Object value){

        Scoreboard playerScoreboard = player.getScoreboard();
        boolean updatedVals = false;

        if(playerScoreboard.getTeam("current_event") != null){
            Team currentEvent = playerScoreboard.getTeam("current_event");
            currentEvent.setPrefix(StringUtil.TranslateColour("&c&l" + eventName + "&f: "));
            currentEvent.setSuffix(StringUtil.TranslateColour("&f" + value.toString()));

            updatedVals = true;
        }

        if(updatedVals){
            player.setScoreboard(playerScoreboard);
        }

    }


}
