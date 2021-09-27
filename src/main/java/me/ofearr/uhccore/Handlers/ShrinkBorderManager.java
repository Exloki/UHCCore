package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShrinkBorderManager {

    private static UHCCore plugin;

    public ShrinkBorderManager(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public void shrinkBorderRunnable(){

        Long interval = plugin.getConfig().getLong("Settings.border-shrink-interval") * 20;

        new BukkitRunnable(){

            @Override
            public void run() {
                if(DeathMatchManager.deathMatchActive || !plugin.gameActive){
                    this.cancel();
                    return;
                }
                Double currentSize = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getSize();
                Double shrinkSize = plugin.getConfig().getDouble("Settings.border-shrink-amount");

                Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);
                Bukkit.getWorld(plugin.getConfig().getString("Settings.nether-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);
                Bukkit.getWorld(plugin.getConfig().getString("Settings.end-world-name")).getWorldBorder().setSize(currentSize - shrinkSize);

                double X = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getCenter().getX();
                double Z = Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().getCenter().getZ();

                String borderLocString = "&c" + X + "x &e" + Z + "z";

                for(Player p : Bukkit.getOnlinePlayers()){

                    plugin.uhcBoardManager.updateScoreboardValue(p, "current_border", borderLocString);
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }
}
