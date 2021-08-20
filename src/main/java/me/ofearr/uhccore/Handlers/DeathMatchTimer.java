package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DeathMatchTimer {

    private static UHCCore plugin;

    public DeathMatchTimer(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public static int taskID = 0;

    public void deathMatchRunnable(){

        BukkitTask task = new BukkitRunnable(){


            int timer = plugin.getConfig().getInt("Settings.Death-Match-Timer");

            @Override
            public void run() {


                int seconds = timer % 60;
                int hours = seconds / 60;
                int minutes = hours % 60;

                hours = hours / 60;

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.getScoreboard().getTeam("current_event").setPrefix(StringUtil.TranslateColour("&c&lDeath Match&f: "));
                    p.getScoreboard().getTeam("current_event").setSuffix(StringUtil.TranslateColour("&a" + hours + "h " + minutes + "m " + seconds + "s"));
                }

                if(timer == 600){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 10 minutes!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 10 minutes!"));

                } else if(timer == 300){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 5 minutes!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 5 minutes!"));

                } else if(timer == 60){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 1 minute!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 1 minute!"));

                } else if(timer == 30){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 30 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 30 seconds!"));

                } else if(timer == 15){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 15 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 15 seconds!"));

                } else if(timer == 10){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 10 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 10 seconds!"));

                } else if(timer == 5){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 5 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 5 seconds!"));

                } else if(timer == 4){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 4 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 4 seconds!"));

                } else if(timer == 3){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 3 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 3 seconds!"));

                } else if(timer == 2){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 2 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 2 seconds!"));

                } else if(timer == 1){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lYou will be teleported for the death match in 1 second!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 1 second!"));

                } else if(timer <= 0){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match has started!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe death match has started!"));
                    this.cancel();
                    plugin.deathMatchManager.startDeathMatch();

                }

                timer--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        taskID = task.getTaskId();
    }
}
