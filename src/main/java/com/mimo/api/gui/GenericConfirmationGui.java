package com.mimo.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public abstract class GenericConfirmationGui extends AbstractInventoryGui {

    public static final ItemStack CONFIRM_ITEM = new ItemStack(Material.GREEN_WOOL);
    public static final ItemStack CANCEL_ITEM = new ItemStack(Material.RED_WOOL);

    public GenericConfirmationGui(Player player, Component title) {
        super(player, title);
        show();
    }

    @Override
    protected ItemStack[] items() {
        for (int i = 0; i < 9; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(i, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        inventory.setItem(0, CONFIRM_ITEM);
        inventory.setItem(1, CONFIRM_ITEM);
        inventory.setItem(2, CONFIRM_ITEM);
        inventory.setItem(3, CONFIRM_ITEM);
        inventory.setItem(5, CANCEL_ITEM);
        inventory.setItem(6, CANCEL_ITEM);
        inventory.setItem(7, CANCEL_ITEM);
        inventory.setItem(8, CANCEL_ITEM);
        return new ItemStack[0];
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }

        boolean isConfirm = itemStack.equals(CONFIRM_ITEM);
        boolean isCancel = itemStack.equals(CANCEL_ITEM);
        if (isConfirm) {
            onConfirm(event);
            inventory.close();
        } else if (isCancel) {
            onCancel(event);
            inventory.close();
        }
    }

    public abstract void onConfirm(InventoryClickEvent event);

    public abstract void onCancel(InventoryClickEvent event);

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
