package com.example.userauthentication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity implements UserDetails {
    @Id
    private String username;
    @Column(name = "hash")
    private String passwordHash;
    @Column
    private String role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword(){
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
