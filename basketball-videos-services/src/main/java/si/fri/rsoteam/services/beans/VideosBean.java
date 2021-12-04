package si.fri.rsoteam.services.beans;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.entities.VideoEntity;
import si.fri.rsoteam.lib.dtos.VideoDto;
import si.fri.rsoteam.services.mappers.VideoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class VideosBean {
    private Logger log = Logger.getLogger(VideosBean.class.getName());

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

     //TODO this will purposly make a lot of call to EM, to lower performance, for metrics
    public VideoDto createVideo(VideoDto videoDto) {
        VideoEntity videoEntity = VideoMapper.dtoToEntity(videoDto);
        videoDto.tags=videoDto.tags.stream().map(tagsBean::createTag).collect(Collectors.toSet());
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
        // TODO update labels
        em.persist(videoEntity);
        this.commitTx();

        return VideoMapper.entityToDto(videoEntity);
    }

    public void deleteVideo(Integer id) {
        if (em.find(VideoEntity.class, id) != null) {
            this.beginTx();
            em.remove(id);
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
