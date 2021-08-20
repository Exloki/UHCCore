package me.ofearr.uhccore.Enchants;

import me.ofearr.uhccore.UHCCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class CustomEnchantmentHandler implements Listener {

    //To translate & to §, takes a string argument and returns an updated string of what was passed in
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
        //Some NPE cancellation (Null Pointer Exception)
        if(book == null) return;
        if(itemstack == null) return;
        if(e.getSlotType() == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if(inv.getType() != InventoryType.PLAYER) return;

        //If the type of the dragged item is not the material of our custom enchant book then return as we don't want to continue with this
        if(book.getType() != Material.BOOK_AND_QUILL){
            return;
        } else{
            //Register the enchantment with our util to apply the enchant using drag and drop
           if(book.containsEnchantment(Enchantment.getById(SpiderEnchant.getID()))) {
               //If the returned item stack from the util is gonna be null then return
               if(bookHandler(Enchantment.getById(SpiderEnchant.getID()), book, itemstack, SpiderEnchant.maxLevel, "&cSpider") == null){
                   return;
               } else{
                   //Set the clicked item (item book is dragged onto) to a copy of the item but with the enchant applied
                   e.setCurrentItem(bookHandler(Enchantment.getById(SpiderEnchant.getID()), book, itemstack, SpiderEnchant.maxLevel, "&cSpider"));
                   //Remove the book from their inventory
                   book.setType(Material.AIR);
                   //Update the inventory to apply the changes
                   player.updateInventory();
               }
           }

        }

    }

    //Cancels opening the book and quill inventory when right-clicked if it's one of our custom enchant books
    @EventHandler
    public void cancelBookOpening(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();

        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){
            //Some NPE cancellation (Null Pointer Exception)
            if(player.getInventory().getItemInHand() == null) return;
            if(!player.getInventory().getItemInHand().hasItemMeta()) return;
            if(!player.getInventory().getItemInHand().getItemMeta().hasLore()) return;
            if(!player.getInventory().getItemInHand().getItemMeta().hasEnchants()) return;
            if(player.getItemInHand().getItemMeta().getLore().contains(TranslateColour("&aEnchant: &c")) && player.getItemInHand().getType() == Material.BOOK_AND_QUILL){
                e.setCancelled(true);
            }
        }

    }

    //Method to apply the enchant book to the item it's dropped onto, takes an enchant argument (enchant to be applied), item stack for the book (book containing the enchant),
    // item stack for the item (item/custom book the enchant is to be applied to), the max level for the enchant we passed (so they cant combine 2 books and get something above the max level) and a lore display
    //(colour of the lore displayed on the item showing that the enchant is applied and what level the enchant is), returns a new item stack.
    public static ItemStack bookHandler(Enchantment enchant, ItemStack book, ItemStack itemStack, int maxLevel, String loreDisplay){

        if(book.getEnchantments().containsKey(enchant)){
            if(!itemStack.getEnchantments().containsKey(enchant)){

                for(int i = 0; i < UHCCore.registeredEnchants.get(i).length; i++){
                    if(itemStack.getType().toString().toLowerCase().contains(UHCCore.registeredEnchants.get(i)[i])){
                        break;
                    } else if(i == UHCCore.registeredEnchants.get(i).length && !(itemStack.getType().toString().toLowerCase().contains(UHCCore.registeredEnchants.get(i)[i]))){
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
