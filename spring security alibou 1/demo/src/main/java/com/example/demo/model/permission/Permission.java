package com.example.demo.model.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),

    ADMIN_UPDATE("admin:update"),

    MANAGER_READ("manager:read"),
    MANAGER_WRITE("manager:write"),
    MANAGER_DELETE("manager:delete"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_OBEY("manager:obey");

    @Getter
    private final String permission;

    // public static Permission fromPermissionString(String permissionString) {
    // for (Permission permission : Permission.values()) {
    // if (permission.getPermission().equals(permissionString)) {
    // return permission;
    // }
    // }
    // throw new IllegalArgumentException("Unknown permission: " +
    // permissionString);
    // }

}
