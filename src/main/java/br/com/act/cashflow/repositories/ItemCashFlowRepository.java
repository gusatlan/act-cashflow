package br.com.act.cashflow.repositories;

import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ItemCashFlowRepository extends ReactiveCassandraRepository<ItemCashFlow, ItemCashFlowId> {

    @AllowFiltering
    Flux<ItemCashFlow> findByIdDate(final LocalDate date);

    @AllowFiltering
    Flux<ItemCashFlow> findByIdTimeBetween(final LocalDateTime begin, final LocalDateTime end);
}
