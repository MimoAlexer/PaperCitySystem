package com.mimo.commands;

import com.mimo.City;
import com.mimo.api.gui.GenericConfirmationGui;
import com.mimo.citygui.CityClaimGui;
import com.mimo.citygui.CityMainGui;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandManager {
    public CommandManager(Commands commands) {
        commands.register(
                Commands.literal("city")
                        .then(
                                Commands.literal("create")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .executes(CommandManager::cityCreateCommandExecute)
                                        )
                        )
                        .then(
                                Commands.literal("claim")
                                        .executes(CommandManager::cityClaimCommandSended)
                        )
                        .then(
                                Commands.literal("join")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .suggests(CommandManager::cityJoinCommandSuggest)
                                                .executes(CommandManager::cityJoinCommandExecute)
                                        )
                        )
                        .then(
                                Commands.literal("war")
                                        .executes(CommandManager::cityWarCommandExecute) // TODO: add more arguments for war commands

                        )
                        .executes(CommandManager::cityCommandExecute).build(), "Manage Cities", List.of("c")
        );
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

    public static CompletableFuture<Suggestions> cityJoinCommandSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        // TODO: Implement asking if the player already is in a city and add Conformation Gui
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int cityWarCommandExecute(CommandContext<CommandSourceStack> ctx) {
        return 0; // TODO: Implement city war logic
    }

    public static int cityJoinCommandExecute(CommandContext<CommandSourceStack> ctx) {
        City.cityArrayList.forEach(city -> {
            if (ctx.getArgument("name", String.class).equals(city.getName())) {
                // TODO: Implement city join logic
            }
        });
        return 0;
    }

    public static int cityClaimCommandSended(CommandContext<CommandSourceStack> ctx) {
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
                    inventory.close();
                }

                @Override
                public void onCancel(InventoryClickEvent event) {
                    inventory.close();
                }
            }.show();
            return 1;
        }
        return 0;
    }
}
