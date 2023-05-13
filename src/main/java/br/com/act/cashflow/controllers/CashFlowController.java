package br.com.act.cashflow.controllers;

import br.com.act.cashflow.services.CashFlowService;
import br.com.act.platform.model.cashflow.CashFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
public class CashFlowController {
    private final CashFlowService service;

    public CashFlowController(final CashFlowService service) {
        this.service = service;
    }

    @GetMapping("/cashflow")
    public Flux<CashFlow> find(
            @RequestParam(name = "begin", required = true) LocalDate begin,
            @RequestParam(name = "end", required = true) LocalDate end) {
        return service.generateCashFlow(begin, end);
    }
}
