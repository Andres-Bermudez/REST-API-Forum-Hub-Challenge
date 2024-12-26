package com.alura.restapiforohubchallenge.domain.login.user;

import jakarta.validation.constraints.NotNull;

public record UserCreateDTO(
        @NotNull
        String username,

        @NotNull
        String email,

        @NotNull
        String password
) {
}
