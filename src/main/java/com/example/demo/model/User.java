package com.example.demo.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class User implements UserDetails {
    private String username;

    private Integer id;

    private String password;


    private String role;

//    List<GrantedAuthority> authorities=new ArrayList<>();
//    public List<GrantedAuthority> getAuthorities() {
//        return getGrantedAuthorities(getRole(),authorities);
//    }
//    public List<GrantedAuthority> getGrantedAuthorities(String roles, List<GrantedAuthority> authorities) {
//        if (!StringUtils.isEmpty(roles)) {
//            String[] rolesArr = roles.split(",");
//            for (String role : rolesArr ) {
//                SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(role);
//                if(!authorities.contains(simpleGrantedAuthority)){
//                    authorities.add(simpleGrantedAuthority);
//                }
//            }
//        }
//        return authorities;
//    }
//
    List<GrantedAuthority> authorities=new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities.add(new SimpleGrantedAuthority(getRole()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}