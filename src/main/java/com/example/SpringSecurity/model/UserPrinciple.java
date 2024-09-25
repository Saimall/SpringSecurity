package com.example.SpringSecurity.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrinciple implements UserDetails {

    private Users user;

    public UserPrinciple(){

    }

    public UserPrinciple(Users user) {
     this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        //this are assuming as true as we are just understanding demo
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //this are assuming as true as we are just understanding demo
        return true;
    }

    @Override
    public boolean isEnabled() {
        //this are assuming as true as we are just understanding demo
        return true;
    }
}
