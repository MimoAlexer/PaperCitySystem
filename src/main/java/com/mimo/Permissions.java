package com.mimo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permissions {
    public boolean hasBlockBreakPermission = false;
    public boolean hasBlockPlacePermission = false;
    public boolean hasInteractPermission = false;
    public boolean hasClaimPermission = false;
    public boolean hasKickPermission = false;
    public boolean hasTreasureChamberPermission = false;
    public boolean hasInvitePermission = false;
    public boolean hasPermissionEditPermission = false;
}
