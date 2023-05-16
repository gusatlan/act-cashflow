package br.com.act.cashflow.service;

import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.repositories.ItemCashFlowRepository;
import br.com.act.cashflow.services.CashFlowService;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.platform.model.enums.ItemCashFlowType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

class CashFlowServiceTest {
    private final Logger logger = LoggerFactory.getLogger("Test");
    private final ItemCashFlowRepository repository = Mockito.mock(ItemCashFlowRepository.class);
    private final ItemCashFlowService itemCashFlowService = new ItemCashFlowService(repository, logger, null);
    private final LocalDate date = LocalDate.of(2023, 5, 16);

    private CashFlowService generateService() {
        return new CashFlowService(itemCashFlowService, logger);
    }

    private Flux<ItemCashFlow> generateItems() {
        Collection<ItemCashFlow> items = new HashSet<>();
        LocalDateTime date = this.date.atStartOfDay();

        for (int i = 0; i < 100; i++) {
            items.add(new ItemCashFlow.Builder()
                    .withId(
                            new ItemCashFlowId.Builder()
                                    .withTime(date = date.plusMinutes(1L))
                                    .withType(i % 2 == 0 ? ItemCashFlowType.CREDIT : ItemCashFlowType.DEBIT)
                                    .build()
                    )
                    .withDescription(String.format("Description %s", i))
                    .withValue(1000.0 * Math.random())
                    .build()
            );
        }

        return Flux.fromIterable(items);
    }

    @Test
    void shouldGenerateCashFlow() {
        final CashFlowService service = generateService();

        Mockito.when(repository.findByIdTimeBetween(Mockito.any(), Mockito.any())).thenReturn(generateItems());

        StepVerifier.create(service.generateCashFlow(date, date))
                .expectNextCount(1L)
                .expectComplete();
    }
}
