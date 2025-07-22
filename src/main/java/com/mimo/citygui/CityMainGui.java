package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class CityMainGui extends AbstractInventoryGui {
    public CityMainGui(Player player) {
        super(player, Component.text(City.getCityByPlayer(player).getName()));
    }

    @Override
    protected ItemStack[] items() {
        City city = City.getCityByPlayer(player);
        // Display the city's banner in the center top (slot 4,0)
        ItemStack banner = city.getBanner();
        if (banner != null) {
            ItemStack bannerCopy = banner.clone();
            BannerMeta meta = (BannerMeta) bannerCopy.getItemMeta();
            meta.displayName(Component.text("City Banner"));
            bannerCopy.setItemMeta(meta);
            addItem(4, 0, bannerCopy);
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        head.getItemMeta().displayName(Component.text("Players"));
        ItemStack claim = new ItemStack(Material.BLACK_BANNER);
        claim.getItemMeta().displayName(Component.text("Claim"));
        ItemStack laws = new ItemStack(Material.BOOK);
        laws.getItemMeta().displayName(Component.text("City Laws"));
        addItem(1, 2, head);
        addItem(3, 2, claim);
        addItem(5, 2, laws);
        // Application/Review button logic
        if (city.getOwner().equals(player)) {
            // Owner: show review applications button
            ItemStack review = new ItemStack(Material.PAPER);
            review.getItemMeta().displayName(Component.text("Review Applications"));
            addItem(7, 2, review);
        } else if (!city.getPlayers().contains(player)) {
            // Not a member: show apply button
            ItemStack apply = new ItemStack(Material.WRITABLE_BOOK);
            apply.getItemMeta().displayName(Component.text("Apply to Join"));
            addItem(7, 2, apply);
        }
        for (int i = 0; i < 9; i++) {
            addItem(i, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(i, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        addItem(8, 5, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) event.getWhoClicked().closeInventory();
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) new CityPlayerGui(player).show();
        if (isItemStackClicked(Material.BLACK_BANNER, event)) new CityClaimGui(player).show();
        if (isItemStackClicked(Material.BOOK, event)) new CityLawGui(player).show();
        // Application/Review button logic
        if (isItemStackClicked(Material.PAPER, event)) {
            new CityApplicationGui(player).show();
        }
        if (isItemStackClicked(Material.WRITABLE_BOOK, event)) {
            City city = City.getCityByPlayer(player);
            city.addApplication(player);
            player.sendMessage(Component.text("Application submitted!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
            player.closeInventory();
        }
        // Banner edit logic
        City city = City.getCityByPlayer(player);
        if (isItemStackClicked(city.getBanner().getType(), event) && city.getOwner().equals(player)) {
            new BannerEditGui(player, city).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
