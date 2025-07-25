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
    private final City city;
    private final boolean canEdit;
    public CityMainGui(Player player) {
        this(player, City.getCityByPlayer(player));
    }
    public CityMainGui(Player player, City city) {
        super(player, Component.text(city.getName()));
        this.city = city;
        this.canEdit = city.getOwner().equals(player) || city.getPlayers().contains(player);
    }

    @Override
    protected ItemStack[] items() {
        // Use this.city instead of always looking up by 
        // thats based...
        City city = this.city;
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
        // Only show action buttons if canEdit
        // small refactor here (please dont ask why)
        // bs refactor
        if (canEdit) {
            if (city.getOwner().equals(player)) {
                ItemStack review = new ItemStack(Material.PAPER);
                review.getItemMeta().displayName(Component.text("Review Applications"));
                addItem(7, 2, review);
            } else if (!city.getPlayers().contains(player)) {
                // Not a member: show apply button
                // bs code here (Please please please dont ask why)
                ItemStack apply = new ItemStack(Material.WRITABLE_BOOK);
                apply.getItemMeta().displayName(Component.text("Apply to Join"));
                addItem(7, 2, apply);
            }
        }
        ItemStack leaderboard = new ItemStack(Material.GOLD_INGOT);
        leaderboard.getItemMeta().displayName(Component.text("Leaderboard"));
        addItem(7, 4, leaderboard);
        ItemStack changes = new ItemStack(Material.CLOCK);
        changes.getItemMeta().displayName(Component.text("Recent Changes"));
        addItem(6, 4, changes);
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
        if (!canEdit) return;
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) new CityPlayerGui(player).show();
        if (isItemStackClicked(Material.BLACK_BANNER, event)) new CityClaimGui(player).show();
        if (isItemStackClicked(Material.BOOK, event)) new CityLawGui(player).show();
        // Application/Review button logic
        if (isItemStackClicked(Material.PAPER, event)) {
            new CityApplicationGui(player).show();
        }
        if (isItemStackClicked(Material.WRITABLE_BOOK, event)) {
            city.addApplication(player);
            player.sendMessage(Component.text("Application submitted!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
            player.closeInventory();
        }
        // Banner edit logic
        if (isItemStackClicked(city.getBanner().getType(), event) && city.getOwner().equals(player)) {
            new BannerEditGui(player, city).show();
        }
        if (isItemStackClicked(Material.GOLD_INGOT, event)) {
            new CityLeaderboardGui(player).show();
        }
        if (isItemStackClicked(Material.CLOCK, event)) {
            new CityChangesGui(player).show();
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
