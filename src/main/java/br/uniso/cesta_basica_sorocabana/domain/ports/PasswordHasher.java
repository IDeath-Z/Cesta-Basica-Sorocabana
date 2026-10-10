package br.uniso.cesta_basica_sorocabana.domain.ports;

public interface PasswordHasher {

    String hash(String password);
    boolean verify(String password, String hash);
}
