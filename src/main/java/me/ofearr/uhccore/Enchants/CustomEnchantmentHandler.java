package me.ofearr.uhccore.Enchants;

import me.ofearr.uhccore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomEnchantmentHandler implements Listener {

    static String TranslateColour(String text){
        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
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
                   book.setType(Material.AIR);
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
