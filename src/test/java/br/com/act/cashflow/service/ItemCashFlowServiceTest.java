package br.com.act.cashflow.service;

import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.repositories.ItemCashFlowRepository;
import br.com.act.cashflow.services.ItemCashFlowService;
import br.com.act.platform.model.enums.ActionType;
import br.com.act.platform.model.enums.ItemCashFlowType;
import br.com.act.platform.model.request.RequestAction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

class ItemCashFlowServiceTest {
    private final Logger logger = LoggerFactory.getLogger("Test");
    private final ItemCashFlowRepository repository = Mockito.mock(ItemCashFlowRepository.class);
    private final LocalDateTime date = LocalDateTime.of(2023, 5, 16, 8, 44, 0);

    private Flux<ItemCashFlow> generateItems() {
        Collection<ItemCashFlow> items = new HashSet<>();
        LocalDateTime date = this.date;

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

    private ItemCashFlow generateItem() {
        return new ItemCashFlow.Builder()
                .withId(
                        new ItemCashFlowId.Builder()
                                .withTime(date)
                                .withType(ItemCashFlowType.CREDIT)
                                .build()
                )
                .withDescription("Description 001")
                .withValue(906.865)
                .build();
    }

    private br.com.act.platform.model.cashflow.ItemCashFlow generateItemDTO() {
        return new br.com.act.platform.model.cashflow.ItemCashFlow.Builder()
                .withDate(date)
                .withType(ItemCashFlowType.CREDIT)
                .withDescription("Description 001")
                .withValue(906.865)
                .build();
    }


    private ItemCashFlowService generateService() {
        return new ItemCashFlowService(repository, logger, null);
    }

    @Test
    void shouldRetrieveItems() {
        ItemCashFlowService service = generateService();

        Mockito.when(repository.findByIdDate(Mockito.any())).thenReturn(generateItems());
        Mockito.when(repository.findByIdTimeBetween(Mockito.any(), Mockito.any())).thenReturn(generateItems());

        StepVerifier.create(service.find(date.toLocalDate()))
                .expectNextCount(100L)
                .expectComplete();

        StepVerifier.create(service.find(date, date))
                .expectNextCount(100L)
                .expectComplete();
    }

    @Test
    void shouldSaveAndRemove() {
        ItemCashFlowService service = generateService();
        ItemCashFlow item = generateItem();

        Mockito.when(repository.save(Mockito.any())).thenReturn(Mono.just(item));
        Mockito.when(repository.deleteById((ItemCashFlowId) Mockito.any())).thenReturn(Mono.empty());

        StepVerifier.create(service.upsert(item))
                .expectNext(item)
                .expectComplete();

        StepVerifier.create(service.remove(item.getId()))
                .expectNextCount(1L)
                .expectComplete();
    }

    @Test
    void shouldUpsertStream() {
        ItemCashFlowService service = generateService();
        ItemCashFlow item = generateItem();
        RequestAction<ItemCashFlow> request = new RequestAction.Builder<ItemCashFlow>()
                .withAction(ActionType.UPSERT)
                .withEntity(item)
                .build();

        Mockito.when(repository.save(Mockito.any())).thenReturn(Mono.just(item));

        StepVerifier.create(service.upsertStream(request))
                .expectNext(item)
                .expectComplete();
    }

}
