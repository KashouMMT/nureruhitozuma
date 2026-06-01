package com.dev.main.security;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dev.main.model.User;


public class MyUserDetails implements UserDetails{

    @Serial
    private static final long serialVersionUID = 1L;
	
    private final Long id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final List<SimpleGrantedAuthority> authorities;
	
	public MyUserDetails(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.enabled = user.isEnabled();
		this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isAdmin() {
		return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	}
	
	public boolean isUser() {
		return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
	}
}
