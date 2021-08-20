package me.ofearr.uhccore.Enchants;

import me.ofearr.uhccore.UHCCore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SpiderEnchantListener implements Listener {

    HashMap<UUID, Float> spiderHashmap = new HashMap<>();

    private static UHCCore plugin;

    public SpiderEnchantListener(UHCCore uhcCore){
        this.plugin = uhcCore;
    }


    @EventHandler
    public void spiderEnchantHandler(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Block block = e.getTo().getBlock();

        //Just to make sure no null pointers are fired off and the event is not ran for no reason
        if(player.getInventory().getBoots() == null) return;
        if(!(player.getInventory().getBoots().getItemMeta().hasEnchants())) return;
        if(block.getType() == Material.AIR) return;

        //If the player's boots contain the spider enchant
        if(player.getInventory().getBoots().getEnchantments().containsKey(Enchantment.getById(SpiderEnchant.getID()))){

            //Logic to move faster when walking through webs
            if(block.getType() == Material.WEB){
                float currentMovementSpeed = player.getWalkSpeed();
                float newMovementSpeed;

                if(!(spiderHashmap.containsKey(player.getUniqueId()))){
                    spiderHashmap.put(player.getUniqueId(), currentMovementSpeed);
                }

                //Temporarily set the player walk speed to 2.5 times what it already is when walking through the web for lvl 1 spider enchant
                if(player.getInventory().getBoots().getEnchantments().get(Enchantment.getById(SpiderEnchant.getID())) == 1){
                    newMovementSpeed = (float) (currentMovementSpeed * 2.5);
                    player.setWalkSpeed(newMovementSpeed);
                    //Temporarily set the player walk speed to 4 times what it already is when walking through the web for lvl 2 spider enchant
                } else if(player.getInventory().getBoots().getEnchantments().get(Enchantment.getById(SpiderEnchant.getID())) == 2){
                    newMovementSpeed = currentMovementSpeed * 4;
                    player.setWalkSpeed(newMovementSpeed);
                    //Temporarily set the player walk speed to 5 times what it already is when walking through the web for lvl 3 spider enchant (max level)
                } else if(player.getInventory().getBoots().getEnchantments().get(Enchantment.getById(SpiderEnchant.getID())) == 3){
                    newMovementSpeed = currentMovementSpeed * 5;
                    player.setWalkSpeed(newMovementSpeed);
                }

                //Set the player walk speed back to what it was previously if the block isn't a web and they're in the map
            } else if(block.getType() != Material.WEB && spiderHashmap.containsKey(player.getUniqueId())){
                player.setWalkSpeed(spiderHashmap.get(player.getUniqueId()));
                spiderHashmap.remove(player);
                //If the player is walking on water then temporarily set the block type to glass
            } else if(block.getRelative(block.getX(), block.getY() -1, block.getZ()).getType() == Material.WATER){
                block.getRelative(block.getX(), block.getY() -1, block.getZ()).setType(Material.GLASS);

                //Set the block type back to water after 1s
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.getRelative(block.getX(), block.getY() -1, block.getZ()).setType(Material.WATER);
                    }
                }.runTaskLater(plugin, 20L);
            }
        }
    }

}
