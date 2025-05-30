package com.mimo.shared.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class InventoryGuiManager implements Listener {

    public InventoryGuiManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the top inventory (our GUI)
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.getHolder() instanceof AbstractInventoryGui abstractInventoryGui) {
            // Cancel clicks in player inventory to prevent item movement
            if (event.getClickedInventory() != topInventory) {
                event.setCancelled(true);
                return;
            }
            abstractInventoryGui.clickCallback(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof AbstractInventoryGui abstractInventoryGui) {
            abstractInventoryGui.inventoryCloseCallback(event);
        }
    }
}
