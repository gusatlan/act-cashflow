package br.com.act.cashflow.services;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.repositories.ItemCashFlowRepository;
import br.com.act.platform.model.enums.ActionType;
import br.com.act.platform.model.exceptions.EntityInvalidException;
import br.com.act.platform.model.request.RequestAction;
import br.com.act.platform.util.DateUtils;
import br.com.act.platform.util.ValidationUtils;
import org.slf4j.Logger;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;

@Service
public class ItemCashFlowService {
    private final ItemCashFlowRepository repository;
    private final Logger logger;
    private final StreamBridge streamBridge;

    public ItemCashFlowService(
            final ItemCashFlowRepository repository,
            final Logger logger,
            final StreamBridge streamBridge
    ) {
        this.repository = repository;
        this.logger = logger;
        this.streamBridge = streamBridge;
    }

    public Flux<ItemCashFlow> find(final LocalDate date) {
        return repository.findByIdDate(date);
    }

    public Flux<ItemCashFlow> find(final LocalDateTime begin, final LocalDateTime end) {
        return repository.findByIdTimeBetween(DateUtils.atStartOfDay(begin), DateUtils.atEndOfDay(end));
    }

    public Flux<ItemCashFlow> find(final LocalDate begin, final LocalDate end) {
        return find(begin.atStartOfDay(), DateUtils.atEndOfDay(end.atStartOfDay()));
    }


    private Mono<ItemCashFlow> upsert(final ItemCashFlow value) {
        return Mono.just(value)
                .filter(ItemCashFlow::isValid)
                .switchIfEmpty(Mono.error(new EntityInvalidException(ValidationUtils.validate(value))))
                .flatMap(repository::save)
                .doOnNext(it ->
                        logger.debug("[ItemCashFlow] add {}", it)
                );
    }

    private Mono<Void> remove(final ItemCashFlowId id) {
        return Mono.just(id)
                .doOnNext(it ->
                        logger.debug("[ItemCashFlow] deleted {}", it)
                )
                .flatMap(repository::deleteById)
                .then();
    }

    private Flux<ItemCashFlow> upsertStream(RequestAction<ItemCashFlow>... values) {
        return Flux.fromIterable(Arrays.asList(values))
                .filter(request ->
                        request.getEntity() != null && request.getEntity().isValid() && request.getAction() != null
                )
                .flatMap(request -> {
                            final ItemCashFlow item = request.getEntity();
                            final ActionType action = request.getAction();

                            if (action.equals(ActionType.UPSERT)) {
                                return upsert(item);
                            } else {
                                return remove(item.getId())
                                        .thenReturn(item);
                            }
                        }
                )
                .doOnNext(it -> logger.debug("[ItemCashFlow] {} saved", it))
                .doOnError(it -> logger.error("[ItemCashFlow] not saved", it));
    }

    public void send(final RequestAction<br.com.act.platform.model.cashflow.ItemCashFlow> request) {
        RequestAction<ItemCashFlow> req = new RequestAction.Builder<ItemCashFlow>()
                .withAction(request.getAction())
                .withEntity(ItemCashFlowMapper.transform(request.getEntity()))
                .build();

        streamBridge.send("upsertItemCashFlow-in-0", req);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Function<Flux<RequestAction<ItemCashFlow>>, Mono<Void>> upsertItemCashFlow() {
        return request -> request.doOnNext(it ->
                        logger.debug("[RECEIVED] ItemCashFlow")
                ).flatMap(this::upsertStream)
                .onErrorResume(it -> {
                            logger.error("[ERROR] ItemCashFlow", it);
                            return Flux.empty();
                        }
                )
                .then();
    }
}
