package me.ofearr.uhccore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class UHCEventHandler implements Listener {

    static Main plugin = Main.plugin;

    String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        Player player = e.getEntity();

        if(plugin.gameActive == false){
            return;
        }

        if(DeathMatchManager.deathMatchActive == false && plugin.currentPlayers.size() -1 <= plugin.getConfig().getInt("Settings.Force-Death-Match")){
            int taskID = DeathMatchTimer.taskID;
            Bukkit.getScheduler().cancelTask(taskID);

            DeathMatchManager.startDeathMatch();
        }

        else if(DeathMatchManager.deathMatchActive == true){
            if(plugin.currentPlayers.size() <= 3){
                DeathMatchManager.winEvent(player);
            }
        }

        plugin.currentPlayers.remove(player.getUniqueId());
        plugin.deadPlayers.add(player.getUniqueId());

        for(Player p : Bukkit.getOnlinePlayers()){
            p.getScoreboard().getTeam("current_players").setPrefix(TranslateColour("&a&lAlive Players: "));
            p.getScoreboard().getTeam("current_players").setSuffix(TranslateColour("&e" + plugin.currentPlayers.size()));
        }

        player.setGameMode(GameMode.SURVIVAL);

        for(Player p : Bukkit.getOnlinePlayers()){
            p.hidePlayer(player);
        }

        player.setFlying(true);

        ItemStack teleportCompass = new ItemStack(Material.COMPASS);

        ItemMeta compassMeta = teleportCompass.getItemMeta();
        ArrayList<String> compassLore = new ArrayList<>();

        compassMeta.setDisplayName(TranslateColour("&cTeleport Compass"));
        compassLore.add(" ");
        compassLore.add(TranslateColour("&6Item Ability: Teleport &e(RIGHT-CLICK)"));
        compassLore.add(TranslateColour("&7Teleport to active players during the game."));

        compassMeta.setLore(compassLore);
        teleportCompass.setItemMeta(compassMeta);


    }

    @EventHandler
    public void useCompass(PlayerInteractEvent e){

        Player player = e.getPlayer();
        Action action = e.getAction();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
            if(player.getItemInHand() == null){
                return;
            }
            if(player.getItemInHand().getItemMeta() == null){
                return;
            }

            if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(TranslateColour("&cTeleport Compass"))){
                player.openInventory(TeleportGUI.GUI());
            }
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        if(plugin.gameActive == false) return;

        Player player = (Player) e.getEntity();

        if(plugin.deadPlayers.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }
}
