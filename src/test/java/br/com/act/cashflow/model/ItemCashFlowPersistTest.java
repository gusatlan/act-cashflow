package br.com.act.cashflow.model;

import br.com.act.cashflow.mappers.ItemCashFlowMapper;
import br.com.act.cashflow.model.cassandra.ItemCashFlow;
import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import br.com.act.platform.model.enums.ItemCashFlowType;
import br.com.act.platform.util.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ItemCashFlowPersistTest {

    private ItemCashFlow generateItemCashFlow() {
        final ItemCashFlowId id = new ItemCashFlowId.Builder()
                .withType(ItemCashFlowType.DEBIT)
                .withTime(DateUtils.now())
                .build();

        return new ItemCashFlow.Builder()
                .withId(id)
                .withDescription("A description here")
                .withValue(new BigDecimal("88864.88"))
                .build();
    }

    @Test
    void shouldMapper() {
        final ItemCashFlow item = generateItemCashFlow();
        final ItemCashFlowPersist itemPersist = ItemCashFlowMapper.transformItemCashFlowPersist(item);

        Assertions.assertNull(itemPersist.getId());
        Assertions.assertEquals(item.getId().getTime(), itemPersist.getDate());
        Assertions.assertEquals(item.getId().getType(), itemPersist.getType());
        Assertions.assertEquals(item.getValue(), itemPersist.getValue());
        Assertions.assertEquals(item.getDescription().toUpperCase(), itemPersist.getDescription().toUpperCase());
    }
}
