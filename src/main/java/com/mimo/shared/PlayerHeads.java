package com.mimo.shared;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PlayerHeads {
    /**
     * Creates a {@link ItemStack} representing a player head corresponding to the given UUID.
     * The player's name is displayed as the item's display name if available.
     *
     * @param uuid the UUID of the player whose head is to be created, must not be null
     * @return an {@link ItemStack} representing the player's head, never null
     */
    public static @NotNull ItemStack fromUuid(@NotNull UUID uuid) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        if (Bukkit.getPlayer(uuid) != null) {
            skullMeta.displayName(Component.text(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName()));
        } else {
            skullMeta.displayName(Component.text(Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())));
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
