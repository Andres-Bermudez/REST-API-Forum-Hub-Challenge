package com.alura.restapiforohubchallenge.domain.login.authentication;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alura.restapiforohubchallenge.domain.login.jsonwebtoken.TokenDTO;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.login.jsonwebtoken.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import com.alura.restapiforohubchallenge.domain.login.user.AuthenticationUserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> authenticationUser(
            @RequestBody
            @Valid
            AuthenticationUserDTO authenticationUserDTO
    ) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                authenticationUserDTO.username(),
                authenticationUserDTO.password()
        );
        Authentication authenticateUser = authenticationManager.authenticate(authToken);

        UserEntity userAuthenticate = (UserEntity) authenticateUser.getPrincipal();
        String generatedToken = tokenService.generateToken(userAuthenticate);

        return ResponseEntity.ok(new TokenDTO(generatedToken));
    }
}
