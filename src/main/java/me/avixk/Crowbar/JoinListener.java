package me.avixk.Crowbar;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPreJoin(PlayerLoginEvent event){
        String whitelistMessage = Crowbar.instance.getWhitelistMessage();
        if(whitelistMessage != null){
            Crowbar.instance.engageCrowbar();

            if(event.getPlayer().hasPermission("crowbar.admin")){
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                    @Override
                    public void run() {
                        event.getPlayer().sendMessage(whitelistMessage);
                    }
                },10);
            }else {
                if(Main.plugin.getConfig().getBoolean("whitelist_during_crowbar")){
                    event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                    event.setKickMessage(whitelistMessage);
                }
            }
        }else{
            Crowbar.instance.disengageCrowbar();
        }
    }
}
