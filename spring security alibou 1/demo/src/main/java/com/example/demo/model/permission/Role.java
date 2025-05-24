package com.example.demo.model.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
        USER(Set.of()),
        ADMIN(Set.of(
                        Permission.ADMIN_READ,
                        Permission.ADMIN_WRITE,
                        Permission.ADMIN_DELETE,
                        Permission.ADMIN_UPDATE,
                        Permission.MANAGER_UPDATE,
                        Permission.MANAGER_READ,
                        Permission.MANAGER_WRITE,
                        Permission.MANAGER_DELETE)),
        MANAGER(Set.of(
                        Permission.MANAGER_READ,
                        Permission.MANAGER_WRITE,
                        Permission.MANAGER_DELETE,
                        Permission.MANAGER_UPDATE));

        @Getter
        private final Set<Permission> permissions;

        // public Collection<? extends GrantedAuthority> getAuthorities() {
        // List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // // Add role-based permissions
        // authorities.addAll(this.getPermissions().stream()
        // .map(permission -> new SimpleGrantedAuthority(permission.name()))
        // .collect(Collectors.toList()));

        // // Add the ROLE_ prefix for role-based authorization
        // authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        // return authorities;
        // }

        public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                // Add role-based permissions using the custom format
                authorities.addAll(this.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.name())) // Use {i use name()
                                                                                                  // for ADMIN_READ }

                                .collect(Collectors.toList()));

                // Add the ROLE_ prefix for role-based authorization
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

                return authorities;
        }
}
