package com.mimo;

import com.mimo.api.gui.GenericConfirmationGui;
import com.mimo.wargui.AlliesGui;
import com.mimo.wargui.CityWarGui;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Getter
public class War {
    // Add that war returns City or smt for CityWarGui
    City attacker;
    City defender;
    ArrayList<City> attackerAllies = new ArrayList<>();
    ArrayList<City> defenderAllies = new ArrayList<>();
    WarTypes warType;
    int attackerScore = 0;
    int defenderScore = 0;

    public War(City attacker, City defender, WarTypes warType) {
        this.attacker = attacker;
        this.defender = defender;
        this.warType = warType;
        attacker.wars.add(this);
        defender.wars.add(this);
    }

    public void addAttackerAlly(City ally) {
        if (!attackerAllies.contains(ally)) {
            attackerAllies.add(ally);
        }
    }

    public void addDefenderAlly(City ally) {
        if (!defenderAllies.contains(ally)) {
            defenderAllies.add(ally);
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    public static int cityStartWarCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (City.playerCityHashMap.get(ctx.getSource().getExecutor()) == null) {
            ctx.getSource().getExecutor().sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
            return 0;
        }
        WarTypes warType = WarTypes.getWarType(ctx.getArgument("wartype", String.class));
        City defenderCity = City.playerCityHashMap.get(ctx.getArgument("city", String.class));
        City attackerCity = City.playerCityHashMap.get(ctx.getSource().getExecutor());
        for (War war : attackerCity.getWars()) {
            if (war.getDefender().equals(defenderCity) && war.getAttacker().equals(attackerCity)) {
                ctx.getSource().getExecutor().sendMessage(Component.text("You are already at war with " + defenderCity.getName() + "!"));
                return 0;
            }
        }
        if (warType == null) {
            ctx.getSource().getExecutor().sendMessage(Component.text("Enter a valid war type!"));
            return 0;
        }
        if (warType != WarTypes.RAID && warType != WarTypes.TRIBUTARY && warType != WarTypes.CONQUEST) {
            ctx.getSource().getExecutor().sendMessage(Component.text("Unknown war type!"));
            return 0;
        }
        final CompletableFuture<Integer> result = new CompletableFuture<>();
        new GenericConfirmationGui((Player) ctx.getSource().getExecutor(), Component.text("Are you sure you want to start a " + warType.name() + " war with " + defenderCity.getName() + "?")) {
            @Override
            public void onConfirm(InventoryClickEvent event) {
                new War(attackerCity, defenderCity, warType);
                player.sendMessage(Component.text("You have started a " + warType.name() + " war with " + defenderCity.getName() + "!"));
                result.complete(1);
            }

            @Override
            public void onCancel(InventoryClickEvent event) {
                player.sendMessage(Component.text("You have cancelled the war request with " + defenderCity.getName() + "."));
                result.complete(0);
            }
        };
        return result.join();
    }

    public static CompletableFuture<Suggestions> warTypesSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("tributary");
        actions.add("raid");
        actions.add("conquest");
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int warAlliesCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            assert player != null;
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
            return 0;
        }
        new AlliesGui(player, Component.text("Allies of " + City.getCityByPlayer(player).getName() + "!"));
        return 0;
    }

    public static CompletableFuture<Suggestions> warCitiesSuggest(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static int cityWarCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
            return 0;
        }
        new CityWarGui(player);
        return 0;
    }
}
