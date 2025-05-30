package com.mimo.citygui;

import com.mimo.City;
import com.mimo.shared.PlayerHeads;
import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CityPlayerGui extends AbstractInventoryGui {
    public CityPlayerGui(Player player) {
        super(player, Component.text("Players of " + City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() { // TODO: Implement new PlayerHeads System
        for (int col = 0; col < 9; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
            addItem(col, 5, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
        }
        addItem(8, 5, new ItemStack(Material.BARRIER).getType());
        List<Player> members = City.getCityByPlayer(player).getPlayers();
        for (int i = 0; i < members.size(); i++) {
            Player member = members.get(i);
            int x = i + 1;
            int col = x % 10;
            int row = 2 + (i / 9);
            addItem(col, row, PlayerHeads.fromUuid(member.getUniqueId()));
        }
        return new ItemStack[0];
    }


    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) new CityMainGui(player).show();
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
