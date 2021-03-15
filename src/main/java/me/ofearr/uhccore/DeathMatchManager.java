package me.ofearr.uhccore;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeathMatchManager implements Listener {

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    static Main plugin = Main.plugin;

    static ArrayList<Location> playerPlacedBlocks = new ArrayList<>();

    public static boolean deathMatchActive = false;
    private static boolean countDown = false;

    public static void startDeathMatch(){
        List<String> spawnPointsList = plugin.getConfig().getStringList("Settings.Deathmatch-Spawns");

        try{
                for(Player p : Bukkit.getOnlinePlayers()){
                    Random rand = new Random();
                    int locIndex = rand.nextInt(spawnPointsList.size());

                    String selectedCoords = spawnPointsList.get(locIndex);

                    ArrayList<String> splitCoords = new ArrayList<>(Arrays.asList(selectedCoords.split(", ")));

                    int X = Integer.valueOf(splitCoords.get(0));
                    int Y = Integer.valueOf(splitCoords.get(1));
                    int Z = Integer.valueOf(splitCoords.get(2));

                    Location spawnLoc = Bukkit.getWorld(plugin.getConfig().getString("Settings.deathmatch-world-name")).getBlockAt(X,Y,Z).getLocation();

                    p.teleport(spawnLoc);
                    p.setHealth(p.getMaxHealth());
                    spawnPointsList.remove(locIndex);
                }
        }catch (IndexOutOfBoundsException e){
            System.out.println("Caught Exception: You don't have enough spawn points set for the current amount of players!");
        }



        deathMatchActive = true;

        new BukkitRunnable() {
            int timer = 10;
            @Override
            public void run() {
                int seconds = timer % 60;

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.getScoreboard().getTeam("current_event").setPrefix(TranslateColour("&c&lDeath Match&f: "));
                    p.getScoreboard().getTeam("current_event").setSuffix(TranslateColour(seconds + "s"));
                }
                if(timer == 10){
                    countDown = true;
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 10 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 10 seconds!"));

                } else if(timer == 5){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 5 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 5 seconds!"));

                } else if(timer == 4){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 4 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 4 seconds!"));

                } else if(timer == 3){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 3 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 3 seconds!"));

                } else if(timer == 2){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 2 seconds!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 2 seconds!"));

                } else if(timer == 1){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match will begin in 1 second!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 1 second!"));

                } else if(timer <= 0){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(TranslateColour("&a&lDeath Match!"), TranslateColour("&c&lThe death match has started!"));
                    }
                    Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe death match has started!"));
                    this.cancel();
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.getScoreboard().getTeam("current_event").setPrefix(TranslateColour("&c&lDeath Match&f: "));
                        p.getScoreboard().getTeam("current_event").setSuffix(TranslateColour("&aNow!"));
                        p.setHealth(p.getMaxHealth());
                        countDown = false;
                    }

                }

            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);


    }

    @EventHandler
    public static void blockBreakDuringDeathmatch(BlockBreakEvent e){
        if (deathMatchActive == true && countDown == false && playerPlacedBlocks.contains(e.getBlock().getLocation())){
            playerPlacedBlocks.remove(e.getBlock().getLocation());
        } else{
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void blockPlaceDuringDeathmatch(BlockPlaceEvent e){
        if(countDown == true){
            e.setCancelled(true);
        } else if (deathMatchActive == true && countDown == false){
            playerPlacedBlocks.add(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public static void playerDamageEvent(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;

        if(countDown == true){
            e.setCancelled(true);
        }
    }


    static ArrayList<String> winners = new ArrayList<>();

    public static void winEvent(Player player){
        if(winners.size() < 3){
            winners.add(player.getName());
        }
        if(winners.size() >= 3){
            System.out.println("There are currently 3 winners, sending command.");
            String winString = "";

            winString = TranslateColour("&a&l=========================\n" +
                    "        &6&lTop 3 Winners\n" +
                    " \n" +
                    "       &6&l1) " + winners.get(2) + " \n" +
                    "       &e&l2) " + winners.get(1)  + " \n" +
                    "       &c&l3) " + winners.get(0)  + "\n" +
                    "&a&l=========================");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(TranslateColour("&a&lGame Over!"), TranslateColour("&aThe game has been won!"));
                p.sendMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game has been won!"));
                p.sendMessage(winString);
            }

            plugin.currentPlayers.clear();
            plugin.deadPlayers.clear();
            plugin.gameActive = false;
            deathMatchActive = false;
            winners.clear();

            //Reset the deathmatch arena to its previous state if modified
            if(playerPlacedBlocks.size() >= 1){
                World world = Bukkit.getWorld(plugin.getConfig().getString("Settings.deathmatch-world-name"));

                for(int i = 0; i < playerPlacedBlocks.size(); i++){
                    world.getBlockAt(playerPlacedBlocks.get(i)).setType(Material.AIR);
                }
            }
        }
    }
}
