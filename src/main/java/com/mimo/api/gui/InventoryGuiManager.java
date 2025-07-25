package com.mimo.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class InventoryGuiManager implements Listener {
    public InventoryGuiManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder == null)
            return;

        if (inventoryHolder instanceof AbstractInventoryGui abstractInventoryGui)
            abstractInventoryGui.clickCallback(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof AbstractInventoryGui abstractInventoryGui) {
            abstractInventoryGui.inventoryCloseCallback(event);
            if (event.getPlayer() instanceof Player p && event.getInventory().getHolder() == this) {
                p.playSound(p.getLocation(), org.bukkit.Sound.UI_TOAST_OUT, 1.0f, 1.0f);
            }
        }
    }
}
