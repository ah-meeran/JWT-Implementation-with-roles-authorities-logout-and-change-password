package com.example.demo.model.user;

import com.example.demo.model.permission.Permission;
import com.example.demo.model.permission.Role;
import com.example.demo.model.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)

    private Set<Permission> permission = new HashSet<>();

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {

    // // 1. Add role-based permissions from the Role enum
    // return role.getAuthorities();
    // }

    @OneToMany(mappedBy = "customerUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private List<Token> allTokem;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Combine role-based permissions and custom permissions
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role-based permissions\

        // authorities.addAll(role.getAuthorities().stream()
        // .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
        // .collect(Collectors.toList()));

        authorities.addAll(role.getAuthorities());
        // Add custom permissions

        // authorities.addAll(permission.stream()
        // .map(p -> new SimpleGrantedAuthority(p.getPermission())) // Use
        // getPermission() instead of name()
        // .collect(Collectors.toList()));

        // return authorities;

        authorities.addAll(permission.stream()
                .map(p -> new SimpleGrantedAuthority(p.name())) // this is for ADMIN_READ
                .collect(Collectors.toList()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
