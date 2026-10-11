package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateStoreUseCase {

    private final StoreRepository storeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String name) {}

    public StoreOutput execute(Input input) {
        if (storeRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(StoreApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        StoreOutput output = StoreOutput.from(storeRepository.save(Store.createNew(input.name())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.STORE, output.id(), output);
        return output;
    }
}
