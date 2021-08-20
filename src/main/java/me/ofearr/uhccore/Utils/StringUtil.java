package me.ofearr.uhccore.Utils;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

}
