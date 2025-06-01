package com.mimo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permissions {
    public boolean blockBreakPermission = false;
    public boolean blockPlacePermission = false;
    public boolean interactPermission = false;
    public boolean claimPermission = false;
}
