package by.moon.masterassistant.repository;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDate(LocalDate localDate);
    List<Appointment> findByUser(User user);
}
