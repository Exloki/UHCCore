package me.ofearr.uhccore.Commands;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.RandomTeleport;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartUHCCMD implements CommandExecutor {

    private static UHCCore plugin;

    public StartUHCCMD(UHCCore uhcCore) {
        this.plugin = uhcCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("start-uhc")){
                if(!player.hasPermission("uhc.start")){
                    player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
                } else{
                    if(plugin.gameActive == true){
                        player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cThe game has already started! Use /end-uhc to stop the current game."));
                    } else{
                        new BukkitRunnable(){

                            int gameStartTimer = 10;
                            @Override
                            public void run() {
                                if(plugin.gameActive = false){
                                    this.cancel();
                                    return;
                                }

                                if(gameStartTimer == 10){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 10 seconds!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 10 seconds!"));
                                } else if(gameStartTimer == 5){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 5 seconds!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 5 seconds!"));
                                } else if(gameStartTimer == 4){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 4 seconds!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 4 seconds!"));
                                } else if(gameStartTimer == 3){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 3 seconds!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 3 seconds!"));
                                } else if(gameStartTimer == 2){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 2 seconds!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 2 seconds!"));
                                } else if(gameStartTimer == 1){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game will start in 1 second!"));
                                    }
                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 1 second!"));
                                } else if(gameStartTimer <= 0){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(StringUtil.TranslateColour("&a&lGame Starting!"), StringUtil.TranslateColour("&a&lThe game has started!"));
                                        plugin.currentPlayers.add(p.getUniqueId());

                                        if(!plugin.teamManager.teamSharedSpawnsEnabled){
                                            RandomTeleport.randomTeleportPlayer(p);
                                        }

                                        plugin.uhcBoardManager.setDefaultScoreboard(p);
                                    }

                                    if(plugin.teamManager.teamSharedSpawnsEnabled){
                                        RandomTeleport.randomTeleportPlayerTeams();
                                    }

                                    Bukkit.getWorld(plugin.getConfig().getString("Settings.over-world-name")).getWorldBorder().setSize(plugin.getConfig().getDouble("Settings.default-border-size"));
                                    Bukkit.getWorld(plugin.getConfig().getString("Settings.nether-world-name")).getWorldBorder().setSize(plugin.getConfig().getDouble("Settings.default-border-size"));
                                    Bukkit.getWorld(plugin.getConfig().getString("Settings.end-world-name")).getWorldBorder().setSize(plugin.getConfig().getDouble("Settings.default-border-size"));

                                    Bukkit.broadcastMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aThe game has started! You will now be teleported to your spawn points..."));

                                    plugin.graceTimer.graceRunnable();
                                    this.cancel();
                                    plugin.gameActive = true;
                                    plugin.graceActive = true;

                                    plugin.teamManager.assignPlayersToTeams();
                                }
                                gameStartTimer--;
                            }
                        }.runTaskTimer(plugin, 0L, 20L);

                    }
                }
            }
        }

        return false;
    }

}
