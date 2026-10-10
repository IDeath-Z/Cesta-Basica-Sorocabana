package br.uniso.cesta_basica_sorocabana.domain.enums;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.AuditError;

public enum AuditAction {

    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String code;

    AuditAction(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AuditAction fromCode(String code) {
        for (AuditAction action : AuditAction.values()) {
            if (action.code.equals(code)) {
                return action;
            }
        }
        throw new IllegalArgumentException(AuditError.INVALID_ACTION.message(code));
    }
}
