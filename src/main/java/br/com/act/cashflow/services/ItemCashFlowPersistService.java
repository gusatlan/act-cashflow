package br.com.act.cashflow.services;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import br.com.act.cashflow.repositories.ItemCashFlowPersistRepository;
import br.com.act.platform.util.DateUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemCashFlowPersistService {
    private final ItemCashFlowPersistRepository repository;
    private final Logger logger;

    public ItemCashFlowPersistService(
            final ItemCashFlowPersistRepository repository,
            final Logger logger
    ) {
        this.repository = repository;
        this.logger = logger;
    }

    public Optional<ItemCashFlowPersist> save(final ItemCashFlow value) {
        Optional<ItemCashFlowPersist> saved = Optional.empty();

        if (value != null && value.isValid()) {
            try {
                saved = Optional.of(repository.save(ItemCashFlowMapper.transformItemCashFlowPersist(value)));
                logger.debug("[ItemCashFlowPersist] {} saved", saved.get().getId());
            } catch (Exception e) {
                logger.error("[ItemCashFlowPersist] not saved", e);
            }
        }

        return saved;
    }

    public Flux<ItemCashFlowPersist> find(final LocalDateTime begin, final LocalDateTime end) {
        return Flux.fromIterable(repository.findByPeriod(DateUtils.atStartOfDay(begin), DateUtils.atEndOfDay(end)));
    }

    public Flux<Void> remove(final LocalDateTime begin, final LocalDateTime end) {
        return find(begin, end)
                .flatMap(item -> {
                    repository.delete(item);
                    return Mono.empty();
                });
    }
}
