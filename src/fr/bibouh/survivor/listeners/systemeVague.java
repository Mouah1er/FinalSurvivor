package fr.bibouh.survivor.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class systemeVague implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            if(player.equals(2)) {

                player.sendMessage("test");

            }

        }

    }

}

