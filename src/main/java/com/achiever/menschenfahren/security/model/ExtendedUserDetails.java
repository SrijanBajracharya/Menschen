package com.achiever.menschenfahren.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

@Data
public class ExtendedUserDetails extends User {

    private static final long serialVersionUID = -4792662802072413826L;

    private final String      email;
    private final String      id;


    public ExtendedUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = null;
        this.id = null;
    }

    public ExtendedUserDetails(String username, String email, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = email;
        this.id = null;
    }

    public ExtendedUserDetails(String username, String email, String password, String id, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.email = email;
        this.id = id;
    }

}
