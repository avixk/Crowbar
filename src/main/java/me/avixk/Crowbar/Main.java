package me.avixk.Crowbar;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sun.awt.Win32GraphicsConfig;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    public static List<String> required_plugins = new ArrayList<>();
    public static boolean crowbarEngaged = false;
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        required_plugins = getConfig().getStringList("required_plugins");
        Bukkit.getPluginManager().registerEvents(this,this);
        if(getConfig().getBoolean("server_list_motd.change_when_crowbar"))
            Bukkit.getPluginManager().registerEvents(new ServerListListener(),this);
    }

    public List<String> checkCrowbar(){
        List<String> unloaded_plugins = new ArrayList<>();
        for(String plugin : required_plugins){
            if(!Bukkit.getPluginManager().isPluginEnabled(plugin))
                unloaded_plugins.add(plugin);
        }
        return unloaded_plugins;
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event){
        List<String> unloaded_plugins = checkCrowbar();
        if(unloaded_plugins.size() > 0){
            crowbarEngaged = true;
            for(OfflinePlayer player : Bukkit.getWhitelistedPlayers()){
                if(player.getUniqueId().equals(event.getUniqueId())){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.this, new Runnable() {
                        @Override
                        public void run() {
                            Player targ = Bukkit.getPlayer(event.getUniqueId());
                            if(targ != null){
                                String pluginFormat = getConfig().getString("crowbar_join_plugin_list_placeholder");
                                String pluginList = "";
                                for(String plugin : unloaded_plugins){
                                    pluginList += pluginFormat.replace("{plugin}",plugin);
                                }
                                String message = getConfig().getString("crowbar_join_message").replace("{plugins}",pluginList);
                                targ.sendMessage(message.replace("&","§").replace("\\n","\n"));
                            }
                        }
                    },60);
                    return;
                }
            }
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            String pluginFormat = getConfig().getString("plugin_list_placeholder");
            String pluginList = "";
            for(String plugin : unloaded_plugins){
                pluginList += pluginFormat.replace("{plugin}",plugin);
            }
            String message = getConfig().getString("kick_message").replace("{plugins}",pluginList);
            event.setKickMessage(message.replace("&","§").replace("\\n","\n"));
        }else{
            crowbarEngaged = false;
        }
    }
}
