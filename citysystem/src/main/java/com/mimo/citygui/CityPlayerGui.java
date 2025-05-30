package com.mimo.citygui;

import com.mimo.City;
import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class CityPlayerGui extends AbstractInventoryGui {
    public CityPlayerGui(Player player) {
        super(player, Component.text("Players of " + City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() { // TODO: Implement new PlayerHeads System
        addItem(8, 3, new ItemStack(Material.BARRIER).getType());
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
            addItem(col, 6, new ItemStack(Material.GRAY_STAINED_GLASS_PANE).getType());
        }
        List<Player> members = City.getCityByPlayer(player).getPlayers();
        for (int i = 0; i < members.size(); i++) {
            Player member = members.get(i);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(member);
                meta.displayName(Component.text(member.getName()));
                head.setItemMeta(meta);
            }
            int col = i % 10;
            int row = 3 + (i / 10);
            addItem(col, row, head);
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
