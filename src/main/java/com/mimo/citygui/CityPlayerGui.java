package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.PlayerHeads;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
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
        City city = City.getCityByPlayer(player);
        boolean isOwner = city.getOwner().equals(player);
        for (int i = 0; i < members.size(); i++) {
            Player member = members.get(i);
            int x = i + 1;
            int col = x % 10;
            int row = 2 + (i / 9);
            ItemStack head = PlayerHeads.fromUuid(member.getUniqueId());
            itemStackPlayerHashMap.put(head, member);
            addItem(col, row, head);
            if (isOwner && !member.equals(player)) {
                ItemStack kick = new ItemStack(Material.BARRIER);
                var meta = kick.getItemMeta();
                meta.displayName(Component.text("Kick", net.kyori.adventure.text.format.NamedTextColor.RED));
                kick.setItemMeta(meta);
                addItem(col + 1, row, kick);
                itemStackPlayerHashMap.put(kick, member);
            }
        }
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) new CityMainGui(player).show();
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) {
            new CityPlayerInfoGui(
                    player,
                    itemStackPlayerHashMap.get(event.getCurrentItem())
            ).show();
        }
        if (isItemStackClicked(Material.BARRIER, event)) {
            Player toKick = itemStackPlayerHashMap.get(event.getCurrentItem());
            City city = City.getCityByPlayer(player);
            if (city != null && city.getOwner().equals(player) && toKick != null && !toKick.equals(player)) {
                new com.mimo.api.gui.GenericConfirmationGui(player, Component.text("Kick " + toKick.getName() + " from the city?", net.kyori.adventure.text.format.NamedTextColor.RED)) {
                    @Override
                    public void onConfirm(org.bukkit.event.inventory.InventoryClickEvent e) {
                        city.removePlayer(toKick);
                        player.sendMessage(Component.text("Kicked " + toKick.getName() + " from the city!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
                        toKick.sendMessage(Component.text("You have been kicked from " + city.getName() + "!", net.kyori.adventure.text.format.NamedTextColor.RED));
                        new CityPlayerInfoGui(player, toKick).show();
                    }
                    @Override
                    public void onCancel(org.bukkit.event.inventory.InventoryClickEvent e) {
                        new CityPlayerGui(player).show();
                    }
                };
            }
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
