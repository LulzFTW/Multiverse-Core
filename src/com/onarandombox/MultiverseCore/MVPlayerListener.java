package com.onarandombox.MultiverseCore;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MVPlayerListener extends PlayerListener {
    private final Logger log = Logger.getLogger("Minecraft");
    MultiverseCore plugin;
    
    public MVPlayerListener(MultiverseCore plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // MultiVerseCore.debugMsg(event.getPlayer().getName() + " just tried to Teleport");
        // event.setCancelled(true);
        // Entity entity = event.getPlayer().;
        // MultiVerseCore.log.info("1 - " + event.getTo().toString());
        // MultiVerseCore.log.info("2 - " + event.getPlayer().getLocation().toString());
        MVPlayerSession ps = this.plugin.getPlayerSession(event.getPlayer());
        ps.setRespawnWorld(event.getTo().getWorld());
        
        log.warning("To: " + event.getTo().getWorld().getName());
        log.warning("From: " + event.getFrom().getWorld().getName());
    }
    
    public void onPlayerKick(PlayerKickEvent event) {
        event.setCancelled(true);
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer(); // Grab Player
        Location loc = p.getLocation(); // Grab Location
        /**
         * Check the Player has actually moved a block to prevent unneeded calculations... This is to prevent huge performance drops on high player count servers.
         */
        MVPlayerSession ps = this.plugin.getPlayerSession(p);
        if (ps.loc.getBlockX() == loc.getBlockX() && ps.loc.getBlockY() == loc.getBlockY() && ps.loc.getBlockZ() == loc.getBlockZ()) {
            return;
        } else {
            ps.loc = loc; // Update the Players Session to the new Location.
        }
    }
    
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        // Not sure if this should be a seperat plugin... in here for now!!!
        // FernFerret
        
        if (event.isCancelled()) {
            return;
        }
        /**
         * Check whether the Server is set to prefix the chat with the World name. If not we do nothing, if so we need to check if the World has an Alias.
         */
        if (this.plugin.configMV.getBoolean("worldnameprefix", true)) {

            String world = event.getPlayer().getWorld().getName();

            String prefix = "";
            
            // If we're not a MV world, don't do anything
            if (!this.plugin.isMVWorld(world)) {
                return;
            }
            MVWorld mvworld = this.plugin.getMVWorld(world);
            prefix = mvworld.getColoredWorldString();

            String format = event.getFormat();

            event.setFormat("[" + prefix + "]" + format);
        }
    }
    
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // TODO: Handle Global Respawn from config
        
        // TODO: Handle Alternate Respawn from config
        
        MVPlayerSession ps = this.plugin.getPlayerSession(event.getPlayer());
        event.setRespawnLocation(ps.getRespawnWorld().getSpawnLocation());
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        
    }
    
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        
    }
}
