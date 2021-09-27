package me.ofearr.uhccore.Enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SpiderEnchant extends Enchantment implements Listener {

    private static int enchantID;
    public static int maxLevel;

    public SpiderEnchant(int id) {
        super(id);
        enchantID = id;
        maxLevel = this.getMaxLevel();
    }

    public static int getID(){
        return enchantID;
    }

    @Override
    public String getName() {
        return "Spider";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_FEET;
    }


    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {

        return false;
    }
}
