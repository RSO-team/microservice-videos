package si.fri.rsoteam.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tag")
@NamedQuery(name = "Tag.getAll", query = "SELECT e from TagEntity e")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Size( min = 3 , max = 100 )
    private String description;

    public Integer getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private VideoEntity  video;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VideoEntity getVideo() {
        return video;
    }

    public void setVideo(VideoEntity video) {
        this.video = video;
    }
}
