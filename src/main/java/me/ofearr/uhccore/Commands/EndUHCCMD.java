package me.ofearr.uhccore.Commands;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndUHCCMD implements CommandExecutor {

    private static UHCCore plugin;

    public EndUHCCMD(UHCCore uhcCore) {
        this.plugin = uhcCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("end-uhc")){
            if(!player.hasPermission("uhc.end")){
                player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
            } else{
                if(plugin.gameActive == false){
                    player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cThere is no game currently active! Start one via '/startuhc'."));
                } else{
                    plugin.endGame();
                }
            }
        }

        return false;
    }

}
