package br.uniso.cesta_basica_sorocabana.infrastructure.auth;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import br.uniso.cesta_basica_sorocabana.application.exceptions.UnauthorizedException;
import br.uniso.cesta_basica_sorocabana.infrastructure.enums.errors.AuthError;
import br.uniso.cesta_basica_sorocabana.domain.ports.TokenProvider;

@Component
public final class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration-minutes}")
    private long expirationMinutes;

    @Override
    public String generateAccessToken(String subject) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(expirationMinutes)))
                .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(AuthError.TOKEN_GENERATION_FAILED.message(), e);
        }
    }

    @Override
    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);

            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException(AuthError.INVALID_ACCESS_TOKEN.message());
        }
    }
}
