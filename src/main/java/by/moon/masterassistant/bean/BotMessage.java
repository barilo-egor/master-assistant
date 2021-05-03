package by.moon.masterassistant.bean;

import by.moon.masterassistant.enums.BotMessageType;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BotMessage extends BaseEntity{
    @Column(unique = true)
    private BotMessageType type;
    @JoinTable
    private String text;
    private String photo;
}
