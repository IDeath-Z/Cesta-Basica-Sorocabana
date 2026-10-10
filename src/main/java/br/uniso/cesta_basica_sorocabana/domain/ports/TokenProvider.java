package br.uniso.cesta_basica_sorocabana.domain.ports;

public interface TokenProvider {

    String generateAccessToken(String subject);
    String validateAccessToken(String token);
}
