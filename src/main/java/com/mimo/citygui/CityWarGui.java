package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CityWarGui extends AbstractInventoryGui {
    // TODO: WIP
    public CityWarGui(Player player) {
        super(player, Component.text("Wars of " + City.getCityByPlayer(player).getName() + "!"));
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
