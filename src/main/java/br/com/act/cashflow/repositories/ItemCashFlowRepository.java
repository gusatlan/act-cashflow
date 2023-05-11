package br.com.act.cashflow.repositories;

import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ItemCashFlowRepository extends ReactiveCassandraRepository<ItemCashFlow, ItemCashFlowId> {

    @AllowFiltering
    public Flux<ItemCashFlow> findByIdDate(final LocalDate date);
}
