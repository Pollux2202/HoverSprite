package com.example.demo.User.enumType;

import java.util.Set;

public enum UserType {
    FARMER(Set.of("CREATE_BOOKING", "VIEW_BOOKING", "NOTIFY_USERS")),
    RECEPTIONIST(Set.of("VIEW_BOOKING", "APPROVE_BOOKING", "CHANGE_STATUS", "NOTIFY_USERS")),
    SPRAYER(Set.of("VIEW_BOOKING", "CHANGE_STATUS", "NOTIFY_USERS"));

    private final Set<String> permissions;

    UserType(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
