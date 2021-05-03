package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.BotMessage;
import by.moon.masterassistant.enums.BotMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
    Optional<BotMessage> findByType(BotMessageType type);
}
