package si.fri.rsoteam.services.beans;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.entities.VideoEntity;
import si.fri.rsoteam.lib.dtos.VideoDto;
import si.fri.rsoteam.services.mappers.TagMapper;
import si.fri.rsoteam.services.mappers.VideoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

import java.util.stream.Collectors;


@RequestScoped
public class VideosBean {
    private Logger log = LogManager.getLogger(VideosBean.class.getName());

    @Inject
    private EntityManager em;
    @Inject
    private TagsBean tagsBean;


    @Timed
    public VideoDto getVideo(Integer id){
        return VideoMapper.entityToDto(em.find(VideoEntity.class,id));
    }

    @Timed
    public List<VideoDto> getAllVideos(){
        return em.createNamedQuery("Video.getAll",VideoEntity.class)
                .getResultList()
                .stream()
                .map(VideoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public VideoDto createVideo(VideoDto videoDto) {
        if (videoDto.createdAt == null) {
            videoDto.createdAt = Instant.now();
        }
        VideoEntity videoEntity = VideoMapper.dtoToEntity(videoDto);
        videoEntity.getTags().forEach(tagEntity -> tagEntity.setVideo(videoEntity));

        this.beginTx();
        em.persist(videoEntity);
        this.commitTx();

        return VideoMapper.entityToDto(videoEntity);
    }

    public VideoDto updateVideo(VideoDto eventDto, Integer id) {
        this.beginTx();

        VideoEntity videoEntity = em.find(VideoEntity.class, id);
        videoEntity.setLink(eventDto.link);
        videoEntity.setCreatedAt(eventDto.createdAt);
        videoEntity.setDescription(eventDto.description);
        videoEntity.setTags(eventDto.tags.stream().map(TagMapper::dtoToEntity).collect(Collectors.toList()));
        videoEntity.getTags().forEach(tagEntity -> tagEntity.setVideo(videoEntity));
        em.persist(videoEntity);
        this.commitTx();

        return VideoMapper.entityToDto(videoEntity);
    }

    public void deleteVideo(Integer id) {
        VideoEntity videoEntity = em.find(VideoEntity.class, id);
        if (videoEntity != null) {

          //  em.createQuery("DELETE FROM video_tag WHERE video_tag.id IN ( SELECT video_tag.id from video_tag where video_tag.video_id = ?1)")
          //                  .setParameter(1,videoEntity.getId()).getResultList();

            videoEntity.getTags().forEach(tagEntity -> tagsBean.deleteTag(tagEntity.getId()));
            this.beginTx();
            em.remove(videoEntity);
            this.commitTx();
        }
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
