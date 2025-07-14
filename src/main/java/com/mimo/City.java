package com.mimo;

import com.mimo.api.gui.GenericConfirmationGui;
import com.mimo.citygui.CityClaimGui;
import com.mimo.citygui.CityMainGui;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class City {
    // TODO: Implement that cities can be lvl up and that players can be kicked
    // TODO: Implement War System very fun O_O
    // TODO: Able to save the Items in right order for the Treasure Chamber
    // Is there an Inventory Object that can be saved and loaded?
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


    public static int cityCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (!(City.playerCityHashMap.containsKey(player))) {
                player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
                return 0;
            }
            City city = City.getCityByPlayer(player);
            new CityMainGui(player).show();
            return 1;
        }
        return 0;
    }

    public static int cityJoinCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (City.playerCityHashMap.containsKey(player)) {
                player.sendMessage(Component.text("You are already in " + City.getCityByPlayer(player).getName() + "!"));
                return 0;
            }
        } else {
            ctx.getSource().getExecutor().sendMessage(Component.text("This command can only be executed by a player!"));
            return 0;
        }
        City.cityArrayList.forEach(city -> {
            if (ctx.getArgument("name", String.class).equals(city.getName())) {
                new GenericConfirmationGui(player, Component.text("Confirmation Gui")) {
                    @Override
                    public void onConfirm(InventoryClickEvent event) {
                        // city.addPlayer(player); or smt
                        // TODO: add that the owner of the city has to accept the request
                        // A chat button or a gui idk
                    }

                    @Override
                    public void onCancel(InventoryClickEvent event) {
                        inventory.close();
                    }
                };
            }
        });
        return 0;
    }

    public static int cityClaimCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (!(City.playerCityHashMap.containsKey(player))) {
                player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>."));
                return 0;
            }
            new CityClaimGui(player).show();
            return 1;
        }
        return 0;
    }

    public static int cityCreateCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player && City.playerCityHashMap.containsKey(player)) {
            player.sendMessage(Component.text("You are already in " + City.getCityByPlayer(player).getName() + "!"));
            return 0;
        }

        if (ctx.getSource().getExecutor() instanceof Player player) {
            new GenericConfirmationGui(player, Component.text("Confirmation Gui")) {
                @Override
                public void onConfirm(InventoryClickEvent event) {
                    City city = new City(ctx.getArgument("name", String.class), player);
                    player.sendMessage(Component.text("Successfully created city named " + city.getName() + "!"));
                }

                @Override
                public void onCancel(InventoryClickEvent event) {
                }
            };
            return 1;
        }
        return 0;
    }


    public static CompletableFuture<Suggestions> cityJoinCommandSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }
}