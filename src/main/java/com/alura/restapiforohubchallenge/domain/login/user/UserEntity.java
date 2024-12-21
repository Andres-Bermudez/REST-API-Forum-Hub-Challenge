package com.alura.restapiforohubchallenge.domain.login.user;

import lombok.Getter;
import java.util.List;
import java.util.Collection;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "users")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "user_name")
    private String userName;

    private String email;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "active_status")
    private Boolean activeStatus;

    public UserEntity(Long idUser,
                      String userName,
                      String email,
                      String password
    ) {
        this.idUser = idUser;
        this.userName = userName;
        this.email = email;
        this.userPassword = password;
        this.activeStatus = true;
    }


    // Para inidcarle a spring los datos de autenticacion.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
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
