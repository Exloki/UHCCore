package me.ofearr.uhccore.Enchants;

import me.ofearr.uhccore.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SpiderEnchant extends Enchantment implements Listener {

    private static int enchantID;
    public static int maxLevel;
    HashMap<UUID, Float> spiderHashmap = new HashMap<>();

    static Main plugin = Main.plugin;

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
