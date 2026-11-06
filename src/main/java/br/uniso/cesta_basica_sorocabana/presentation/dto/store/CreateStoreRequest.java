package br.uniso.cesta_basica_sorocabana.presentation.dto.store;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateStoreRequest", description = "Payload for creating a new store")
public record CreateStoreRequest(
    @NotBlank @Size(max = 120) @Schema(description = "Store name, must be unique", example = "Tauste")
    String name,

    @URL @Size(max = 2048) @Schema(description = "Store website, needed only to search on the site", example = "https://tauste.com.br/sorocaba1")
    String storeUrl
) {}
