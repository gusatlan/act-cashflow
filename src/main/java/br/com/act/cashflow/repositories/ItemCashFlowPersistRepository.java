package br.com.act.cashflow.repositories;

import br.com.act.cashflow.model.sql.ItemCashFlowPersist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ItemCashFlowPersistRepository extends JpaRepository<ItemCashFlowPersist, Long> {

    @Query(value = "select o from ItemCashFlowPersist o where o.date between :begin and :end")
    Collection<ItemCashFlowPersist> findByPeriod(final LocalDateTime begin, final LocalDateTime end);
}
