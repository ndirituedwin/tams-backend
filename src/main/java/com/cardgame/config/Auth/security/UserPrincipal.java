//package com.cardgame.config.Auth.security;
//
//import com.cardgame.Entity.User;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserPrincipal implements UserDetails {
//
//
//    private Long id;
//    private Long uid;
//    private String  username;
//    private String mobilenumber;
//
//
//
//
//    @JsonIgnore
//    private String password;
//    public Collection<? extends GrantedAuthority> authorities;
//
//
//
//    public static UserPrincipal create(User user) {
////        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
////                new SimpleGrantedAuthority(role.getName().name())
////        ).collect(Collectors.toList());
//        List<GrantedAuthority> authorities = Collections.emptyList();
//
//
//        return new UserPrincipal(
//                user.getId(),
//                user.getUID(),
//                user.getUsername(),
//                user.getMobilenumber(),
//                user.getPassword(),
//                authorities
//        );
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}