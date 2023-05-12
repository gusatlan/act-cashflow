package br.com.act.cashflow.model.sql;

import br.com.act.platform.model.enums.ItemCashFlowType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_cash_flow", uniqueConstraints = @UniqueConstraint(name = "udx_item_cash_flow", columnNames = "event_time"))
public final class ItemCashFlowPersist implements Comparable<ItemCashFlowPersist> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_time")
    private LocalDateTime date;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItemCashFlowType type;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ItemCashFlowType getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemCashFlowPersist other && id != null && other.getId() != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int compareTo(final ItemCashFlowPersist other) {
        return date != null && other != null && other.getDate() != null ? date.compareTo(other.getDate()) : 0;
    }

    public ItemCashFlowPersist() {
    }

    private ItemCashFlowPersist(
            final Long id,
            final LocalDateTime date,
            final ItemCashFlowType type,
            final BigDecimal value,
            final String description
    ) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    public static class Builder {
        private Long id;
        private LocalDateTime date;
        private ItemCashFlowType type;
        private BigDecimal value;
        private String description;

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withDate(final LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder withType(final ItemCashFlowType type) {
            this.type = type;
            return this;
        }

        public Builder withValue(final BigDecimal value) {
            this.value = value;
            return this;
        }

        public Builder withValue(final Double value) {
            return withValue(new BigDecimal(value.toString()));
        }

        public Builder withDescription(final String description) {
            this.description = description != null ? description.trim().toUpperCase() : "-";
            return this;
        }

        public ItemCashFlowPersist build() {
            return new ItemCashFlowPersist(
                    id,
                    date,
                    type,
                    value,
                    description
            );
        }
    }
}
