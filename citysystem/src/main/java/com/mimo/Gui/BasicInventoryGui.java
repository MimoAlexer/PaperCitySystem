package com.mimo.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        int rows = Math.max((int) Math.ceil(items.length / 9.0), 1);
        inventory = Bukkit.createInventory(this, rows * 9, Component.text(title));
        inventory.setContents(items);
    }

    protected void addItem(int x, int y, Material material) {
        int slot = y * 9 + x;
        if (x < 0 || x > 8 || slot < 0 || slot >= inventory.getSize()) {
            throw new IndexOutOfBoundsException(
                    "Invalid inventory coordinates: (" + x + "," + y + ")");
        }
        inventory.setItem(slot, new ItemStack(material));
    }

    public boolean isItemStackClicked(@NotNull InventoryClickEvent event, @NotNull Material material) {
        if(event.getCurrentItem() == null) return false;
        return event.getCurrentItem().equals(new ItemStack(material));
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
