package com.mimo.citygui;

import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CityPlayerInfoGui extends AbstractInventoryGui {
    private final Player infoPlayer;

    public CityPlayerInfoGui(Player player, Player infoPlayer) {
        super(player, Component.text(infoPlayer.getName() + "'s Info"));
        this.infoPlayer = infoPlayer;
    }

    /**
     * Generates an array of {@link ItemStack} objects representing the items to be displayed
     * in the GUI. This layout includes decorative glass panes, a player head representing the
     * information about a specific player, a barrier for navigation, and an experience bottle
     * showing the player's total experience points.
     * <p>
     * Decorative elements:
     * - Fills the first and last rows with gray stained-glass panes.
     * - Includes a barrier item in the bottom right corner for navigation.
     * Player-specific elements:
     * - Displays the player's head in the center.
     * - Shows the total experience of the player beside their head.
     *
     * @return an array of {@link ItemStack}, representing layout and content of the GUI.
     * Typically, returns an empty array as items are directly placed into the inventory grid.
     */
    @Override
    protected ItemStack[] items() {
        for (int col = 0; col < 10; col++) {
            addItem(col, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(col, 6, Material.GRAY_STAINED_GLASS_PANE);
        }
        addItem(8, 6, Material.BARRIER);
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
