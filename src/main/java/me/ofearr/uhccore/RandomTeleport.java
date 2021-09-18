package me.ofearr.uhccore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RandomTeleport {

    private static UHCCore plugin;

    public RandomTeleport(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public static void randomTeleportPlayer(Player player){

        int teleportBounds = plugin.getConfig().getInt("Settings.random-teleport-bounds");

        int X = (int) (Math.random() * teleportBounds);
        int Z = (int) (Math.random() * teleportBounds);
        int Y = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getHighestBlockYAt(X, Z);

        Location randomLoc = new Location(Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")), X, Y, Z);

        player.teleport(randomLoc);
    }

    public static void randomTeleportPlayerTeams(){

        int teleportBounds = plugin.getConfig().getInt("Settings.random-teleport-bounds");

        for(List<UUID> playerTeam : plugin.teamManager.getPlayerTeams()){

            for(UUID playerUUID : playerTeam){

                int X = (int) (Math.random() * teleportBounds);
                int Z = (int) (Math.random() * teleportBounds);
                int Y = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getHighestBlockYAt(X, Z);

                Location randomLoc = new Location(Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")), X, Y, Z);

                if(Bukkit.getPlayer(playerUUID) != null){
                    Player teamPlayer = Bukkit.getPlayer(playerUUID);

                    teamPlayer.teleport(randomLoc);
                }

            }
        }

    }
}
