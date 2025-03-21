package com.github.heisdanielade.pamietampsa.entity;


import com.github.heisdanielade.pamietampsa.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @SequenceGenerator(name = "app_user_seq", sequenceName = "app_user_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Transient
    private Character initial;

    @Column(nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean enabled;

    // For email verification
    private String verificationCode;
    private LocalDateTime verificationCodeExpiresAt;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column
    private Instant lastLoginAt;

    private LocalDate accountExpirationDate = null;


    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Character getInitial(){
        if(this.name != null){
            return this.name.charAt(0);
        } else{
            return this.email.charAt(0);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // Returning email fixed "login 403 forbidden error" whereby spring was
        // recognizing an empty string "" as the auth input instead of actual user email
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (this.accountExpirationDate == null){
            return true;
        }
        return LocalDate.now().isBefore(accountExpirationDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
