package br.com.act.cashflow.model.cassandra.ids;

import br.com.act.platform.model.enums.ItemCashFlowType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDate;
import java.time.LocalDateTime;

@PrimaryKeyClass
public final class ItemCashFlowId implements Comparable<ItemCashFlowId> {

    @PrimaryKeyColumn(name = "event_date", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private LocalDate date;

    @PrimaryKeyColumn(name = "event_datetime", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private LocalDateTime time;


    @PrimaryKeyColumn(name = "type", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private ItemCashFlowType type;

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public ItemCashFlowType getType() {
        return type;
    }

    @Transient
    public boolean isValid() {
        return time != null && type != null;
    }

    public ItemCashFlowId() {
    }

    private ItemCashFlowId(final LocalDateTime time, final ItemCashFlowType type) {
        this.time = time;
        this.date = time.toLocalDate();
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemCashFlowId other &&
                time != null && other.getTime() != null && time.isEqual(other.getTime()) &&
                type != null && other.getType() != null && type.equals(other.getType());
    }

    @Override
    public int hashCode() {
        return (time != null ? time.hashCode() : 0) ^ (type != null ? type.hashCode() : 0);
    }

    @Override
    public int compareTo(final ItemCashFlowId other) {
        return other != null && other.getTime() != null && time != null ? time.compareTo(other.getTime()) : 0;
    }

    @Override
    public String toString() {
        return "{" + "\"time\": \"" + time + "\", \"type\": \"" + type + "\"}";
    }

    public static class Builder {
        private LocalDateTime time;
        private ItemCashFlowType type;

        public Builder withTime(final LocalDateTime time) {
            this.time = time;
            return this;
        }

        public Builder withType(final ItemCashFlowType type) {
            this.type = type;
            return this;
        }

        public ItemCashFlowId build() {
            return new ItemCashFlowId(time, type);
        }
    }
}
