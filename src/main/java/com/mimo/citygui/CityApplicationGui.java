package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CityApplicationGui extends AbstractInventoryGui {
    private final City city;
    private final HashMap<ItemStack, UUID> itemStackApplicantMap = new HashMap<>();

    public CityApplicationGui(Player player) {
        super(player, Component.text("Pending Applications"));
        this.city = City.getCityByPlayer(player);
    }

    @Override
    protected ItemStack[] items() {
        List<UUID> applications = city.getPendingApplications();
        int row = 2;
        int col = 1;
        for (UUID uuid : applications) {
            Player applicant = Bukkit.getPlayer(uuid);
            if (applicant == null) continue;
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = head.getItemMeta();
            meta.displayName(Component.text(applicant.getName(), NamedTextColor.AQUA));
            head.setItemMeta(meta);
            itemStackApplicantMap.put(head, uuid);
            addItem(col, row, head);
            // Accept button
            // bs code here (dont ask why)
            ItemStack accept = new ItemStack(Material.LIME_WOOL);
            ItemMeta acceptMeta = accept.getItemMeta();
            acceptMeta.displayName(Component.text("Accept", NamedTextColor.GREEN));
            accept.setItemMeta(acceptMeta);
            addItem(col + 1, row, accept);
            // Reject button
            // bs code here too (please dont ASKKK WHYY)
            ItemStack reject = new ItemStack(Material.RED_WOOL);
            ItemMeta rejectMeta = reject.getItemMeta();
            rejectMeta.displayName(Component.text("Reject", NamedTextColor.RED));
            reject.setItemMeta(rejectMeta);
            addItem(col + 2, row, reject);
            row++;
            if (row > 4) {
                row = 2;
                col += 4;
            }
        }
        addItem(8, 5, Material.BARRIER);
        return new ItemStack[0];
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        if (isItemStackClicked(Material.BARRIER, event)) new CityMainGui(player).show();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;
        // Find which applicant this row is for
        // You know????? I'm so proud that this project isnt done yet
        // Because I'm SHIT AT CODING I WILL BE ALWAYS TRASH
        // (David Goggins reference)
        for (ItemStack head : itemStackApplicantMap.keySet()) {
            UUID uuid = itemStackApplicantMap.get(head);
            Player applicant = Bukkit.getPlayer(uuid);
            if (applicant == null) continue;
            if (clicked.getType() == Material.LIME_WOOL) {
                // Accept
                city.addPlayer(applicant);
                city.removeApplication(applicant);
                player.sendMessage(Component.text("Accepted " + applicant.getName() + "!", NamedTextColor.GREEN));
                applicant.sendMessage(Component.text("You have been accepted into " + city.getName() + "!", NamedTextColor.GREEN));
                new CityApplicationGui(player).show();
                return;
            } else if (clicked.getType() == Material.RED_WOOL) {
                // Reject
                city.removeApplication(applicant);
                player.sendMessage(Component.text("Rejected " + applicant.getName() + ".", NamedTextColor.YELLOW));
                applicant.sendMessage(Component.text("Your application to " + city.getName() + " was rejected.", NamedTextColor.RED));
                new CityApplicationGui(player).show();
                return;
            }
        }
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
} 