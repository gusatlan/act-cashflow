package br.com.act.cashflow.services;

import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import br.com.act.platform.model.cashflow.ReportCashFlow;
import br.com.act.platform.model.exceptions.ApplicationException;
import br.com.act.platform.model.request.RequestCashFlowReport;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReportService {
    private final ItemCashFlowService service;
    private final ItemCashFlowPersistService reportService;
    private final Logger logger;

    public ReportService(
            final ItemCashFlowService service,
            final ItemCashFlowPersistService reportService,
            final Logger logger
    ) {
        this.service = service;
        this.reportService = reportService;
        this.logger = logger;
    }


//    public ReportCashFlow generateReport(final RequestCashFlowReport request) {
//        service.find(request.getBeginDate())
//                .map(reportService::save)
//                .collectList();
//
//    }
}
