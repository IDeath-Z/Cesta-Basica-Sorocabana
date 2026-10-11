package br.uniso.cesta_basica_sorocabana.application.exceptions;

public class ForbiddenActionException extends RuntimeException {

    public ForbiddenActionException(String message) {
        super(message);
    }
}
