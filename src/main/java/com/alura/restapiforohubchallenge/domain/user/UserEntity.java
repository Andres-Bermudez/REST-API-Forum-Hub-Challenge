package com.alura.restapiforohubchallenge.domain.user;

import lombok.Getter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    private String email;
    private String password;

    @Column(name = "active_status")
    private Boolean activeStatus;

    public UserEntity(Long id,
                      String userName,
                      String email,
                      String password
    ) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.activeStatus = true;
    }
}
