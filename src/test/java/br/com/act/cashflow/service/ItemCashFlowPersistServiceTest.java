package br.com.act.cashflow.service;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import br.com.act.cashflow.repositories.ItemCashFlowPersistRepository;
import br.com.act.cashflow.services.ItemCashFlowPersistService;
import br.com.act.platform.model.enums.ItemCashFlowType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

class ItemCashFlowPersistServiceTest {

    private final ItemCashFlowPersistRepository repository = Mockito.mock(ItemCashFlowPersistRepository.class);
    private final Logger logger = LoggerFactory.getLogger("Test");
    private final LocalDateTime date = LocalDateTime.of(2023, 5, 16, 8, 44, 0);

    private ItemCashFlowPersistService generateService() {
        return new ItemCashFlowPersistService(repository, logger);
    }

    private ItemCashFlow generateItemValid() {
        return new ItemCashFlow.Builder()
                .withId(
                        new ItemCashFlowId.Builder()
                                .withTime(date)
                                .withType(ItemCashFlowType.CREDIT)
                                .build()
                )
                .withDescription("Valid CREDIT")
                .withValue(1234.99)
                .build();
    }

    private ItemCashFlow generateItemInvalid() {
        return new ItemCashFlow.Builder().build();
    }

    private ItemCashFlowPersist generateItem() {
        return new ItemCashFlowPersist.Builder()
                .withDate(date)
                .withType(ItemCashFlowType.CREDIT)
                .withDescription("Description")
                .withValue(12.45)
                .withId(1L)
                .build();
    }

    @Test
    void shouldSave() {
        final ItemCashFlowPersistService service = generateService();
        final ItemCashFlow validItem = generateItemValid();
        final ItemCashFlow invalidItem = generateItemInvalid();

        Assertions.assertTrue(validItem.isValid());
        Assertions.assertFalse(invalidItem.isValid());

        Mockito.when(
                        repository.save(Mockito.any()))
                .thenReturn(ItemCashFlowMapper.transformItemCashFlowPersist(validItem));

        Assertions.assertTrue(service.save(validItem).isPresent());
        Assertions.assertFalse(service.save(invalidItem).isPresent());
    }

    @Test
    void shouldFind() {
        final ItemCashFlowPersistService service = generateService();
        final ItemCashFlowPersist item = generateItem();

        Mockito.when(repository.findByPeriod(Mockito.any(), Mockito.any())).thenReturn(List.of(item));

        StepVerifier.create(service.find(date, date))
                .expectNext(item)
                .expectComplete();
    }

    @Test
    void shouldRemove() {
        final ItemCashFlowPersistService service = generateService();
        final ItemCashFlowPersist item = generateItem();

        Mockito.when(repository.findByPeriod(Mockito.any(), Mockito.any())).thenReturn(List.of(item));

        StepVerifier.create(service.remove(date, date))
                .expectNextCount(1L)
                .expectComplete();
    }
}
