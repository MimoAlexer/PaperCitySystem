package com.mimo;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a set of permissions that can be assigned to a player within a city.
 * These permissions control actions that the player is allowed to perform in the city.
 * <p>
 * The permissions include:
 * - Breaking blocks in the city.
 * - Placing blocks in the city.
 * - Interacting with entities or objects in the city.
 * - Claiming chunks for the city.
 * <p>
 * Each permission is represented as a boolean, where true means the player has permission
 * to perform the respective action, and false means the player does not have permission.
 */
@Getter
@Setter
public class Permissions {
    public boolean blockBreakPermission = false;
    public boolean blockPlacePermission = false;
    public boolean interactPermission = false;
    public boolean claimPermission = false;
}
