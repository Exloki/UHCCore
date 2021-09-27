package me.ofearr.uhccore.Handlers;

import me.ofearr.uhccore.UHCCore;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamManager implements Listener {

    private static UHCCore instance;

    public TeamManager(UHCCore uhcCore){
        this.instance = uhcCore;
    }

    public boolean teamsActive = instance.getConfig().getBoolean("teams-enabled");
    public boolean teamSharedSpawnsEnabled = instance.getConfig().getBoolean("team-shared-spawns");
    private int teamSizes = instance.getConfig().getInt("team-sizes");

    private static ArrayList<List<UUID>> playerTeams = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageTeamMember(EntityDamageByEntityEvent e){

        if(!instance.gameActive) return;
        if(!teamsActive) return;

        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){

            Player damager = (Player) e.getDamager();
            Player target = (Player) e.getEntity();

            for(List<UUID> teams : playerTeams){

                if(teams.contains(damager.getUniqueId()) && teams.contains(target.getUniqueId())){
                    e.setCancelled(true);
                }
            }
        }

    }

    public void assignPlayersToTeams(){

        if(teamsActive){

            List<UUID> teamBufferList = new ArrayList<>();

            for(Player player : Bukkit.getOnlinePlayers()){

                if(teamBufferList.size() >= teamSizes) {
                    playerTeams.add(teamBufferList);
                    teamBufferList.clear();

                }
                teamBufferList.add(player.getUniqueId());

            }

            if(teamBufferList.size() > 0){
                playerTeams.add(teamBufferList);
            }
        }

        for(List<UUID> teams : playerTeams){

            for(UUID playerUUID : teams){

                if(Bukkit.getPlayer(playerUUID) != null){
                    Player player = Bukkit.getPlayer(playerUUID);

                    player.sendMessage(StringUtil.TranslateColour("&aYour team consists of " + getPlayerTeamString(player) + "&a!"));
                }

            }

        }


    }

    public void resetPlayerTeams(){
        playerTeams.clear();
    }

    public List<UUID> getPlayerTeam(Player player){

        for(List<UUID> teams : playerTeams){
            if(teams.contains(player.getUniqueId())){
                return teams;
            }
        }

        return Arrays.asList(player.getUniqueId());
    }

    public String getPlayerTeamString(Player player){

        if(teamsActive){
            for(List<UUID> teams : playerTeams){
                if(teams.contains(player.getUniqueId())){

                    StringBuilder sb = new StringBuilder();

                    for(UUID uuid : teams){

                        if(Bukkit.getPlayer(uuid) == null){
                            OfflinePlayer teamPlayer = Bukkit.getOfflinePlayer(uuid);

                            sb.append(StringUtil.TranslateColour("&c" + teamPlayer.getName() + ", "));
                        } else {
                            Player teamPlayer = Bukkit.getPlayer(uuid);

                            if(instance.currentPlayers.contains(teamPlayer.getUniqueId())){
                                sb.append(StringUtil.TranslateColour("&a" + teamPlayer.getName() + ", "));

                            } else {
                                sb.append(StringUtil.TranslateColour("&c" + teamPlayer.getName() + ", "));

                            }


                        }
                    }

                    String teamString = sb.toString();
                    teamString = teamString.substring(0, teamString.length() - 2);

                    return teamString;
                }
            }
        }

        return player.getName();
    }

    public String getPlayerTeamString(OfflinePlayer player){

        if(teamsActive){
            for(List<UUID> teams : playerTeams){
                if(teams.contains(player.getUniqueId())){

                    StringBuilder sb = new StringBuilder();

                    for(UUID uuid : teams){

                        if(Bukkit.getPlayer(uuid) == null){
                            OfflinePlayer teamPlayer = Bukkit.getOfflinePlayer(uuid);

                            sb.append(StringUtil.TranslateColour("&c" + teamPlayer.getName() + ", "));
                        } else {
                            Player teamPlayer = Bukkit.getPlayer(uuid);

                            if(instance.currentPlayers.contains(teamPlayer.getUniqueId())){
                                sb.append(StringUtil.TranslateColour("&a" + teamPlayer.getName() + ", "));

                            } else {
                                sb.append(StringUtil.TranslateColour("&c" + teamPlayer.getName() + ", "));

                            }


                        }
                    }

                    String teamString = sb.toString();
                    teamString = teamString.substring(0, teamString.length() - 2);

                    return teamString;
                }
            }
        }

        return player.getName();
    }

    public String getPlayerTeamString(UUID playerUUID){

        Player player;

        if(Bukkit.getPlayer(playerUUID) == null){
            player = Bukkit.getOfflinePlayer(playerUUID).getPlayer();

        } else {
            player = Bukkit.getPlayer(playerUUID);

        }

        return getPlayerTeamString(player);
    }

    public ArrayList<List<UUID>> getPlayerTeams(){

        return playerTeams;
    }


}
