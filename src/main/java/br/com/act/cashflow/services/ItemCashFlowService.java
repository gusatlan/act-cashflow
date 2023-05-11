package br.com.act.cashflow.services;

import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.repositories.ItemCashFlowRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class ItemCashFlowService {
    private final ItemCashFlowRepository repository;

    public ItemCashFlowService(final ItemCashFlowRepository repository) {
        this.repository = repository;
    }

    public Flux<ItemCashFlow> find(final LocalDate date) {
        return repository.findByIdDate(date);
    }
}
