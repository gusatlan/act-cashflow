package br.com.act.cashflow.mappers;

import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import br.com.act.platform.model.cashflow.ItemCashFlow;

public final class ItemCashFlowMapper {

    public static ItemCashFlow transform(final br.com.act.cashflow.model.cassandra.ItemCashFlow value) {
        return new ItemCashFlow.Builder()
                .withDate(value.getId().getTime())
                .withType(value.getId().getType())
                .withDescription(value.getDescription())
                .withValue(value.getValue())
                .build();
    }

    public static br.com.act.cashflow.model.cassandra.ItemCashFlow transform(final ItemCashFlow value) {
        return new br.com.act.cashflow.model.cassandra.ItemCashFlow.Builder()
                .withId(
                        new ItemCashFlowId.Builder()
                                .withType(value.getType())
                                .withTime(value.getDate())
                                .build()
                )
                .withDescription(value.getDescription())
                .withValue(value.getValue())
                .build();
    }
}
