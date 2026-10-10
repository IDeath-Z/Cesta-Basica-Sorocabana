package br.uniso.cesta_basica_sorocabana.domain.enums;

// Source of truth for permissions: constants are synchronized to the permission table on startup.
// Roles only group permissions; access control always checks the permission.
public enum Permission {

    // Users, roles and permissions are managed only by holders of ADMIN.
    ADMIN("Administrar usuários e funções"),

    CREATE_SURVEY_TYPE("Cadastrar tipo de coleta"),
    UPDATE_SURVEY_TYPE("Editar, ativar e desativar tipo de coleta"),

    CREATE_UNIT("Cadastrar unidade"),
    UPDATE_UNIT("Editar, ativar e desativar unidade"),

    CREATE_STORE("Cadastrar mercado"),
    UPDATE_STORE("Editar, ativar e desativar mercado"),

    CREATE_CATEGORY("Cadastrar categoria"),
    UPDATE_CATEGORY("Editar, ativar e desativar categoria"),

    CREATE_BRAND("Cadastrar marca"),
    UPDATE_BRAND("Editar, ativar e desativar marca"),

    CREATE_PRODUCT("Cadastrar produto"),
    UPDATE_PRODUCT("Editar, ativar e desativar produto"),

    CREATE_PRODUCT_BRAND("Vincular marca a produto"),
    UPDATE_PRODUCT_BRAND("Ativar e desativar vínculo de marca com produto"),

    CREATE_SURVEY("Cadastrar coleta"),
    UPDATE_SURVEY("Editar coleta"),
    DELETE_SURVEY("Excluir coleta"),
    PUBLISH_SURVEY("Publicar coleta"),

    CREATE_SURVEY_ITEM("Adicionar levantamento à coleta"),
    DELETE_SURVEY_ITEM("Remover levantamento da coleta"),
    REGISTER_SURVEY_ITEM_PRICE("Registrar preço do levantamento"),
    DELETE_SURVEY_ITEM_PRICE("Remover preço do levantamento"),

    CALCULATE_RESULT("Calcular resultado do levantamento"),

    VIEW_AUDIT("Consultar auditoria");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
