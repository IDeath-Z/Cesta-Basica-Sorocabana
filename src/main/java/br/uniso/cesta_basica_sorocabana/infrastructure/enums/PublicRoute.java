package br.uniso.cesta_basica_sorocabana.infrastructure.enums;

// Routes accessible without authentication. A null method means any HTTP method.
// Versioned routes are prefixed with the API base path (app.api.base-path).
public enum PublicRoute {

    API_DOCS(null, "/v3/api-docs", false),
    API_DOCS_ALL(null, "/v3/api-docs/**", false),
    API_DOCS_YAML(null, "/v3/api-docs.yaml", false),
    SWAGGER_UI(null, "/swagger-ui/**", false),
    SWAGGER_UI_HTML(null, "/swagger-ui.html", false),
    ERROR(null, "/error", false),
    AUTH_LOGIN("POST", "/auth/login", true);

    private final String method;
    private final String path;
    private final boolean versioned;

    PublicRoute(String method, String path, boolean versioned) {
        this.method = method;
        this.path = path;
        this.versioned = versioned;
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public boolean isVersioned() { return versioned; }
}
