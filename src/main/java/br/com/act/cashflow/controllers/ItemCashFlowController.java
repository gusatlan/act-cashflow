package br.com.act.cashflow.controllers;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.platform.model.cashflow.ItemCashFlow;
import br.com.act.platform.model.request.RequestAction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
public class ItemCashFlowController {
    private final ItemCashFlowService service;

    public ItemCashFlowController(final ItemCashFlowService service) {
        this.service = service;
    }

    @GetMapping("/item-cash-flow")
    public Flux<ItemCashFlow> find(@RequestParam(name = "date", required = true) final LocalDate date) {
        return service.find(date).map(ItemCashFlowMapper::transform);
    }

    @PostMapping("/item-cash-flow")
    @PutMapping("/item-cash-flow")
    @DeleteMapping("/item-cash-flow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void upsert(@RequestBody final RequestAction<ItemCashFlow> value) {
        service.send(value);
    }

}
