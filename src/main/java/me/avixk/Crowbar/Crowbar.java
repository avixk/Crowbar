package me.avixk.Crowbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Crowbar {
    public static Crowbar instance;
    private List<String> required_plugins = new ArrayList<>();
    private boolean crowbarEngaged = false;

    public boolean isCrowbarEngaged(){
        return crowbarEngaged;
    }

    public List<String> getRequiredPlugins() {
        return required_plugins;
    }

    public void setRequiredPlugins(List<String> required_plugins) {
        this.required_plugins = required_plugins;
    }

    public void addRequiredPlugin(String required_plugin) {
        this.required_plugins.add(required_plugin);
    }

    public void setCrowbarEngaged(boolean crowbarEngaged) {
        if(crowbarEngaged)
            engageCrowbar();
        else
            disengageCrowbar();
    }

    public String getKickMessage() {
        List<String> unloaded_plugins = checkCrowbar();
        if(unloaded_plugins.size() == 0 || !Main.plugin.getConfig().getBoolean("enabled")) return null;
        String pluginFormat = Main.plugin.getConfig().getString("kick_message_plugins_placeholder");
        String pluginList = "";
        for(String plugin : unloaded_plugins){
            pluginList += pluginFormat.replace("{plugin}",plugin);
        }
        String message = Main.plugin.getConfig().getString("kick_message_format")
                .replace("{plugins}",pluginList)
                .replace("&","§")
                .replace("\\n","\n");
        return message;
    }

    public String getWhitelistMessage() {
        List<String> unloaded_plugins = checkCrowbar();
        if(unloaded_plugins.size() == 0 || !Main.plugin.getConfig().getBoolean("enabled")) return null;
        String pluginFormat = Main.plugin.getConfig().getString("whitelisted_message_plugins_placeholder");
        String pluginList = "";
        for(String plugin : unloaded_plugins){
            pluginList += pluginFormat.replace("{plugin}",plugin);
        }
        String message = Main.plugin.getConfig().getString("whitelisted_message_format")
                .replace("{plugins}",pluginList)
                .replace("&","§")
                .replace("\\n","\n");
        engageCrowbar();
        return message;
    }

    public List<String> checkCrowbar(){
        List<String> unloaded_plugins = new ArrayList<>();
        for(String plugin : required_plugins){
            if(!Bukkit.getPluginManager().isPluginEnabled(plugin))
                unloaded_plugins.add(plugin);
        }
        return unloaded_plugins;
    }

    public void engageCrowbar(){
        crowbarEngaged = true;
        String kick_message = getKickMessage();
        if(Main.plugin.getConfig().getBoolean("kick_all_during_crowbar")){
            for(Player player : Bukkit.getOnlinePlayers()){
                if(!player.hasPermission("crowbar.admin"))
                    player.kickPlayer(kick_message);
            }
        }
    }

    public void disengageCrowbar(){
        crowbarEngaged = false;
    }
}
