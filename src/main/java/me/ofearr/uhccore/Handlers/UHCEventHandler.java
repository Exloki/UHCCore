package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.GUI.TeleportGUI;
import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class UHCEventHandler implements Listener {

    private static UHCCore plugin;

    public UHCEventHandler(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        Player player = e.getEntity();

        if(plugin.gameActive == false){
            return;
        }

        if(plugin.deathMatchManager.deathMatchActive == false && plugin.currentPlayers.size() -1 <= plugin.getConfig().getInt("Settings.Force-Death-Match")){
            int taskID = DeathMatchTimer.taskID;
            Bukkit.getScheduler().cancelTask(taskID);

            plugin.deathMatchManager.startDeathMatch();
        }

        else if(DeathMatchManager.deathMatchActive == true){
            if(plugin.currentPlayers.size() <= 3){
                DeathMatchManager.winEvent(player);
            }
        }

        plugin.currentPlayers.remove(player.getUniqueId());
        plugin.deadPlayers.add(player.getUniqueId());

        for(Player p : Bukkit.getOnlinePlayers()){
            p.getScoreboard().getTeam("current_players").setPrefix(StringUtil.TranslateColour("&a&lAlive Players: "));
            p.getScoreboard().getTeam("current_players").setSuffix(StringUtil.TranslateColour("&e" + plugin.currentPlayers.size()));
        }

        player.setGameMode(GameMode.SURVIVAL);

        for(Player p : Bukkit.getOnlinePlayers()){
            p.hidePlayer(player);
        }

        player.setFlying(true);

        ItemStack teleportCompass = new ItemStack(Material.COMPASS);

        ItemMeta compassMeta = teleportCompass.getItemMeta();
        ArrayList<String> compassLore = new ArrayList<>();

        compassMeta.setDisplayName(StringUtil.TranslateColour("&cTeleport Compass"));
        compassLore.add(" ");
        compassLore.add(StringUtil.TranslateColour("&6Item Ability: Teleport &e(RIGHT-CLICK)"));
        compassLore.add(StringUtil.TranslateColour("&7Teleport to active players during the game."));

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

            if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(StringUtil.TranslateColour("&cTeleport Compass"))){
                player.openInventory(new TeleportGUI(plugin).GUI());
            }
        }
    }

    @EventHandler
    public void compassGUIHandler(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        //NPE Handling
        if(item == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if(item.getType() != Material.SKULL_ITEM){
            e.setCancelled(true);
            return;
        }
        if(!item.hasItemMeta()){
            e.setCancelled(true);
            return;
        }

        //If its the teleport menu then get the player which the skull belongs to and teleport the player who clicked to the skull owner
        if(e.getView().getTitle().equalsIgnoreCase(StringUtil.TranslateColour("&cTeleport Menu"))){
            String skullDisplay = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            Player target = Bukkit.getPlayer(skullDisplay);

            player.teleport(target);
            player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &aYou've been teleported to " + target.getName() + "!"));
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