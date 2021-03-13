package me.ofearr.uhccore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShrinkBorder {

    static Main plugin = Main.plugin;

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    public static void shrinkBorderRunnable(){

        Long interval = plugin.getConfig().getLong("Settings.border-shrink-interval") * 20;

        new BukkitRunnable(){

            @Override
            public void run() {
                if(DeathMatchManager.deathMatchActive == true){
                    this.cancel();
                }
                Double currentSize = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getSize();
                Double shrinkSize = plugin.getConfig().getDouble("Settings.border-shrink-amount");

                Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);
                Bukkit.getWorld(plugin.getConfig().getString("Settings.nether-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);
                Bukkit.getWorld(plugin.getConfig().getString("Settings.end-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);

                Double X = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getCenter().getX();
                Double Z = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getCenter().getZ();

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.getScoreboard().getTeam("current_border").setPrefix(TranslateColour("&c&lBorder Center&f: "));
                    p.getScoreboard().getTeam("current_border").setSuffix(TranslateColour("&c" + X + "x &e" + Z + "z"));
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, interval);
    }
}
