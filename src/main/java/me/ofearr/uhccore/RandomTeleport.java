package me.ofearr.uhccore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RandomTeleport {

    static Main plugin = Main.plugin;

    public static void randomTeleportPlayer(Player player){

        int teleportBounds = plugin.getConfig().getInt("Settings.random-teleport-bounds");

        int X = (int) (Math.random() * teleportBounds);
        int Z = (int) (Math.random() * teleportBounds);
        int Y = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getHighestBlockYAt(X, Z);

        Location randomLoc = new Location(Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")), X, Y, Z);
        player.teleport(randomLoc);
    }
}
