package br.com.act.cashflow.controllers;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.platform.model.cashflow.ItemCashFlow;
import br.com.act.platform.model.request.RequestAction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
public class ItemCashFlowController {
    private final ItemCashFlowService service;

    public ItemCashFlowController(final ItemCashFlowService service) {
        this.service = service;
    }

    @GetMapping("/item-cash-flow")
    public Flux<ItemCashFlow> find(@RequestParam(name="date", required = true) final LocalDate date) {
        return service.find(date).map(ItemCashFlowMapper::transform);
    }

    @PostMapping("item-cash-flow")
    public Mono<ItemCashFlow> upsert(final RequestAction<ItemCashFlow> value) {
        //TODO Implementar o save para o cassandra
        return Mono.empty();
    }

}
