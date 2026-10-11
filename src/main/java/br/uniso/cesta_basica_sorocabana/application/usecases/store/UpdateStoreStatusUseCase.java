package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.StoreApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateStoreStatusUseCase {

    private final StoreRepository storeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public StoreOutput execute(Input input) {
        Store store = storeRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(StoreApplicationError.NOT_FOUND.message(input.id())));

        Store changed = input.active() ? store.activate() : store.deactivate();
        if (changed == store)
            return StoreOutput.from(store);

        StoreOutput before = StoreOutput.from(store);
        StoreOutput after = StoreOutput.from(storeRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.STORE, after.id(), before, after);
        return after;
    }
}
