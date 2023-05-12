package br.com.act.cashflow.controllers;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.platform.model.cashflow.ItemCashFlow;
import br.com.act.platform.model.exceptions.ApplicationException;
import br.com.act.platform.model.request.RequestAction;
import br.com.act.platform.util.DateUtils;
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
    public Flux<ItemCashFlow> find(
            @RequestParam(name = "date", required = false) final LocalDate date,
            @RequestParam(name = "begin", required = false) final String begin,
            @RequestParam(name = "end", required = false) final String end
    ) throws ApplicationException {
        if (date != null) {
            return service.find(date).map(ItemCashFlowMapper::transform);
        } else if (begin != null && end != null) {
            return service.find(DateUtils.convert(begin), DateUtils.convert(end)).map(ItemCashFlowMapper::transform);
        } else {
            throw new ApplicationException("Filter not exists");
        }
    }

    @PostMapping("/item-cash-flow")
    @PutMapping("/item-cash-flow")
    @DeleteMapping("/item-cash-flow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void upsert(@RequestBody final RequestAction<ItemCashFlow> value) {
        service.send(value);
    }
}
