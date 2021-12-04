package si.fri.rsoteam.lib.dtos;

import java.time.Instant;
import java.util.Set;

public class VideoDto {
    public Integer id;
    public String link;
    public String description;
    public Set<TagDto> tags;
    public Instant createdAt;

    public VideoDto() {
    }
}
