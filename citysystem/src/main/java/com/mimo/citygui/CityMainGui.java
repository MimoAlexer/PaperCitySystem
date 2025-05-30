package com.mimo.citygui;

import com.mimo.City;
import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class CityMainGui extends AbstractInventoryGui {
    public CityMainGui(Player player) {
        super(player, Component.text(City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() {
        addItem(8, 3, Material.BARRIER); // TODO: Makes a NullPointerException fix this
        addItem(2, 2, Material.PLAYER_HEAD);
        for (int i = 0; i < 8; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(i, 6, Material.GRAY_STAINED_GLASS_PANE);
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) event.getWhoClicked().closeInventory();
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) new CityPlayerGui(player).show();
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
