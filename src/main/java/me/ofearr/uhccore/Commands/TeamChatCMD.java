package me.ofearr.uhccore.Commands;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TeamChatCMD implements CommandExecutor {

    private static UHCCore instance;

    public TeamChatCMD(UHCCore uhcCore) {
        this.instance = uhcCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){

            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("teamchat")){
                if(!player.hasPermission("uhc.teamchat")){
                    player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
                } else{

                    if(!(args.length > 0)){
                        player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cInsufficient arguments: /teamchat <message>"));
                    } else {

                        if(instance.gameActive){
                            List<UUID> playerTeam = instance.teamManager.getPlayerTeam(player);

                            StringBuilder sb = new StringBuilder();

                            for(String s : args){
                                sb.append(s + " ");
                            }

                            String message = StringUtil.TranslateColour("&8[&d&lUHC&8] &8[&b&lTeam Chat&8] " + player.getName() + " >> &c" + sb.toString());

                            for(UUID playerUUID : playerTeam){

                                if(Bukkit.getPlayer(playerUUID) != null){
                                    Player teamPlayer = Bukkit.getPlayer(playerUUID);

                                    teamPlayer.sendMessage(message);
                                }

                            }

                        } else {
                            player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cThere is no game currently active!"));
                        }

                    }


                }
            }
        }

        return false;
    }
}
