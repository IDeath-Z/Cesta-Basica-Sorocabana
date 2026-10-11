package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateStoreUseCase {

    private final StoreRepository storeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, String name) {}

    public StoreOutput execute(Input input) {
        Store store = storeRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(StoreApplicationError.NOT_FOUND.message(input.id())));

        if (!store.getName().equals(input.name()) && storeRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(StoreApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        StoreOutput before = StoreOutput.from(store);
        StoreOutput after = StoreOutput.from(storeRepository.save(store.rename(input.name())));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.STORE, after.id(), before, after);
        return after;
    }
}
