package by.moon.masterassistant.bean;

import by.moon.masterassistant.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Time;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkingDay extends BaseEntity{
    @Column(unique = true)
    private Day day;
    private Time timeFrom;
    private Time timeTo;
}
