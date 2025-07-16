package com.mimo.citygui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mimo.City;

import java.util.HashMap;
import java.util.Map;

public class TreasureChamber{
    private static Player staticPlayer;
    private static final int SIZE = 6 * 9; // 6 rows, 9 columns
    private static final String TITLE = "Treasure Chamber of " + City.getCityByPlayer(staticPlayer).getName();
    private final Inventory inventory;

    public TreasureChamber() {
        this.inventory = Bukkit.createInventory(null, SIZE, TITLE);
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Call this when a player opens the chamber
    public void show(Player player, boolean canEdit) {
        staticPlayer = player;
        player.openInventory(inventory);
        if (!canEdit) {
            // Register a temporary event handler to prevent item removal
            TreasureChamberListener.registerViewOnly(player, inventory);
        }
    }
} 