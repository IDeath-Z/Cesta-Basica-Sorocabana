package br.uniso.cesta_basica_sorocabana.domain.ports;

// Turns a snapshot of a record into JSON for the audit history.
public interface DataSerializer {

    String toJson(Object value);
}
