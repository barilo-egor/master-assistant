package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.WorkingDay;
import by.moon.masterassistant.enums.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    Optional<WorkingDay> findByDay(Day day);
    boolean existsByDay(Day day);
}
