package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.BalanceChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Long> {
}
