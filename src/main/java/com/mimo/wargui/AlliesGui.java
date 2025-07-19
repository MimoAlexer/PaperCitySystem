package com.mimo.wargui;

import com.mimo.City;
import com.mimo.War;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class AlliesGui extends AbstractInventoryGui {
    private final War war;
    private final HashSet<City> pendingInvites;

    public AlliesGui(Player player, Component title) {
        super(player, title);
        // Find the first war for this city (could be improved for multiple wars)
        City city = City.getCityByPlayer(player);
        this.war = city.getWars().isEmpty() ? null : city.getWars().get(0);
        this.pendingInvites = war != null && war.getAttacker().equals(city) ? war.getPendingAllyInvites() : new HashSet<>();
    }

    @Override
    protected ItemStack[] items() {
        List<ItemStack> results = new ArrayList<>();
        if (war == null) return results.toArray(new ItemStack[0]);
        // Allies
        for (City ally : war.getAttackerAllies()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Ally: " + ally.getName(), net.kyori.adventure.text.format.NamedTextColor.GREEN));
            meta.lore(List.of(Component.text("Click to remove ally", net.kyori.adventure.text.format.NamedTextColor.RED)));
            item.setItemMeta(meta);
            results.add(item);
        }
        // Pending invites
        for (City pending : pendingInvites) {
            ItemStack item = new ItemStack(Material.MAP);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("Pending: " + pending.getName(), net.kyori.adventure.text.format.NamedTextColor.YELLOW));
            meta.lore(List.of(Component.text("Invitation sent", net.kyori.adventure.text.format.NamedTextColor.GRAY)));
            item.setItemMeta(meta);
            results.add(item);
        }
        // Add invite button
        ItemStack invite = new ItemStack(Material.EMERALD);
        ItemMeta meta = invite.getItemMeta();
        meta.displayName(Component.text("Invite new ally", net.kyori.adventure.text.format.NamedTextColor.AQUA));
        invite.setItemMeta(meta);
        results.add(invite);
        return results.toArray(new ItemStack[0]);
    }

    @Override
    protected void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (clicked.getType() == Material.PAPER) {
            // Remove ally
            String name = clicked.getItemMeta().getDisplayName().replace("Ally: ", "");
            City toRemove = City.cityArrayList.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
            if (toRemove != null) {
                war.getAttackerAllies().remove(toRemove);
                player.sendMessage(Component.text("Removed ally: " + name, net.kyori.adventure.text.format.NamedTextColor.RED));
                new AlliesGui(player, Component.text("Allies of " + war.getAttacker().getName())).show();
            }
        } else if (clicked.getType() == Material.EMERALD) {
            player.sendMessage(Component.text("Use /city war inviteally <city> to invite a new ally.", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
}
