package br.com.act.cashflow.services;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.platform.model.cashflow.CashFlow;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CashFlowService {
    private final ItemCashFlowService service;
    private final Logger logger;

    public CashFlowService(
            final ItemCashFlowService service,
            final Logger logger
    ) {
        this.service = service;
        this.logger = logger;
    }

    private Map<LocalDate, List<ItemCashFlow>> toMap(final Collection<ItemCashFlow> items) {
        return items
                .stream()
                .collect(Collectors.groupingBy(item -> item.getId().getDate(), Collectors.toList()));
    }

    private Collection<CashFlow> toCashFlow(final Map<LocalDate, List<ItemCashFlow>> map) {
        return map
                .entrySet()
                .stream()
                .map(entry ->
                        new CashFlow.Builder()
                                .withDate(entry.getKey())
                                .withItems(
                                        entry.getValue().stream().map(ItemCashFlowMapper::transform).toList()
                                )
                                .build()
                ).toList();
    }

    public Flux<CashFlow> generateCashFlow(final LocalDate beginAt, final LocalDate endAt) {
        return service.find(beginAt, endAt)
                .collectList()
                .map(this::toMap)
                .flatMapIterable(this::toCashFlow)
                .doOnNext(it ->
                        logger.debug("[CashFlow] generateCashFlow {}", it)
                );
    }
}
