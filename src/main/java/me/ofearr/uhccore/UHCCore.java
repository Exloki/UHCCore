package me.ofearr.uhccore;

import me.ofearr.uhccore.Commands.EndUHCCMD;
import me.ofearr.uhccore.Commands.StartUHCCMD;
import me.ofearr.uhccore.Enchants.CustomEnchantmentHandler;
import me.ofearr.uhccore.Enchants.SpiderEnchant;
import me.ofearr.uhccore.Handlers.*;
import me.ofearr.uhccore.Utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class UHCCore extends JavaPlugin {

    public DeathMatchManager deathMatchManager = new DeathMatchManager(this);
    public GraceTimer graceTimer = new GraceTimer(this);
    public UHCBoardManager uhcBoardManager = new UHCBoardManager(this);
    public ShrinkBorderManager shrinkBorderManager = new ShrinkBorderManager(this);

    public boolean gameActive = false;
    public boolean graceActive = false;

    private SpiderEnchant spiderEnchant;
    public static HashMap<Enchantment, String[]> registeredEnchants = new HashMap<>();

    private void registerEnchant(Enchantment enchant, String[] itemTypes){
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);

            field.set(null, true);
            Enchantment.registerEnchantment(enchant);
            registeredEnchants.put(Enchantment.getById(enchant.getId()), itemTypes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void unregisterEnchant(Enchantment enchant){
        try {
            Field IdField = Enchantment.class.getDeclaredField("byId");
            IdField.setAccessible(true);

            HashMap<Integer, Enchantment> byIDField = (HashMap<Integer, Enchantment>) IdField.get(null);
            if(byIDField.containsKey(enchant.getId())){
                byIDField.remove(enchant.getId());
                registeredEnchants.remove(Enchantment.getById(enchant.getId()));
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);

            HashMap<String, Enchantment> byNameField = (HashMap<String, Enchantment>) IdField.get(null);
            if(byNameField.containsKey(enchant.getName())){
                byNameField.remove(enchant.getName());
                registeredEnchants.remove(Enchantment.getByName(enchant.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    public ArrayList<UUID> currentPlayers = new ArrayList<>();
    public ArrayList<UUID> deadPlayers = new ArrayList<>();

    String[] spiderEnchantItemArray = new String[] {"boots"};

    @Override
    public void onEnable() {
        //Set var vals and load config
        loadConfig();
        gameActive = false;

        setRegisteredCommands();
        //setRegisteredEnchants();
        passPluginInstance();
        setRegisteredEvents();

    }

    private void setRegisteredCommands(){
        getCommand("start-uhc").setExecutor(new StartUHCCMD(this));
        getCommand("end-uhc").setExecutor(new EndUHCCMD(this));
        //getCommand("giveenchant").setExecutor(new GiveEnchantCMD());
    }

    private void setRegisteredEnchants(){
        Bukkit.getPluginManager().registerEvents(new CustomEnchantmentHandler(), this);
        registerEnchant(spiderEnchant = new SpiderEnchant(100), spiderEnchantItemArray);
    }

    private void setRegisteredEvents(){
        //Bukkit.getPluginManager().registerEvents(new SpiderEnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new UHCEventHandler(this), this);
    }

    private void passPluginInstance(){
        new RandomTeleport(this);
    }



    @Override
    public void onDisable() {
        //Disable enchants
      //  unregisterEnchant(spiderEnchant);
    }

    public void loadConfig(){
        saveDefaultConfig();
    }

    public void endGame(){
        currentPlayers.clear();
        deadPlayers.clear();
        gameActive = false;
        graceActive = false;
        deathMatchManager.deathMatchActive = false;
        deathMatchManager.winners.clear();
        deathMatchManager.playerPlacedBlocks.clear();
        UHCEventHandler.playerKillCount.clear();

        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(StringUtil.TranslateColour("&8[&d&lUHC&8] >> &cThe game has been ended!"));
        }

    }

}
