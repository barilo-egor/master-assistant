package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.MasterContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterContactRepository extends JpaRepository<MasterContact, Long> {
}
