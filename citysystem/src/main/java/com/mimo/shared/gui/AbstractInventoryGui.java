package com.mimo.shared.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractInventoryGui implements InventoryHolder {
    protected final Player player;
    protected Inventory inventory;
    protected Component title;

    public AbstractInventoryGui(Player player, Component title) {
        this.player = player;
        this.title = title;
    }

    public static int correctInvSize(int size) {
        return ((size + 8) / 9) * 9;
    }

    protected void addItem(int x, int y, Material material) {
        addItem(x, y, new ItemStack(material));
    }

    protected void addItem(int x, int y, ItemStack itemStack) {
        int slot = y * 9 + x;
        if (x < 0 || x > 8 || slot < 0 || slot >= inventory.getSize()) {
            throw new IndexOutOfBoundsException(
                    "Invalid inventory coordinates: (" + x + "," + y + ")");
        }
        inventory.setItem(slot, itemStack);
    }

    public void show() {
        ItemStack[] items = items();
        int inventorySize = correctInvSize(items.length); // must be multiple of 9
        inventory = Bukkit.createInventory(this, inventorySize, title);
        inventory.setContents(items);
        player.openInventory(inventory);
    }

    public boolean isItemStackClicked(Material material, InventoryClickEvent event) {
        return Objects.equals(Objects.requireNonNull(event.getCurrentItem()).getType(), material);
    }

    public boolean isItemStackClicked(ItemStack itemStack, InventoryClickEvent event) {
        return Objects.equals(event.getCurrentItem(), itemStack);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    protected abstract ItemStack[] items();

    protected abstract void clickCallback(InventoryClickEvent event);

    protected abstract void inventoryCloseCallback(InventoryCloseEvent event);
}
