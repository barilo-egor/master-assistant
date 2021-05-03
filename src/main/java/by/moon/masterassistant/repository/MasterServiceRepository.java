package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.MasterService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterServiceRepository extends JpaRepository<MasterService, Long> {
}
