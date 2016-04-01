package com.c45y.tranceping;

import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TrancePing extends JavaPlugin {
    private final int max_ping_length = 59;
    
    private String pingResponse = null;
    
    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.getConfig().addDefault("ping.network", "&fNerdNu");
            this.getConfig().addDefault("ping.server", "&f?");
            this.getConfig().addDefault("ping.message", "&6Unconfigured and still awesome!");
            
            this.saveConfig();
            this.reloadConfig();
        }

        this.getServer().getPluginManager().registerEvents(new TranceListener(this), this);
    }

    @Override
    public void onDisable() {
    }
    
    public String getConfigSetting(String param) {
        return getConfig().getString("ping." + param);
    }
    
    public String getPingResponse() {
        if (pingResponse == null) {
            pingResponse = getPingResponse(getConfigSetting("message"));
        }
        return pingResponse;
    }
    
    public String getPingResponse(String message) {
        return ChatColor.translateAlternateColorCodes('&', 
            getConfigSetting("network") + " " +
            getConfigSetting("server") + "\n" +
            message
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("tranceping")) {
            return false;
        }
        
        if (!sender.hasPermission("tranceping.staff")) {
            sender.sendMessage(ChatColor.RED + "You cannot use that. Please gain the " + ChatColor.GOLD + "tranceping.staff" + ChatColor.RED + " permission");
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "/tranceping <message> - Sets the server description in the multiplayer menu.");
            return true;
        }
        
        StringBuilder message = new StringBuilder();
        for (String arg: args) {
            message.append(arg);
            message.append(" ");
        }
        
        String serverMessage = message.toString().trim();
        
        if (getPingResponse(serverMessage).length() > max_ping_length) {
            int maxlen = max_ping_length - getPingResponse("").length(); // Get the length of all the other fluff besides our message
            sender.sendMessage(ChatColor.RED + "You cannot set a message longer than " + ChatColor.GOLD + String.valueOf(maxlen) + ChatColor.RED + " characters");
            return true;
        }
        
        
        getConfig().set("ping.message", serverMessage);
        saveConfig();
        reloadConfig();
        
        pingResponse = null;
        
        sender.sendMessage(ChatColor.GREEN + "Changed server ping message!");
        
        return true;
    }
}
