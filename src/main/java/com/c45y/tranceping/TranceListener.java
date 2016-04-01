package com.c45y.tranceping;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 *
 * @author c45y
 */
public class TranceListener implements Listener {
    private TrancePing _plugin;
    
    public TranceListener(TrancePing plugin) {
        _plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerListPing(ServerListPingEvent event) {      
        event.setMotd(_plugin.getPingResponse());
    }
}
