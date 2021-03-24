package me.ofearr.uhccore;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GraceTimer{

    static Main plugin = Main.plugin;

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }



    @SuppressWarnings("deprecation")
    public static void graceRunnable(){

        new BukkitRunnable(){

            int timer = plugin.getConfig().getInt("Settings.Grace-Period");

            @Override
            public void run() {

                int seconds = timer % 60;
                int hours = seconds / 60;
                int minutes = hours % 60;

                hours = hours / 60;



                for(Player p : Bukkit.getOnlinePlayers()){
                    p.getScoreboard().getTeam("current_event").setPrefix(TranslateColour("&c&lGrace Period&f: "));
                    p.getScoreboard().getTeam("current_event").setSuffix(TranslateColour("&a" + hours + "h " + minutes + "m " + seconds + "s"));
                }
                if(timer == 600){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 10 minutes!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 10 minutes!"));

                } else if(timer == 300){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 5 minutes!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 5 minutes!"));

                } else if(timer == 60){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 1 minute!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 1 minute!"));

                } else if(timer == 30){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 30 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 30 seconds!"));

                } else if(timer == 15){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 15 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 15 seconds!"));

                } else if(timer == 10){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 10 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 10 seconds!"));

                } else if(timer == 5){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 5 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 5 seconds!"));

                } else if(timer == 4){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 4 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 4 seconds!"));

                } else if(timer == 3){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 3 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 3 seconds!"));

                } else if(timer == 2){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 2 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 2 seconds!"));

                } else if(timer == 1){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace period will end in 1 second!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period will end in 1 second!"));

                } else if(timer <= 0){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lGrace Period!"), TranslateColour("&c&lThe grace has ended!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe grace period has ended! PVP is now enabled and the border will now start shrinking!"));
                    plugin.graceActive = false;
                    ShrinkBorder.shrinkBorderRunnable();
                    this.cancel();
                }

                timer--;
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

}
