package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.PlayerHeads;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class CityPlayerGui extends AbstractInventoryGui {
    public HashMap<ItemStack, Player> itemStackPlayerHashMap = new HashMap<>();

    public CityPlayerGui(Player player) {
        super(player, Component.text("Players of " + City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() {
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
            ItemStack head = PlayerHeads.fromUuid(member.getUniqueId());
            itemStackPlayerHashMap.put(head, member);
            addItem(col, row, head);
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f); //TODO: if you play the sound at the players location does it player for other Players too?
            new CityMainGui(player).show();
            return;
        }
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            new CityPlayerInfoGui(
                player,
                itemStackPlayerHashMap.get(event.getCurrentItem())
            ).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
