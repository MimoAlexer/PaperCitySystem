package com.mimo.CityGui;

import com.mimo.Gui.BasicInventoryGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CityPlayerGui extends BasicInventoryGui {
    public CityPlayerGui(Player player, String title) {
        super(player, title);
    }

    @Override
    protected ItemStack[] items() {
        addItem(9, 3, Material.BARRIER);
        for(int i = 0; i < 8; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(i, 6, Material.GRAY_STAINED_GLASS_PANE);
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        switch (event.getCurrentItem()){
            case ItemStack _ when isItemStackClicked(event, Material.BARRIER) -> {
                MainGui mainGui = new MainGui(player);
                mainGui.show();
            }
            case null -> {}
            default -> {
                event.setCancelled(true);
                return;
            }
        }
    }
}
