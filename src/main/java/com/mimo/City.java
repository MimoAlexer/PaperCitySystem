package com.mimo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class City {
    public static HashMap<Player, City> playerCityHashMap = new HashMap<>();
    public static ArrayList<City> cityArrayList = new ArrayList<>();
    public static HashMap<Player, Permissions> playerPermissionsHashMap = new HashMap<>();
    private final List<Chunk> chunks = new ArrayList<>();
    @Setter
    private String name;
    @Setter
    private Player owner;
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
}