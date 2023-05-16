package br.com.act.cashflow.service;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import br.com.act.cashflow.repositories.ItemCashFlowPersistRepository;
import br.com.act.cashflow.repositories.ItemCashFlowRepository;
import br.com.act.cashflow.services.ItemCashFlowPersistService;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.cashflow.services.ReportService;
import br.com.act.platform.model.enums.ItemCashFlowType;
import br.com.act.platform.model.request.RequestCashFlowReport;
import br.com.act.platform.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

public class ReportServiceTest {
    private final Logger logger = LoggerFactory.getLogger("Test");
    private final String reportFilename = "cash_flow.jasper";
    private final ItemCashFlowRepository itemCashFlowRepository = Mockito.mock(ItemCashFlowRepository.class);
    private final ItemCashFlowPersistRepository itemCashFlowPersistRepository = Mockito.mock(ItemCashFlowPersistRepository.class);
    private final ItemCashFlowService itemCashFlowService = new ItemCashFlowService(itemCashFlowRepository, logger, null);
    private final ItemCashFlowPersistService itemCashFlowPersistService = new ItemCashFlowPersistService(itemCashFlowPersistRepository, logger);
    private final ReportService reportService = new ReportService(
            itemCashFlowService,
            itemCashFlowPersistService,
            logger,
            reportFilename,
            null
    );


    private ItemCashFlow generateItem() {
        return new ItemCashFlow.Builder()
                .withId(
                        new ItemCashFlowId.Builder()
                                .withTime(DateUtils.now())
                                .withType(ItemCashFlowType.CREDIT)
                                .build()
                )
                .withDescription("Description")
                .withValue(12.77)
                .build();
    }

    private RequestCashFlowReport generateRequest() {
        final LocalDate date = LocalDate.now();
        return new RequestCashFlowReport.Builder()
                .withRequestId("abc")
                .withBeginDate(date)
                .withEndDate(date)
                .build();
    }

    @Test
    void shouldGenerateFakeReport() {
        final ItemCashFlow item = generateItem();
        final ItemCashFlowPersist itemPersist = ItemCashFlowMapper.transformItemCashFlowPersist(item);


        Mockito.when(itemCashFlowRepository.findByIdTimeBetween(Mockito.any(), Mockito.any()))
                .thenReturn(Flux.just(item));

        Mockito.when(itemCashFlowPersistRepository.save(Mockito.any()))
                .thenReturn(itemPersist);

        Mockito.when(itemCashFlowPersistRepository.findByPeriod(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(itemPersist));

        StepVerifier.create(reportService.generateReport(generateRequest()))
                .expectNextCount(1L)
                .expectComplete();
    }
}
