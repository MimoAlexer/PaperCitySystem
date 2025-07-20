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

    // Store pending join requests for offline owners
    private static final Set<PendingJoinRequest> pendingJoinRequests = new HashSet<>();

    private static class PendingJoinRequest {
        public final Player requester;
        public final City city;
        public PendingJoinRequest(Player requester, City city) {
            this.requester = requester;
            this.city = city;
        }
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

    public static int cityCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.YELLOW));
            return 0;
        } else {
            new CityMainGui((Player) ctx.getSource().getExecutor()).show();
            return 1;
        }
        // unreachable
    }

    public static int cityJoinCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (City.playerCityHashMap.containsKey(player)) {
                player.sendMessage(Component.text("You are already in " + City.getCityByPlayer(player).getName() + "!", NamedTextColor.YELLOW));
                return 0;
            }
        } else {
            ctx.getSource().getExecutor().sendMessage(Component.text("This command can only be executed by a player!", NamedTextColor.RED));
            return 0;
        }
        City.cityArrayList.forEach(city -> {
            if (ctx.getArgument("name", String.class).equals(city.getName())) {
                Player owner = city.getOwner();
                if (owner.isOnline()) {
                    Component joinMsg = Component.text(player.getName() + " wants to join your city! ", NamedTextColor.AQUA)
                        .append(Component.text("[Review]", NamedTextColor.GREEN)
                            .hoverEvent(HoverEvent.showText(Component.text("Click to review join request", NamedTextColor.YELLOW)))
                            .clickEvent(ClickEvent.runCommand("/city reviewjoin " + player.getName() + " " + city.getName())));
                    owner.sendMessage(joinMsg);
                } else {
                    pendingJoinRequests.add(new PendingJoinRequest(player, city));
                    player.sendMessage(Component.text("The city owner is offline. They will be notified when they come online.", NamedTextColor.YELLOW));
                }
            }
        });
        return 0;
    }

    public static void notifyOwnerOnLogin(Player owner) {
        pendingJoinRequests.removeIf(req -> {
            if (req.city.getOwner().equals(owner)) {
                Component joinMsg = Component.text(req.requester.getName() + " wanted to join your city while you were offline. ", NamedTextColor.AQUA)
                    .append(Component.text("[Review]", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to review join request", NamedTextColor.YELLOW)))
                        .clickEvent(ClickEvent.runCommand("/city reviewjoin " + req.requester.getName() + " " + req.city.getName())));
                owner.sendMessage(joinMsg);
                return true;
            }
            return false;
        });
    }

    public static int reviewJoinCommandExecute(CommandContext<CommandSourceStack> ctx) {
        String playerName = ctx.getArgument("player", String.class);
        String cityName = ctx.getArgument("city", String.class);
        Player owner = (Player) ctx.getSource().getExecutor();
        Player requester = Bukkit.getPlayerExact(playerName);
        City city = City.cityArrayList.stream().filter(c -> c.getName().equals(cityName)).findFirst().orElse(null);
        if (city == null || requester == null) {
            owner.sendMessage(Component.text("Invalid join request.", NamedTextColor.RED));
            return 0;
        }
        new GenericConfirmationGui(owner, Component.text("Accept " + requester.getName() + " into your city?", NamedTextColor.AQUA)) {
            @Override
            public void onConfirm(InventoryClickEvent event) {
                city.addPlayer(requester);
                owner.sendMessage(Component.text("You accepted " + requester.getName() + " into your city!", NamedTextColor.GREEN));
                requester.sendMessage(Component.text("You have been accepted into " + city.getName() + "!", NamedTextColor.GREEN));
                inventory.close();
            }
            @Override
            public void onCancel(InventoryClickEvent event) {
                owner.sendMessage(Component.text("You declined " + requester.getName() + "'s join request.", NamedTextColor.YELLOW));
                requester.sendMessage(Component.text("Your join request to " + city.getName() + " was declined.", NamedTextColor.RED));
                inventory.close();
            }
        };
        return 1;
    }

    public static int cityClaimCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>.", NamedTextColor.YELLOW));
            return 0;
        } else {
            new CityClaimGui((Player) ctx.getSource().getExecutor()).show();
            return 1;
        }
        // unreachable
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


    public static CompletableFuture<Suggestions> cityJoinCommandSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
// Uh ohhh 200 lines rule is broken <BS> XD