package com.mimo.Gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public abstract class BasicInventoryGui implements InventoryHolder {
    protected final Inventory inventory;
    protected final Player player;

    public BasicInventoryGui(Player player, String title) {
        this.player = player;

        ItemStack[] items = items();
        int inventorySize = (Math.max(items.length / 9, 1)) * 9; // must be multiple of 9
        inventory = Bukkit.createInventory(this, inventorySize, Component.text(title)); // TODO: if you set the owner to the player, does that mean the player drops the inventory on death?
        inventory.setContents(items);
    }

    public void show() {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    protected abstract ItemStack[] items();

    public abstract void clickCallback(InventoryClickEvent event);
}
