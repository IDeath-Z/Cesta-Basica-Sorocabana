package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchStoresUseCase {

    private final StoreRepository storeRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<StoreOutput> execute(Input input) {
        return storeRepository.findAll(input.pageRequest()).map(StoreOutput::from);
    }
}
