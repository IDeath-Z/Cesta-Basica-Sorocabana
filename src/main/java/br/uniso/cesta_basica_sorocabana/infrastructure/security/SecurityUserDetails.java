package br.uniso.cesta_basica_sorocabana.infrastructure.security;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Set;

// Authorities are the user's permissions (e.g. CREATE_BRAND), never the role name.
public final class SecurityUserDetails implements UserDetails {

    private final User user;
    private final Set<Permission> permissions;

    public SecurityUserDetails(User user, Set<Permission> permissions) {
        this.user = user;
        this.permissions = Set.copyOf(permissions);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.name()))
            .toList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
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

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
