package com.mimo;

import com.mimo.api.gui.GenericConfirmationGui;
import com.mimo.wargui.AlliesGui;
import com.mimo.wargui.CityWarGui;
import com.mimo.wargui.EnemyGui;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.HashSet;
import java.util.Set;

@Getter
public class War {
    // Add that war returns City or smt for CityWarGui
    City attacker;
    City defender;
    ArrayList<City> attackerAllies = new ArrayList<>();
    ArrayList<City> defenderAllies = new ArrayList<>();
    private final HashSet<City> pendingAllyInvites = new HashSet<>();
    WarTypes warType;
    int attackerScore = 0;
    int defenderScore = 0;

    // Store pending ally invites for offline owners
    private static final Set<PendingAllyInvite> staticPendingAllyInvites = new HashSet<>();

    private static class PendingAllyInvite {
        public final City inviter;
        public final City invitee;
        public final War war;
        public PendingAllyInvite(City inviter, City invitee, War war) {
            this.inviter = inviter;
            this.invitee = invitee;
            this.war = war;
        }
    }

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

    public HashSet<City> getPendingAllyInvites() {
        return pendingAllyInvites;
    }

    public static void notifyOwnerOnLogin(Player owner) {
        staticPendingAllyInvites.removeIf(req -> {
            if (req.invitee.getOwner().equals(owner)) {
                Component allyMsg = Component.text(req.inviter.getName() + " wants to ally with your city in their war against " + req.war.getDefender().getName() + "! ", NamedTextColor.AQUA)
                    .append(Component.text("[Review]", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to review ally invitation", NamedTextColor.YELLOW)))
                        .clickEvent(ClickEvent.runCommand("/city war reviewally " + req.inviter.getName() + " " + req.invitee.getName() + " " + req.war.getAttacker().getName() + " " + req.war.getDefender().getName())));
                owner.sendMessage(allyMsg);
                return true;
            }
            return false;
        });
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

    public static int warInviteAllyCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.RED));
            return 0;
        }
        
        String targetCityName = ctx.getArgument("city", String.class);
        City inviterCity = City.getCityByPlayer(player);
        City inviteeCity = City.cityArrayList.stream().filter(c -> c.getName().equals(targetCityName)).findFirst().orElse(null);
        
        if (inviteeCity == null) {
            player.sendMessage(Component.text("City " + targetCityName + " does not exist!", NamedTextColor.RED));
            return 0;
        }
        
        if (inviteeCity.equals(inviterCity)) {
            player.sendMessage(Component.text("You cannot invite your own city!", NamedTextColor.RED));
            return 0;
        }
        
        // Find a war where the inviter is the attacker
        War war = inviterCity.getWars().stream()
            .filter(w -> w.getAttacker().equals(inviterCity))
            .findFirst()
            .orElse(null);
            
        if (war == null) {
            player.sendMessage(Component.text("You need to be in a war as the attacker to invite allies!", NamedTextColor.RED));
            return 0;
        }
        
        if (war.getAttackerAllies().contains(inviteeCity)) {
            player.sendMessage(Component.text(inviteeCity.getName() + " is already your ally in this war!", NamedTextColor.YELLOW));
            return 0;
        }
        
        Player owner = inviteeCity.getOwner();
        if (owner.isOnline()) {
            Component allyMsg = Component.text(inviterCity.getName() + " wants to ally with your city in their war against " + war.getDefender().getName() + "! ", NamedTextColor.AQUA)
                .append(Component.text("[Review]", NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to review ally invitation", NamedTextColor.YELLOW)))
                    .clickEvent(ClickEvent.runCommand("/city war reviewally " + inviterCity.getName() + " " + inviteeCity.getName() + " " + war.getAttacker().getName() + " " + war.getDefender().getName())));
            owner.sendMessage(allyMsg);
            player.sendMessage(Component.text("Ally invitation sent to " + inviteeCity.getName() + "!", NamedTextColor.GREEN));
        } else {
            staticPendingAllyInvites.add(new PendingAllyInvite(inviterCity, inviteeCity, war));
            player.sendMessage(Component.text("The city owner is offline. They will be notified when they come online.", NamedTextColor.YELLOW));
        }
        
        return 1;
    }

    public static int warReviewAllyCommandExecute(CommandContext<CommandSourceStack> ctx) {
        String inviterName = ctx.getArgument("inviter", String.class);
        String inviteeName = ctx.getArgument("invitee", String.class);
        String attackerName = ctx.getArgument("attacker", String.class);
        String defenderName = ctx.getArgument("defender", String.class);
        
        Player owner = (Player) ctx.getSource().getExecutor();
        City inviter = City.cityArrayList.stream().filter(c -> c.getName().equals(inviterName)).findFirst().orElse(null);
        City invitee = City.cityArrayList.stream().filter(c -> c.getName().equals(inviteeName)).findFirst().orElse(null);
        City attacker = City.cityArrayList.stream().filter(c -> c.getName().equals(attackerName)).findFirst().orElse(null);
        City defender = City.cityArrayList.stream().filter(c -> c.getName().equals(defenderName)).findFirst().orElse(null);
        
        if (inviter == null || invitee == null || attacker == null || defender == null) {
            owner.sendMessage(Component.text("Invalid ally invitation.", NamedTextColor.RED));
            return 0;
        }
        
        if (!invitee.getOwner().equals(owner)) {
            owner.sendMessage(Component.text("This ally invitation is not for your city.", NamedTextColor.RED));
            return 0;
        }
        
        War war = attacker.getWars().stream()
            .filter(w -> w.getAttacker().equals(attacker) && w.getDefender().equals(defender))
            .findFirst()
            .orElse(null);
            
        if (war == null) {
            owner.sendMessage(Component.text("War not found.", NamedTextColor.RED));
            return 0;
        }
        
        new GenericConfirmationGui(owner, Component.text("Accept " + inviter.getName() + " as an ally in their war against " + defender.getName() + "?", NamedTextColor.AQUA)) {
            @Override
            public void onConfirm(InventoryClickEvent event) {
                war.addAttackerAlly(invitee);
                owner.sendMessage(Component.text("You accepted " + inviter.getName() + " as an ally!", NamedTextColor.GREEN));
                Player inviterOwner = inviter.getOwner();
                if (inviterOwner.isOnline()) {
                    inviterOwner.sendMessage(Component.text(invitee.getName() + " accepted your ally invitation!", NamedTextColor.GREEN));
                }
                inventory.close();
            }
            
            @Override
            public void onCancel(InventoryClickEvent event) {
                owner.sendMessage(Component.text("You declined " + inviter.getName() + "'s ally invitation.", NamedTextColor.YELLOW));
                Player inviterOwner = inviter.getOwner();
                if (inviterOwner.isOnline()) {
                    inviterOwner.sendMessage(Component.text(invitee.getName() + " declined your ally invitation.", NamedTextColor.RED));
                }
                inventory.close();
            }
        };
        
        return 1;
    }
    public static int warRemoveAllyCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.RED));
            return 0;
        }
        
        String targetCityName = ctx.getArgument("city", String.class);
        City playerCity = City.getCityByPlayer(player);
        City targetCity = City.cityArrayList.stream().filter(c -> c.getName().equals(targetCityName)).findFirst().orElse(null);
        
        if (targetCity == null) {
            player.sendMessage(Component.text("City " + targetCityName + " does not exist!", NamedTextColor.RED));
            return 0;
        }
        
        War war = playerCity.getWars().stream()
            .filter(w -> w.getAttacker().equals(playerCity))
            .findFirst()
            .orElse(null);
            
        if (war == null) {
            player.sendMessage(Component.text("You need to be in a war as the attacker to remove allies!", NamedTextColor.RED));
            return 0;
        }
        
        if (!war.getAttackerAllies().contains(targetCity)) {
            player.sendMessage(Component.text(targetCity.getName() + " is not your ally in this war!", NamedTextColor.YELLOW));
            return 0;
        }
        
        war.getAttackerAllies().remove(targetCity);
        player.sendMessage(Component.text("Removed " + targetCity.getName() + " as an ally!", NamedTextColor.GREEN));
        
        Player targetOwner = targetCity.getOwner();
        if (targetOwner.isOnline()) {
            targetOwner.sendMessage(Component.text(playerCity.getName() + " has removed your city as an ally in their war against " + war.getDefender().getName() + ".", NamedTextColor.RED));
        }
        
        return 1;
    }
    
    public static int warListCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.RED));
            return 0;
        }
        
        City playerCity = City.getCityByPlayer(player);
        if (playerCity.getWars().isEmpty()) {
            player.sendMessage(Component.text("Your city is not currently involved in any wars.", NamedTextColor.YELLOW));
            return 0;
        }
        
        player.sendMessage(Component.text("=== Wars for " + playerCity.getName() + " ===", NamedTextColor.GOLD));
        
        for (War war : playerCity.getWars()) {
            String role = war.getAttacker().equals(playerCity) ? "Attacker" : "Defender";
            String opponent = war.getAttacker().equals(playerCity) ? war.getDefender().getName() : war.getAttacker().getName();
            
            player.sendMessage(Component.text("• " + war.getWarType().name() + " War vs " + opponent + " (" + role + ")", NamedTextColor.AQUA));
            
            if (war.getAttacker().equals(playerCity)) {
                if (!war.getAttackerAllies().isEmpty()) {
                    String allies = war.getAttackerAllies().stream()
                        .map(City::getName)
                        .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
                    player.sendMessage(Component.text("  Allies: " + allies, NamedTextColor.GREEN));
                }
            } else {
                if (!war.getDefenderAllies().isEmpty()) {
                    String allies = war.getDefenderAllies().stream()
                        .map(City::getName)
                        .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
                    player.sendMessage(Component.text("  Allies: " + allies, NamedTextColor.GREEN));
                }
            }
            
            player.sendMessage(Component.text("  Score: " + war.getAttackerScore() + " - " + war.getDefenderScore(), NamedTextColor.YELLOW));
        }
        
        return 1;
    }
    
    public static int warEnemiesCommandExecute(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getExecutor();
        if (City.playerCityHashMap.get(player) == null) {
            player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>", NamedTextColor.RED));
            return 0;
        }
        
        City playerCity = City.getCityByPlayer(player);
        if (playerCity.getWars().isEmpty()) {
            player.sendMessage(Component.text("Your city is not currently involved in any wars.", NamedTextColor.YELLOW));
            return 0;
        }
        
        new EnemyGui(player, Component.text("Enemies of " + playerCity.getName()));
        return 1;
    }
}
