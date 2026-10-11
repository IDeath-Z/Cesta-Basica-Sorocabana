package br.uniso.cesta_basica_sorocabana.infrastructure.enums;

public enum AuthScheme {

    BEARER("bearer", "Bearer ", "bearerAuth", "JWT");

    private final String scheme;
    private final String headerPrefix;
    private final String securitySchemeName;
    private final String tokenFormat;

    AuthScheme(String scheme, String headerPrefix, String securitySchemeName, String tokenFormat) {
        this.scheme = scheme;
        this.headerPrefix = headerPrefix;
        this.securitySchemeName = securitySchemeName;
        this.tokenFormat = tokenFormat;
    }

    public String getScheme() { return scheme; }
    public String getHeaderPrefix() { return headerPrefix; }
    public String getSecuritySchemeName() { return securitySchemeName; }
    public String getTokenFormat() { return tokenFormat; }
}
