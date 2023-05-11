package br.com.act.cashflow.model.cassandra;

import br.com.act.cashflow.model.cassandra.ids.ItemCashFlowId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;

@Table("items_cash_flows")
public final class ItemCashFlow implements Comparable<ItemCashFlow> {

    @PrimaryKey
    @NotNull(message = "Id not be null")
    private ItemCashFlowId id;

    @Column("description")
    @NotNull(message = "Description not be null")
    @NotBlank(message = "Description not be blank")
    private String description;

    @Column("value")
    @NotNull(message = "Value not be null")
    @Min(message = "Value must be positive", value = 0)
    private BigDecimal value;

    public ItemCashFlowId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Transient
    public boolean isValid() {
        return id != null && id.isValid() && !description.isBlank() && value != null && value.doubleValue() >= 0D;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemCashFlow other && id != null && other.getId() != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(final ItemCashFlow other) {
        return id != null && other != null ? id.compareTo(other.getId()) : 0;
    }

    @Override
    public String toString() {
        return String.format("{\"id\": %s, \"description\": \"%s\", \"value\": %s}", id, description, value);
    }

    public ItemCashFlow() {
    }

    private ItemCashFlow(final ItemCashFlowId id, final String description, final BigDecimal value) {
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public static class Builder {
        private ItemCashFlowId id;
        private String description;
        private BigDecimal value;

        public Builder withId(final ItemCashFlowId id) {
            this.id = id;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withValue(final BigDecimal value) {
            this.value = value;
            return this;
        }

        public ItemCashFlow build() {
            return new ItemCashFlow(id, description, value);
        }
    }
}
