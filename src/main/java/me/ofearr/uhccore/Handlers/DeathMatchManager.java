package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DeathMatchManager implements Listener {

    private static UHCCore plugin;

    public DeathMatchManager(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public static ArrayList<Location> playerPlacedBlocks = new ArrayList<>();

    public static boolean deathMatchActive = false;
    private static boolean countDown = false;

    public void startDeathMatch(){
        List<String> spawnPointsList = plugin.getConfig().getStringList("Settings.Deathmatch-Spawns");

        try{
                for(Player p : Bukkit.getOnlinePlayers()){
                    Random rand = new Random();
                    int locIndex = rand.nextInt(spawnPointsList.size());

                    String selectedCoords = spawnPointsList.get(locIndex);

                    ArrayList<String> splitCoords = new ArrayList<>(Arrays.asList(selectedCoords.split(", ")));

                    int X = Integer.parseInt(splitCoords.get(0));
                    int Y = Integer.parseInt(splitCoords.get(1));
                    int Z = Integer.parseInt(splitCoords.get(2));

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

                String secondsString = seconds + "s";

                for(Player p : Bukkit.getOnlinePlayers()){

                    plugin.uhcBoardManager.setScoreboardCurrentEvent(p, "Death Match", secondsString);
                    p.setHealth(p.getMaxHealth());
                }

                if(timer == 10){
                    countDown = true;
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 10 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 10 seconds!"));

                } else if(timer == 5){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 5 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 5 seconds!"));

                } else if(timer == 4){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 4 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 4 seconds!"));

                } else if(timer == 3){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 3 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 3 seconds!"));

                } else if(timer == 2){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 2 seconds!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 2 seconds!"));

                } else if(timer == 1){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match will begin in 1 second!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &c&lThe death match will begin in 1 second!"));

                } else if(timer <= 0){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendTitle(StringUtil.TranslateColour("&a&lDeath Match!"), StringUtil.TranslateColour("&c&lThe death match has started!"));
                    }
                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe death match has started!"));
                    this.cancel();

                    for(Player p : Bukkit.getOnlinePlayers()){

                        plugin.uhcBoardManager.setScoreboardCurrentEvent(p, "Death Match", "&aNow!");
                        p.setHealth(p.getMaxHealth());
                    }

                    countDown = false;

                }

            }
        }.runTaskTimer(plugin, 0L, 20L);


    }

    @EventHandler
    public static void blockBreakDuringDeathmatch(BlockBreakEvent e){

        if(!plugin.gameActive) return;

        if (deathMatchActive && !countDown && playerPlacedBlocks.contains(e.getBlock().getLocation())){
            playerPlacedBlocks.remove(e.getBlock().getLocation());
        } else{
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void blockPlaceDuringDeathmatch(BlockPlaceEvent e){
        if(!plugin.gameActive) return;

        if(countDown){
            e.setCancelled(true);
        } else if (deathMatchActive && !countDown){
            playerPlacedBlocks.add(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public static void playerDamageEvent(EntityDamageEvent e){

        if(!plugin.gameActive) return;

        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;

        if(countDown){
            e.setCancelled(true);
        }
    }


    public static ArrayList<UUID> winners = new ArrayList<>();

    public static void winEvent(Player player){
        if(winners.size() < 3){
            winners.add(player.getUniqueId());
        }
        if(winners.size() >= 3){
            String winString;

            winString = StringUtil.TranslateColour("&a&l=========================\n" +
                    "        &6&lTop 3 Teams\n" +
                    " \n" +
                    "       &6&l1) " + plugin.teamManager.getPlayerTeamString(winners.get(2)) + " \n" +
                    "       &e&l2) " + plugin.teamManager.getPlayerTeamString(winners.get(1))  + " \n" +
                    "       &c&l3) " + plugin.teamManager.getPlayerTeamString(winners.get(0))  + "\n" +
                    "&a&l=========================");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(StringUtil.TranslateColour("&a&lGame Over!"), StringUtil.TranslateColour("&aThe game has been won!"));
                p.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game has been won!"));
                p.sendMessage(winString);
            }

            //Reset the deathmatch arena to its previous state if modified
            if(playerPlacedBlocks.size() >= 1){
                World world = Bukkit.getWorld(plugin.getConfig().getString("Settings.deathmatch-world-name"));

                for (Location playerPlacedBlock : playerPlacedBlocks) {
                    world.getBlockAt(playerPlacedBlock).setType(Material.AIR);
                }
            }

            plugin.endGame();

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void disableMovement(PlayerMoveEvent e){

        if(!plugin.gameActive) return;

        if(!countDown) return;
        Location from = e.getFrom();
        Location to = e.getTo();

        if(from == to){
            return;
        }else{
            e.setCancelled(true);
        }
    }
}
