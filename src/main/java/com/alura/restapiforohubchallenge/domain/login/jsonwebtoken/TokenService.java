package com.alura.restapiforohubchallenge.domain.login.jsonwebtoken;

import com.auth0.jwt.JWT;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.LocalDateTime;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@Service
public class TokenService {

    @Value("${security.token.secret}")
    private String secret;

    public String generateToken(UserEntity userEntity) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ForumHubRESTAPI")
                    .withSubject(userEntity.getUsername())
                    .withClaim("id", userEntity.getIdUser())
                    .withExpiresAt(generateExpiredDateToken())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Token could not be generated.");
        }
    }

    private Instant generateExpiredDateToken() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }


    // Método para validar el token.
    public boolean validateToken(String token) {
        try {
            // Crea el algoritmo HMAC con SHA-256
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Crea un verificador y verifica el token
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;  // El token es válido

        } catch (JWTVerificationException e) {
            // Si el token es inválido. (por ejemplo, firma incorrecta o expirado)
            return false;
        }
    }

    public String getSubjectToken(String tokenReceived) {
        if (tokenReceived == null) {
            throw new ValidationException("This token received is null.");
        }
        DecodedJWT verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            verifier = JWT.require(algorithm)
                    .withIssuer("ForumHubRESTAPI")
                    .build()
                    .verify(tokenReceived);
            verifier.getSubject();

        } catch (JWTVerificationException exception) {
            throw new ValidationException("The token is invalid.");
        }

        if (verifier.getSubject() == null || verifier.getSubject().isEmpty()) {
            throw new ValidationException("The token subject is null or empty.");
        }
        return verifier.getSubject();
    }
}
