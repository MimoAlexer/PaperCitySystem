package com.mimo.wargui;

import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class AlliesGui extends AbstractInventoryGui {
    // TODO: WIP
    public AlliesGui(Player player, Component title) {
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        return new ItemStack[0];
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {

    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
