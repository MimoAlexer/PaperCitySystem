package com.mimo.commands;

import com.mimo.City;
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
        ArrayList<String> actions = new ArrayList<>();
        City.playerCityHashMap.forEach((player, city) -> {
            actions.add(city.getName().toLowerCase());
        });
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int cityJoinCommandExecute(CommandContext<CommandSourceStack> ctx) {
        // TODO: Implement with the name Argument
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
            return 1;
        }

        if (ctx.getSource().getExecutor() instanceof Player player) {
            new City(ctx.getArgument("name", String.class), player);
        }
        return 0;
    }
}
