package br.com.act.cashflow.services;

import br.com.act.platform.model.cashflow.ReportCashFlow;
import br.com.act.platform.model.request.RequestCashFlowReport;
import br.com.act.platform.util.DateUtils;
import br.com.act.platform.util.EncodeUtils;
import br.com.act.platform.util.ReportUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {
    private final ItemCashFlowService service;
    private final ItemCashFlowPersistService reportService;
    private final Logger logger;
    private final String reportFilename;

    private final DataSource ds;

    public ReportService(
            final ItemCashFlowService service,
            final ItemCashFlowPersistService reportService,
            final Logger logger,
            @Value("${report.filename}") final String reportFilename,
            final DataSource ds
    ) {
        this.service = service;
        this.reportService = reportService;
        this.logger = logger;
        this.reportFilename = reportFilename;
        this.ds = ds;
    }

    private Optional<String> createReport(final LocalDateTime beginAt, final LocalDateTime endAt) {
        final LocalDateTime begin = beginAt != null ? beginAt : DateUtils.now();
        final LocalDateTime end = endAt != null ? endAt : DateUtils.now();
        Map<String, Object> params = new HashMap<>();
        Optional<String> report = Optional.empty();

        params.put("BEGIN_DATE", DateUtils.convert(DateUtils.atStartOfDay(begin)));
        params.put("END_DATE", DateUtils.convert(DateUtils.atEndOfDay(end)));

        try (Connection connection = ds.getConnection()){
            final byte[] reportBin = ReportUtils.exportReport(ReportUtils.getReportFile(reportFilename), params, connection);
            report = Optional.of(EncodeUtils.encodeBase64(reportBin));
        } catch (Exception e) {
            logger.error("createReport", e);
        }

        return report;
    }

    public Mono<ReportCashFlow> generateReport(final RequestCashFlowReport request) {
        return service.find(request.getBeginDate(), request.getEndDate())
                .map(reportService::save)
                .collectList()
                .flatMap(items -> {
                            final Optional<String> report = createReport(request.getBeginDate().atStartOfDay(), request.getEndDate().atStartOfDay());
                            final ReportCashFlow reportCashFlow = new ReportCashFlow.Builder()
                                    .withRequest(request)
                                    .withContent(report.orElse(null))
                                    .build();

                            reportService.remove(request.getBeginDate().atStartOfDay(), DateUtils.atEndOfDay(request.getEndDate().atStartOfDay())).then().subscribe();

                            return Mono.just(reportCashFlow);
                        }
                );
    }
}
