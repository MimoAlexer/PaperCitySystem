package com.mimo.citygui;

import com.mimo.City;
import com.mimo.gui.BasicInventoryGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CityMainGui extends BasicInventoryGui {
    public CityMainGui(Player player) {
        super(player, City.getCityByPlayer(player).getName());
    }

    @Override
    protected ItemStack[] items() {
        addItem(9, 3, Material.BARRIER, null);
        addItem(2, 2, Material.PLAYER_HEAD, null);
        for (int i = 0; i < 8; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE, null);
            addItem(i, 6, Material.GRAY_STAINED_GLASS_PANE, null);
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        switch (event.getCurrentItem()) {
            case ItemStack _ when isItemStackClicked(event, Material.BARRIER) -> {
                event.getWhoClicked().closeInventory();
            }
            case ItemStack _ when isItemStackClicked(event, Material.PLAYER_HEAD) -> {
                CityPlayerGui cityPlayerGui = new CityPlayerGui(player);
                cityPlayerGui.show();
            }
            case null -> {
            }
            default -> {
                return;
            }
        }
    }
}
