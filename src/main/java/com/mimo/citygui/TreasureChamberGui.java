package com.mimo.citygui;

import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TreasureChamberGui extends AbstractInventoryGui {
    // TODO
    public TreasureChamberGui(Player player, Component title) {
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        return new ItemStack[0];
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        // check if the member is still a member else cancel event and close Inventory
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
