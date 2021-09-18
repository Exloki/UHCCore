package me.ofearr.uhccore.GUI;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class TeleportGUI {

    private static UHCCore plugin;

    public TeleportGUI(UHCCore uhcCore){
        this.plugin = uhcCore;
    }

    public Inventory GUI(){

        Inventory teleportInventory = Bukkit.createInventory(null, 36, StringUtil.TranslateColour("&cTeleport Menu"));

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, (byte) 3);
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, (byte) 15);

        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");

        filler.setItemMeta(fillerMeta);

        for(int a = 0; a < teleportInventory.getSize(); a++){
            teleportInventory.setItem(a, filler);
        }

        for(int i = 0; i < plugin.currentPlayers.size(); i++){
            Player player = Bukkit.getPlayer(plugin.currentPlayers.get(i));
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            ArrayList<String> skullLore = new ArrayList<>();

            skullMeta.setOwner(player.getName());
            skullMeta.setDisplayName(StringUtil.TranslateColour("&a" + player.getName()));

            skullLore.add(" ");
            skullLore.add(StringUtil.TranslateColour("&bTeam Member(s): " + plugin.teamManager.getPlayerTeamString(player)));
            skullLore.add(StringUtil.TranslateColour("&cClick to teleport to " + player.getName() + "."));

            skullMeta.setLore(skullLore);
            skull.setItemMeta(skullMeta);

            teleportInventory.setItem(i, skull);
        }

        return teleportInventory;

    }
}
