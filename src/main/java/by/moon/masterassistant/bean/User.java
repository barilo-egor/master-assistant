package by.moon.masterassistant.bean;

import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity{
    private Long chatId;
    private Role role;
    private String username;
    private String firstName;
    private String lastName;
    private UserState userState;
    private String buffer;
    @OneToMany
    private List<Appointment> appointments;
    private String phoneNumber;
}
