package br.uniso.cesta_basica_sorocabana.infrastructure.serialization;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.ports.DataSerializer;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public final class JacksonDataSerializer implements DataSerializer {

    private final JsonMapper jsonMapper;

    @Override
    public String toJson(Object value) {
        return jsonMapper.writeValueAsString(value);
    }
}
