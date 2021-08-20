package me.ofearr.uhccore.Commands;

import me.ofearr.uhccore.Enchants.RomanNumeralUtil;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GiveEnchantCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("giveenchant")){
            if(!sender.hasPermission("uhc.enchants")){
                sender.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
            } else{
                if(args.length < 2){
                    sender.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] &8[&eEnchants&8] >> &cInsufficient Arguments! /giveenchant"));
                } else{
                    Player target = Bukkit.getPlayer(args[0]);
                    String enchantName = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);
                    Enchantment enchant = Enchantment.getByName(enchantName);
                    int enchantLevel = Integer.valueOf(args[2]);

                    ItemStack enchantBook = new ItemStack(Material.BOOK_AND_QUILL);
                    ItemMeta enchantMeta = enchantBook.getItemMeta();

                    enchantMeta.setDisplayName(StringUtil.TranslateColour("&d&kH&cEnchanted Book&d&kH"));
                    List<String> enchantLore = new ArrayList<>();

                    enchantLore.add(StringUtil.TranslateColour(" "));
                    enchantLore.add(StringUtil.TranslateColour("&aEnchant: &c" + enchantName + " " + RomanNumeralUtil.integerToNumeral(enchantLevel)));
                    enchantLore.add(StringUtil.TranslateColour("&aApplicable to: &e" + enchant.getItemTarget().name()));
                    enchantLore.add(" ");
                    enchantLore.add(StringUtil.TranslateColour("&7Drag and drop this enchant"));
                    enchantLore.add(StringUtil.TranslateColour("&7enchant onto an item in your"));
                    enchantLore.add(StringUtil.TranslateColour("&7inventory to apply."));

                    enchantMeta.addEnchant(enchant, enchantLevel, false);
                    enchantBook.setItemMeta(enchantMeta);

                    target.getInventory().addItem(enchantBook);
                    target.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] &8[&eEnchants&8] &8>> &aYou've been given " + enchantName + " at level " + enchantLevel + "!"));


                }
            }
        }

        return false;
    }
}
