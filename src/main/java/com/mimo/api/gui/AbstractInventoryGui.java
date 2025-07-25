package com.mimo.api.gui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractInventoryGui implements InventoryHolder {
    protected final Player player;
    @Getter
    protected Inventory inventory;
    protected Component title;
    protected final int rows;

    public AbstractInventoryGui(Player player, Component title) {
        this(player, title, 6);
    }

    /**
     * @param player - the player sees this inventory
     * @param title  - the title of the inventory is displayed at the left top corner
     * @param rows   - rows in an inventory are cool tho
     */
    public AbstractInventoryGui(Player player, Component title, int rows) {
        this.player = player;
        this.title = title;
        this.rows = Math.min(6, Math.max(1, rows));
        inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public static int correctInvSize(int size) {
        return ((size + 8) / 9) * 9;
    }

    protected void addItem(int x, int y, Material material) {
        addItem(x, y, new ItemStack(material));
    }

    protected void addItem(int x, int y, ItemStack itemStack) {
        if (x < 0 || x > 8) {
            throw new IndexOutOfBoundsException(
                    "Invalid x-coordinate: " + x + ", must be between 0 and 8");
        }
        if (y < 0 || y >= rows) {
            throw new IndexOutOfBoundsException(
                    "Invalid y-coordinate: " + y + ", must be between 0 and " + (rows - 1));
        }

        int slot = y * 9 + x;
        inventory.setItem(slot, itemStack);
    }

    public void show() {
        items();
        player.openInventory(inventory);
    }

    public boolean isItemStackClicked(Material material, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        return clickedItem != null && clickedItem.getType() == material;
    }

    public boolean isItemStackClicked(ItemStack itemStack, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        return clickedItem != null && clickedItem.equals(itemStack);
    }

    protected abstract ItemStack[] items();

    protected abstract void clickCallback(InventoryClickEvent event);

    protected abstract void inventoryCloseCallback(InventoryCloseEvent event);

    protected void playClickSound(InventoryClickEvent event, Material clickedMaterial) {
        if (clickedMaterial == Material.BARRIER) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f); //TODO: if you play the sound at the Players location, will it be played for everyone else near the player?
        } else {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
    }
}