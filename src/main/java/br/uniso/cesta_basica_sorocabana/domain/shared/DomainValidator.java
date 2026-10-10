package br.uniso.cesta_basica_sorocabana.domain.shared;

import java.math.BigDecimal;

// This class is like Objects.requireNonNull(), but with custom methods to validate domain entities and throw IllegalArgumentException with custom messages.
public final class DomainValidator {

    private DomainValidator() {}

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int requireNonNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static BigDecimal requireNonNegative(BigDecimal value, String message) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static BigDecimal requirePositive(BigDecimal value, String message) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
