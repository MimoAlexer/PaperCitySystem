package com.mimo.citygui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.kyori.adventure.text.Component;

public class TreasureChamber{
    @SuppressWarnings("unused")
    private static Player staticPlayer; //maybe i can be usedin sometime
    private static final int SIZE = 6 * 9; // 6 rows, 9 columns
    private static final Component TITLE = Component.text("Treasure Chamber");
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