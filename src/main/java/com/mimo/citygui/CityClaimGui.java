package com.mimo.citygui;

import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CityClaimGui extends AbstractInventoryGui {
    // TODO: WIP

    public CityClaimGui(Player player) {
        super(player, Component.text("Claim Gui"));
        // Don't call show() in constructor
    }

    @Override
    protected ItemStack[] items() {
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {

    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
