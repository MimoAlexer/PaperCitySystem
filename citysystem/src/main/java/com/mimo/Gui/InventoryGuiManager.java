package com.mimo.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.mimo.CitySystem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InventoryGuiManager implements Listener {

    public InventoryGuiManager() {
        Bukkit.getPluginManager().registerEvents(this, CitySystem.INSTANCE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null)
            return;

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder == null)
            return;

        if (inventoryHolder instanceof BasicInventoryGui basicInventoryGui) {
            basicInventoryGui.clickCallback(event);
        } else {
            event.setCancelled(true);
        }
    }
}
