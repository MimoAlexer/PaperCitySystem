package com.mimo.shared.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class GenericConfirmationGui extends AbstractInventoryGui {

    public static final ItemStack CONFIRM_ITEM = new ItemStack(Material.GREEN_WOOL);
    public static final ItemStack CANCEL_ITEM = new ItemStack(Material.RED_WOOL);

    private final BiFunction<Player, Boolean, Void> function;

    public GenericConfirmationGui(Player player, Component title, BiFunction<Player, Boolean, Void> function) {
        super(player, title);
        this.function = function;
    }

    @Override
    protected ItemStack[] items() {
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
        if (isConfirm || isCancel) {
            function.apply(player, isConfirm);
            player.closeInventory();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
