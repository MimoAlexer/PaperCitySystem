package com.mimo.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PlayerHeads {
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
