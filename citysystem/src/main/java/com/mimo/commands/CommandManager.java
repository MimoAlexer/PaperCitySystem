package com.mimo.commands;

import com.mimo.City;
import com.mimo.citygui.CityClaimGui;
import com.mimo.citygui.CityMainGui;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandManager {
    public CommandManager(Commands commands) {
        commands.register(
                Commands.literal("city")
                        .then(
                                Commands.literal("create")
                                        .executes(CommandManager::cityCreateCommandSended)
                        )
                        .then(
                                Commands.literal("claim")
                                        .executes(CommandManager::cityClaimCommandSended)
                        )
                        .then(
                                Commands.literal("join")
                                        .executes(CommandManager::cityJoinCommandSended)
                        )
                        .executes(CommandManager::cityCommandExecute).build(), "Manage Cities", List.of("c")
        );
        // TODO: Implement the city command
    }

    public static int cityCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (!(City.playerCityHashMap.containsKey(player))) {
                player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
                return 0;
            }
            City city = City.getCityByPlayer(player);
            new CityMainGui(player).show();
        }
        return 0;
    }

    // TODO: Refactor name
    public static int cityJoinCommandSended(CommandContext<CommandSourceStack> ctx) {
        return 0;
    }

    public static int cityClaimCommandSended(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (!(City.playerCityHashMap.containsKey(player))) {
                player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>."));
                return 0;
            }
            new CityClaimGui(player).show();
        }
        return 0;
    }

    public static int cityCreateCommandSended(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            new City("test", player);
        }
        // TODO: add check when hes already in a City
        return 0;
    }
}
