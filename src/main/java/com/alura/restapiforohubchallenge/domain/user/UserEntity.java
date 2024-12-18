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
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "user_name")
    private String userName;

    private String email;
    private String password;

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
        this.password = password;
        this.activeStatus = true;
    }
}
