package com.mimo.citygui;

import com.mimo.City;
import com.mimo.shared.PlayerHeads;
import com.mimo.shared.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class CityPlayerGui extends AbstractInventoryGui {
    public CityPlayerGui(Player player) {
        super(player, Component.text("Players of " + City.getCityByPlayer(player).getName()));
    }

    /**
     * Generates the items to be displayed in the city player GUI. This includes:
     * - Gray stained glass panes for the borders.
     * - A barrier icon at a specific position.
     * - Player heads representing the members of the player's city, placed dynamically
     * based on the total number of members.
     * <p>
     * Each player's head is retrieved using their unique identifier, and the layout
     * of the inventory is adjusted to accommodate all members.
     *
     * @return an array of {@code ItemStack} representing the items for the inventory GUI,
     * though the returned array is empty as the items are directly added to the
     * inventory during execution.
     */
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
            addItem(col, row, PlayerHeads.fromUuid(member.getUniqueId()));
        }
        return new ItemStack[0];
    }

    /**
     * Handles the click events for the CityPlayerGui inventory. This method processes specific
     * inventory item clicks and executes corresponding actions while canceling the original event
     * to prevent unintended interactions with the inventory slots.
     * <p>
     * Clicking a barrier item opens the main city GUI, and clicking a player head triggers the
     * CityPlayerInfoGui to display information about the selected player.
     *
     * @param event the {@link InventoryClickEvent} that represents the inventory click action.
     *              Contains information such as the clicked item, the player performing the action,
     *              and the slot in the inventory that was clicked.
     */
    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);
        if (isItemStackClicked(Material.BARRIER, event)) new CityMainGui(player).show();
        if (isItemStackClicked(Material.PLAYER_HEAD, event)) {
            new CityPlayerInfoGui(
                    player,
                    Objects.
                            requireNonNull(
                                    Bukkit.
                                            getPlayer(
                                                    Objects.
                                                            requireNonNull(
                                                                    Objects.
                                                                            requireNonNull(
                                                                                    event.
                                                                                            getCurrentItem()
                                                                            ).
                                                                            getItemMeta()
                                                                            .displayName()).
                                                            toString()
                                            )
                            )
            );
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {

    }
}
