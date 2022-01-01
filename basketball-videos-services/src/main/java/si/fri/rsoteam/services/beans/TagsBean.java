package si.fri.rsoteam.services.beans;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.entities.TagEntity;
import si.fri.rsoteam.lib.dtos.TagDto;
import si.fri.rsoteam.services.mappers.TagMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class TagsBean {
    private Logger log = LogManager.getLogger(TagsBean.class.getName());

    @Inject
    private EntityManager em;

    @Timed
    public TagDto getTag(Integer id){
        return TagMapper.entityToDto(em.find(TagEntity.class,id));
    }

    @Timed
    public List<TagDto> getAllTags(){
        return em.createNamedQuery("Tag.getAll", TagEntity.class).getResultList().stream().map(TagMapper::entityToDto).collect(Collectors.toList());
    }

    public void deleteTag(Integer id) {
        TagEntity tagEntity = em.find(TagEntity.class, id);
        if ( tagEntity != null) {
            this.beginTx();
            em.remove(tagEntity);
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
