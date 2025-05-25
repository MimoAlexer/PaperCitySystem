package com.mimo.CityGui;

import com.mimo.City;
import com.mimo.Gui.BasicInventoryGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainGui extends BasicInventoryGui {
    public MainGui(Player player) {
        String title = City.getCitybyPlayer(player).getName();
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        addItem(3, 9, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        switch (event.getCurrentItem()){
            case ItemStack itemStack when isItemStackClicked(event, Material.BARRIER) -> {
                event.getWhoClicked().closeInventory();
            }
            case null -> {}
            default -> {
                return;
            }
        }
    }
}
