package by.moon.masterassistant.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Time;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appointment extends BaseEntity {
    private LocalDate date;
    private Time time;
    @ManyToOne
    private User user;
    private int duration;
    private boolean isActive;
    private boolean isConfirmed;
    private String comment;
}
