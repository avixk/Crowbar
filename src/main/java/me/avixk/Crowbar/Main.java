package me.avixk.Crowbar;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Crowbar.instance = new Crowbar();
        Crowbar.instance.setRequiredPlugins(getConfig().getStringList("required_plugins"));
        Bukkit.getPluginManager().registerEvents(new JoinListener(),this);
        if(getConfig().getBoolean("server_list_motd.change_when_crowbar"))
            Bukkit.getPluginManager().registerEvents(new ServerListListener(),this);
    }
}
