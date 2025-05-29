package com.mimo.citygui;

import com.mimo.gui.BasicInventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CityClaimGui extends BasicInventoryGui {

    public CityClaimGui(Player player) {
        super(player, "City Claim");
    }

    @Override
    protected ItemStack[] items() {
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {

    }
}
