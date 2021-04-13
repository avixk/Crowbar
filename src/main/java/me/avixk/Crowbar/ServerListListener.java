package me.avixk.Crowbar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListListener implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent e){
        if(Main.crowbarEngaged){
            e.setMaxPlayers(Main.plugin.getConfig().getInt("server_list_motd.maxplayers"));
            e.setMotd(Main.plugin.getConfig().getString("server_list_motd.message").replace("&","§").replace("\\n","\n"));
        }
    }
}
