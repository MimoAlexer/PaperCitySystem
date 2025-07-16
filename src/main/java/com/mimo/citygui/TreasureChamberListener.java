package com.mimo.citygui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import java.util.HashSet;
import java.util.Set;

public class TreasureChamberListener implements Listener {
    private static final Set<Player> viewOnlyPlayers = new HashSet<>();
    private static final Set<Inventory> trackedInventories = new HashSet<>();

    public static void registerViewOnly(Player player, Inventory inventory) {
        viewOnlyPlayers.add(player);
        trackedInventories.add(inventory);
        // Register this listener if not already
        Bukkit.getPluginManager().registerEvents(new TreasureChamberListener(), Bukkit.getPluginManager().getPlugins()[0]);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (viewOnlyPlayers.contains(player) && trackedInventories.contains(event.getInventory())) {
                event.setCancelled(true);
            }
        }
    }
} 