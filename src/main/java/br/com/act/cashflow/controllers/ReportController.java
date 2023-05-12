package br.com.act.cashflow.controllers;

import br.com.act.cashflow.services.ReportService;
import br.com.act.platform.model.cashflow.ReportCashFlow;
import br.com.act.platform.model.request.RequestCashFlowReport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReportController {
    private final ReportService service;

    public ReportController(final ReportService service) {
        this.service = service;
    }

    @PostMapping("/report")
    public Mono<ReportCashFlow> generate(@RequestBody final RequestCashFlowReport request) {
        return service.generateReport(request);
    }
}
