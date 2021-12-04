package si.fri.rsoteam.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tag")
@NamedQuery(name = "Tag.getAll", query = "SELECT e from TagEntity e")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size( min = 3 , max = 100 )
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
