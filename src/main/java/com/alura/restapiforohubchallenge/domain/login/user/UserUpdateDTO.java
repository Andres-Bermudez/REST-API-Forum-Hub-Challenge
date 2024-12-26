package com.alura.restapiforohubchallenge.domain.login.user;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(

        @NotNull
        String actualEmail,

        String newUserName,
        String newEmail,
        String newPassword
) {
}
