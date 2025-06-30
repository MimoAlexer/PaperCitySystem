package com.mimo.citygui;

import com.mimo.City;
import com.mimo.api.gui.AbstractInventoryGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CityClaimGui extends AbstractInventoryGui {
    private final City playerCity;
    private final Chunk playerChunk;
    private static final int GRID_SIZE = 4;
    private static final int CENTER = 2;

    public CityClaimGui(Player player) {
        super(player, Component.text("Claim Chunks"));
        this.playerCity = City.getCityByPlayer(player);
        this.playerChunk = player.getLocation().getChunk();
    }

    @Override
    protected ItemStack[] items() {
        for (int col = 0; col < 9; col++) {
            addItem(col, 0, Material.GRAY_STAINED_GLASS_PANE);
            addItem(col, 5, Material.GRAY_STAINED_GLASS_PANE);
        }
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.displayName(Component.text("Back"));
        barrier.setItemMeta(barrierMeta);
        addItem(8, 5, barrier);
        int baseX = playerChunk.getX() - CENTER;
        int baseZ = playerChunk.getZ() - CENTER;
        for (int gridZ = 0; gridZ < GRID_SIZE; gridZ++) {
            for (int gridX = 0; gridX < 5; gridX++) {
                int chunkX = baseX + gridX;
                int chunkZ = baseZ + gridZ;
                int guiX = 2 + gridX;
                int guiY = 1 + gridZ;
                ItemStack wool;
                if (gridX == CENTER && gridZ == CENTER) {
                    wool = createChunkItem(Material.BLUE_WOOL, chunkX, chunkZ, "Your Location");
                } else if (isChunkClaimedByPlayerCity(chunkX, chunkZ)) {
                    wool = createChunkItem(Material.YELLOW_WOOL, chunkX, chunkZ, "Your City's Claim");
                } else if (isChunkClaimedByOtherCity(chunkX, chunkZ)) {
                    wool = createChunkItem(Material.RED_WOOL, chunkX, chunkZ, "Claimed by Another City");
                } else {
                    wool = createChunkItem(Material.LIME_WOOL, chunkX, chunkZ, "Unclaimed");
                }
                addItem(guiX, guiY, wool);
            }
        }

        return new ItemStack[0];
    }

    private ItemStack createChunkItem(Material material, int chunkX, int chunkZ, String status) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Chunk [" + chunkX + ", " + chunkZ + "]"));
        meta.lore(List.of(Component.text(status)));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey keyX = new NamespacedKey("cityguiplugin", "chunk-x");
        NamespacedKey keyZ = new NamespacedKey("cityguiplugin", "chunk-z");
        container.set(keyX, PersistentDataType.INTEGER, chunkX);
        container.set(keyZ, PersistentDataType.INTEGER, chunkZ);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isChunkClaimedByPlayerCity(int chunkX, int chunkZ) {
        for (Chunk chunk : playerCity.getChunks()) {
            if (chunk.getX() == chunkX && chunk.getZ() == chunkZ) {
                return true;
            }
        }
        return false;
    }

    private boolean isChunkClaimedByOtherCity(int chunkX, int chunkZ) {
        for (City city : City.playerCityHashMap.values()) {
            if (city != playerCity) {
                for (Chunk chunk : city.getChunks()) {
                    if (chunk.getX() == chunkX && chunk.getZ() == chunkZ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void clickCallback(InventoryClickEvent event) {
        event.setCancelled(true);

        if (isItemStackClicked(Material.BARRIER, event)) {
            new CityMainGui(player).show();
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getItemMeta() != null) {
            Material type = clickedItem.getType();
            if (type == Material.LIME_WOOL) {
                PersistentDataContainer container = clickedItem.getItemMeta().getPersistentDataContainer();
                NamespacedKey keyX = new NamespacedKey("cityguiplugin", "chunk-x");
                NamespacedKey keyZ = new NamespacedKey("cityguiplugin", "chunk-z");
                if (container.has(keyX, PersistentDataType.INTEGER) && container.has(keyZ, PersistentDataType.INTEGER)) {
                    if (container.get(keyX, PersistentDataType.INTEGER) == null || container.get(keyZ, PersistentDataType.INTEGER) == null)
                        return;
                    // Bruh using Containers
                    int chunkX = container.get(keyX, PersistentDataType.INTEGER);
                    int chunkZ = container.get(keyZ, PersistentDataType.INTEGER);
                    attemptToClaimChunk(chunkX, chunkZ);
                    show();
                }
            }
        }
    }

    private void attemptToClaimChunk(int chunkX, int chunkZ) {
        if (playerCity.getPermissions(player).hasClaimPermission) {
            Chunk chunk = player.getWorld().getChunkAt(chunkX, chunkZ);
            if (!playerCity.getChunks().contains(chunk)) {
                playerCity.getChunks().add(chunk);
                player.sendMessage(Component.text("Successfully claimed chunk at [" + chunkX + ", " + chunkZ + "]!"));
            }
        } else {
            player.sendMessage(Component.text("You don't have permission to claim chunks!"));
        }
        // TODO: Implement cool Particles or effects at the claimed chunk
        // Red for enemy cities and green for your own and blue for allies
    }

    @Override
    protected void inventoryCloseCallback(InventoryCloseEvent event) {
    }
}
