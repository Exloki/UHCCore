package me.ofearr.uhccore.Enchants;

import me.ofearr.uhccore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomEnchantmentListener implements Listener {

    HashMap<UUID, Float> spiderHashmap = new HashMap<>();

    static Main plugin = Main.plugin;

    static String TranslateColour(String text){
        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
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

    @EventHandler
    public void applyEnchantHandler(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack book = e.getCursor();
        ItemStack itemstack = e.getCurrentItem();
        if(book == null) return;
        if(itemstack == null) return;
        if(e.getSlotType() == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if(inv.getType() != InventoryType.PLAYER) return;

        if(book.getType() != Material.BOOK_AND_QUILL){
            return;
        } else{

           if(book.containsEnchantment(Enchantment.getById(SpiderEnchant.getID()))) {
               if(bookHandler(Enchantment.getById(SpiderEnchant.getID()), book, itemstack, SpiderEnchant.maxLevel, "&cSpider") == null){
                   return;
               } else{
                   e.setCurrentItem(bookHandler(Enchantment.getById(SpiderEnchant.getID()), book, itemstack, SpiderEnchant.maxLevel, "&cSpider"));
                   player.updateInventory();
               }
           }

        }

    }

    public static ItemStack bookHandler(Enchantment enchant, ItemStack book, ItemStack itemStack, int maxLevel, String loreDisplay){

        if(book.getEnchantments().containsKey(enchant)){
            if(!itemStack.getEnchantments().containsKey(enchant)){

                for(int i = 0; i < Main.registeredEnchants.get(i).length; i++){
                    if(itemStack.getType().toString().toLowerCase().contains(Main.registeredEnchants.get(i)[i])){
                        break;
                    } else if(i == Main.registeredEnchants.get(i).length && !(itemStack.getType().toString().toLowerCase().contains(Main.registeredEnchants.get(i)[i]))){
                        return null;
                    }
                }


                int bookLevel = book.getEnchantments().get(enchant);

                book.setType(Material.AIR);

                ItemStack enchantedStack = itemStack;
                ItemMeta enchantedMeta = enchantedStack.getItemMeta();

                enchantedMeta.addEnchant(enchant, bookLevel, false);
                List<String> enchantLore = enchantedMeta.getLore();

                enchantLore.add(TranslateColour(loreDisplay) + RomanNumeralUtil.integerToNumeral(bookLevel));
                enchantedMeta.setLore(enchantLore);

                enchantedStack.setItemMeta(enchantedMeta);

                itemStack = itemStack;

            } else{
                if(!(itemStack.getEnchantments().containsKey(enchant))) return null;
                if(!(book.getEnchantments().containsKey(enchant))) return null;
                if(book.getType() != Material.BOOK_AND_QUILL) return null;
                if(itemStack.getType() != Material.BOOK_AND_QUILL) return null;

                int currentLevel = itemStack.getEnchantmentLevel(enchant);
                int bookLevel = book.getEnchantments().get(enchant);

                if(currentLevel == bookLevel && bookLevel + 1 != maxLevel){
                    book.setType(Material.AIR);

                    ItemStack enchantedStack = itemStack;
                    ItemMeta enchantedMeta = enchantedStack.getItemMeta();

                    enchantedMeta.addEnchant(enchant, bookLevel + 1, false);
                    List<String> enchantLore = enchantedMeta.getLore();

                    enchantLore.add(TranslateColour(loreDisplay) + RomanNumeralUtil.integerToNumeral(bookLevel + 1));
                    enchantedMeta.setLore(enchantLore);

                    enchantedStack.setItemMeta(enchantedMeta);
                    itemStack = itemStack;
                } else{
                    return null;
                }

            }
        }

        return itemStack;
    }
}
