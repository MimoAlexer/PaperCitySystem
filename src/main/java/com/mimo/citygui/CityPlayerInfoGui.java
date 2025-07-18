package com.mimo.citygui;

import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.mimo.City;
import com.mimo.Permissions;

public class CityPlayerInfoGui extends AbstractInventoryGui {
    private final Player infoPlayer;

    public CityPlayerInfoGui(Player player, Player infoPlayer) {
        super(player, Component.text(infoPlayer.getName() + "'s Info"));
        this.infoPlayer = infoPlayer;
    }

    @Override
    protected ItemStack[] items() {
        for (int col = 0; col < 9; col++) {
            addItem(col, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(col, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        addItem(8, 5, Material.BARRIER);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(infoPlayer);
            meta.displayName(Component.text(infoPlayer.getName()));
            head.setItemMeta(meta);
        }
        addItem(4, 3, head);

        ItemStack infoExp = new ItemStack(Material.EXPERIENCE_BOTTLE);
        var itemMeta = infoExp.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text(infoPlayer.getTotalExperience() + " XP"));
            infoExp.setItemMeta(itemMeta);
        }
        addItem(6, 2, infoExp);

        City city = City.getCityByPlayer(infoPlayer);
        String cityName = city != null ? city.getName() : "None";
        String role = (city != null && city.getOwner().equals(infoPlayer)) ? "Owner" : "Member";
        ItemStack cityInfo = new ItemStack(Material.PAPER);
        var cityMeta = cityInfo.getItemMeta();
        if (cityMeta != null) {
            cityMeta.displayName(Component.text("City Info"));
            cityMeta.lore(java.util.List.of(
                Component.text("City: " + cityName),
                Component.text("Role: " + role)
            ));
            cityInfo.setItemMeta(cityMeta);
        }
        addItem(2, 2, cityInfo);

        Permissions perms = City.playerPermissionsHashMap.get(infoPlayer);
        ItemStack permBook = new ItemStack(Material.BOOK);
        var permMeta = permBook.getItemMeta();
        if (permMeta != null && perms != null) {
            permMeta.displayName(Component.text("Permissions"));
            permMeta.lore(java.util.List.of(
                Component.text("Block Break: " + perms.hasBlockBreakPermission),
                Component.text("Block Place: " + perms.hasBlockPlacePermission),
                Component.text("Interact: " + perms.hasInteractPermission),
                Component.text("Claim: " + perms.hasClaimPermission),
                Component.text("Kick: " + perms.hasKickPermission),
                Component.text("Treasure Chamber: " + perms.hasTreasureChamberPermission)
            ));
            permBook.setItemMeta(permMeta);
        }
        addItem(2, 3, permBook);

        ItemStack stats = new ItemStack(Material.COMPASS);
        var statsMeta = stats.getItemMeta();
        if (statsMeta != null) {
            statsMeta.displayName(Component.text("Statistics"));
            int kills = infoPlayer.getStatistic(org.bukkit.Statistic.PLAYER_KILLS);
            int deaths = infoPlayer.getStatistic(org.bukkit.Statistic.DEATHS);
            int playTicks = infoPlayer.getStatistic(org.bukkit.Statistic.PLAY_ONE_MINUTE);
            int playMinutes = playTicks / (20 * 60);
            statsMeta.lore(java.util.List.of(
                Component.text("Kills: " + kills),
                Component.text("Deaths: " + deaths),
                Component.text("Playtime: " + playMinutes + " min")
            ));
            stats.setItemMeta(statsMeta);
        }
        addItem(6, 3, stats);

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
