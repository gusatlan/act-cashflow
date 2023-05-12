package br.com.act.cashflow.model;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.platform.model.enums.ItemCashFlowType;
import br.com.act.platform.util.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ItemCashFlowTest {

    private ItemCashFlowId generateId() {
        return new ItemCashFlowId.Builder()
                .withTime(DateUtils.now())
                .withType(ItemCashFlowType.CREDIT)
                .build();
    }

    private ItemCashFlow generateItemCashFlow(final ItemCashFlowId id) {
        return new ItemCashFlow.Builder()
                .withId(id)
                .withDescription("A Item's description")
                .withValue(new BigDecimal("1999.97"))
                .build();
    }

    @Test
    void shouldMapper() {
        final ItemCashFlowId id = generateId();
        final ItemCashFlow item = generateItemCashFlow(id);
        final br.com.act.platform.model.cashflow.ItemCashFlow itemMapped = ItemCashFlowMapper.transform(item);
        final ItemCashFlow itemUnmapped = ItemCashFlowMapper.transform(itemMapped);

        Assertions.assertEquals(item, itemUnmapped);
        Assertions.assertEquals(id, itemUnmapped.getId());

        Assertions.assertEquals(item.getId().getTime(), itemMapped.getDate());
        Assertions.assertEquals(item.getId().getType(), itemMapped.getType());
        Assertions.assertEquals(item.getDescription().toUpperCase(), itemMapped.getDescription().toUpperCase());
        Assertions.assertEquals(item.getValue(), itemMapped.getValue());

        Assertions.assertEquals(itemUnmapped.getId().getTime(), itemMapped.getDate());
        Assertions.assertEquals(itemUnmapped.getId().getType(), itemMapped.getType());
        Assertions.assertEquals(itemUnmapped.getDescription().toUpperCase(), itemMapped.getDescription().toUpperCase());
        Assertions.assertEquals(itemUnmapped.getValue(), itemMapped.getValue());

    }
}
