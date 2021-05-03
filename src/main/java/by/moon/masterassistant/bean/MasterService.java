package by.moon.masterassistant.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MasterService extends BaseEntity {
    private String name;
    private String photo;
    private String description;
    private String price;
}
