package me.ofearr.uhccore;

import me.ofearr.uhccore.Enchants.CustomEnchantmentHandler;
import me.ofearr.uhccore.Enchants.RomanNumeralUtil;
import me.ofearr.uhccore.Enchants.SpiderEnchant;
import me.ofearr.uhccore.Enchants.SpiderEnchantListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static boolean gameActive = false;
    public static boolean graceActive = false;
    public static Main plugin;

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

    public static String TranslateColour(String text){

        String converted = ChatColor.translateAlternateColorCodes('&', text);

        return converted;
    }

    public static ArrayList<UUID> currentPlayers = new ArrayList<>();
    public static ArrayList<UUID> deadPlayers = new ArrayList<>();

    String[] spiderEnchantItemArray = new String[] {"boots"};

    @Override
    public void onEnable() {
        //Set var vals and load config
        gameActive = false;
        plugin = this;
        loadConfig();

        //Register Enchants
        Bukkit.getPluginManager().registerEvents(new CustomEnchantmentHandler(), this);

        registerEnchant(spiderEnchant = new SpiderEnchant(100), spiderEnchantItemArray);
        Bukkit.getPluginManager().registerEvents(new SpiderEnchantListener(), this);

    }

    @Override
    public void onDisable() {
        //Disable enchants
        unregisterEnchant(spiderEnchant);
    }

    public void loadConfig(){
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("start-uhc")){
            if(!player.hasPermission("uhc.start")){
                player.sendMessage(TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
            } else{
                if(gameActive == true){
                    player.sendMessage(TranslateColour("&8[&d&lUHC&8] >> &cThe game has already started! Use /end-uhc to stop the current game."));
                } else{
                    new BukkitRunnable(){

                        int gameStartTimer = 10;
                        @Override
                        public void run() {
                            if(gameStartTimer == 10){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 10 seconds!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 10 seconds!"));
                            } else if(gameStartTimer == 5){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 5 seconds!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 5 seconds!"));
                            } else if(gameStartTimer == 4){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 4 seconds!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 4 seconds!"));
                            } else if(gameStartTimer == 3){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 3 seconds!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 3 seconds!"));
                            } else if(gameStartTimer == 2){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 2 seconds!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 2 seconds!"));
                            } else if(gameStartTimer == 1){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game will start in 1 second!"));
                                }
                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game will start in 1 second!"));
                            } else if(gameStartTimer <= 0){
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(TranslateColour("&a&lGame Starting!"), TranslateColour("&a&lThe game has started!"));
                                    currentPlayers.add(p.getUniqueId());
                                    RandomTeleport.randomTeleportPlayer(p);

                                    UHCBoardManager.setScoreboard(p);
                                }

                                Bukkit.getWorld(getConfig().getString("Settings.over-world-name")).getWorldBorder().setSize(getConfig().getDouble("Settings.default-border-size"));
                                Bukkit.getWorld(getConfig().getString("Settings.nether-world-name")).getWorldBorder().setSize(getConfig().getDouble("Settings.default-border-size"));
                                Bukkit.getWorld(getConfig().getString("Settings.end-world-name")).getWorldBorder().setSize(getConfig().getDouble("Settings.default-border-size"));

                                Bukkit.broadcastMessage(TranslateColour("&8[&d&lUHC&8] >> &aThe game has started! You will now be teleported to your spawn points..."));

                                GraceTimer.graceRunnable();
                                this.cancel();
                                gameActive = true;
                                graceActive = true;
                            }
                            gameStartTimer--;
                        }
                    }.runTaskTimerAsynchronously(this, 0L, 20L);

                }
            }
        }
        if(command.getName().equalsIgnoreCase("giveenchant")){
            if(!player.hasPermission("uhc.enchants")){
                player.sendMessage(TranslateColour("&8[&d&lUHC&8] >> &cInsufficient Permissions!"));
            } else{
                if(args.length < 2){
                    player.sendMessage(TranslateColour("&8[&d&lUHC&8] &8[&eEnchants&8] >> &cInsufficient Arguments! /giveenchant"));
                } else{
                    Player target = Bukkit.getPlayer(args[0]);
                    String enchantName = args[1].substring(0, 1).toUpperCase() + args[1].substring(1);
                    Enchantment enchant = Enchantment.getByName(enchantName);
                    int enchantLevel = Integer.valueOf(args[2]);

                    ItemStack enchantBook = new ItemStack(Material.BOOK_AND_QUILL);
                    ItemMeta enchantMeta = enchantBook.getItemMeta();

                    enchantMeta.setDisplayName(TranslateColour("&d&kH&cEnchanted Book&d&kH"));
                    List<String> enchantLore = new ArrayList<>();

                    enchantLore.add(TranslateColour(" "));
                    enchantLore.add(TranslateColour("&aEnchant: &c" + enchantName + " " + RomanNumeralUtil.integerToNumeral(enchantLevel)));
                    enchantLore.add(TranslateColour("&aApplicable to: &e" + enchant.getItemTarget().name()));
                    enchantLore.add(" ");
                    enchantLore.add(TranslateColour("&7Drag and drop this enchant"));
                    enchantLore.add(TranslateColour("&7enchant onto an item in your"));
                    enchantLore.add(TranslateColour("&7inventory to apply."));

                    enchantMeta.addEnchant(enchant, enchantLevel, false);
                    enchantBook.setItemMeta(enchantMeta);

                    target.getInventory().addItem(enchantBook);
                    target.sendMessage(TranslateColour("&8[&d&lUHC&8] &8[&eEnchants&8] &8>> &aYou've been given " + enchantName + " at level " + enchantLevel + "!"));


                }
            }
        }
        return false;
    }
}
