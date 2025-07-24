package com.mimo;

import com.mimo.api.gui.GenericConfirmationGui;
import com.mimo.citygui.CityClaimGui;
import com.mimo.citygui.CityMainGui;
import com.mimo.citygui.TreasureChamber;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BannerMeta;
import java.util.UUID;

@Getter
public class City {
    // TODO: Implement that cities can be lvl up and that players can be kicked
    // TODO: Implement War System very fun O_O
    List<War> wars = new ArrayList<>();
    public static HashMap<Player, City> playerCityHashMap = new HashMap<>();
    public static ArrayList<City> cityArrayList = new ArrayList<>();
    public static HashMap<Player, Permissions> playerPermissionsHashMap = new HashMap<>();
    private final List<Chunk> chunks = new ArrayList<>();
    @Setter
    private String name;
    @Setter
    private Player owner;
    private final CityTypes cityType = CityTypes.SETTLEMENT;
    private final List<Player> players = new ArrayList<>();
    private final TreasureChamber treasureChamber;
    @Setter
    private int exp = 0;
    @Setter
    private ItemStack banner; // City's banner/emblem
    private final List<UUID> pendingApplications = new ArrayList<>(); // Player UUIDs who applied to join

    public void addApplication(Player player) {
        if (!pendingApplications.contains(player.getUniqueId())) {
            pendingApplications.add(player.getUniqueId());
        }
    }
    public void removeApplication(Player player) {
        pendingApplications.remove(player.getUniqueId());
    }
    public List<UUID> getPendingApplications() {
        return pendingApplications;
    }

    public City(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        cityArrayList.add(this);
        players.add(owner);
        playerCityHashMap.put(owner, this);
        Permissions permissions = new Permissions();
        permissions.setHasBlockBreakPermission(true);
        permissions.setHasBlockPlacePermission(true);
        permissions.setHasInteractPermission(true);
        permissions.setHasClaimPermission(true);
        playerPermissionsHashMap.put(owner, permissions);
        this.treasureChamber = new TreasureChamber();
        // Default banner: white banner, no patterns
        ItemStack defaultBanner = new ItemStack(Material.WHITE_BANNER);
        this.banner = defaultBanner;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            playerCityHashMap.put(player, this);
            playerPermissionsHashMap.put(player, new Permissions());
        }
    }

    public Permissions getPermissions(Player player) {
        return playerPermissionsHashMap.get(player);
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            playerCityHashMap.remove(player);
        }
    }

    public static City getCityByPlayer(Player player) {
        return playerCityHashMap.get(player);
    }

    public TreasureChamber getTreasureChamber() {
        return treasureChamber;
    }

    public CityTypes getCurrentCityType() {
        if (exp >= 4000) return CityTypes.CITY;
        if (exp >= 2000) return CityTypes.TOWN;
        if (exp >= 1000) return CityTypes.VILLAGE;
        if (exp >= 500) return CityTypes.HAMLET;
        return CityTypes.SETTLEMENT;
    }

    public void setBanner(ItemStack banner) {
        if (banner != null && banner.getType().name().endsWith("BANNER")) {
            this.banner = banner;
        }
    }

    public static int cityCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.YELLOW));
            return 0;
        } else {
            new CityMainGui((Player) ctx.getSource().getExecutor()).show();
            return 1;
        }
    }

    public static int cityClaimCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>.", NamedTextColor.YELLOW));
            return 0;
        } else {
            new CityClaimGui((Player) ctx.getSource().getExecutor()).show();
            return 1;
        }
    }

    public static int cityCreateCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player && City.playerCityHashMap.containsKey(player)) {
            player.sendMessage(Component.text("You are already in " + City.getCityByPlayer(player).getName() + "!", NamedTextColor.YELLOW));
            return 0;
        }
        if (ctx.getSource().getExecutor() instanceof Player player) {
            new GenericConfirmationGui(player, Component.text("Confirmation Gui", NamedTextColor.AQUA)) {
                @Override
                public void onConfirm(InventoryClickEvent event) {
                    City city = new City(ctx.getArgument("name", String.class), player);
                    player.sendMessage(Component.text("Successfully created city named " + city.getName() + "!", NamedTextColor.GREEN));
                }
                @Override
                public void onCancel(InventoryClickEvent event) {
                }
            };
            return 1;
        }
        return 0;
    }

    public static int cityRenameCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getExecutor().sendMessage(Component.text("This command can only be executed by a player!", NamedTextColor.RED));
            return 0;
        }
        City city = City.getCityByPlayer(player);
        if (city == null) {
            player.sendMessage(Component.text("You are not in a city!", NamedTextColor.RED));
            return 0;
        }
        if (!city.getOwner().equals(player)) {
            player.sendMessage(Component.text("Only the city owner can rename the city!", NamedTextColor.RED));
            return 0;
        }
        String newName = ctx.getArgument("newname", String.class);
        for (City c : cityArrayList) {
            if (c.getName().equalsIgnoreCase(newName)) {
                player.sendMessage(Component.text("A city with that name already exists!", NamedTextColor.RED));
                return 0;
            }
        }
        String oldName = city.getName();
        city.setName(newName);
        player.sendMessage(Component.text("City renamed from " + oldName + " to " + newName + "!", NamedTextColor.GREEN));
        return 1;
    }

    public static CompletableFuture<Suggestions> cityJoinCommandSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int cityLeaveCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getExecutor().sendMessage(Component.text("This command can only be executed by a player!", NamedTextColor.RED));
            return 0;
        }
        City city = City.getCityByPlayer(player);
        if (city == null) {
            player.sendMessage(Component.text("You are not in a city!", NamedTextColor.RED));
            return 0;
        }
        if (city.getOwner().equals(player)) {
            player.sendMessage(Component.text("The city owner cannot leave the city! Use /city disband to delete the city.", NamedTextColor.RED));
            return 0;
        }
        city.removePlayer(player);
        player.sendMessage(Component.text("You have left " + city.getName() + "!", NamedTextColor.YELLOW));
        return 1;
    }
}
// Uh ohhh 200 lines rule is broken <BS> XD
// NOOOO TAHT FILE IS TO MASSIVE!!!!!