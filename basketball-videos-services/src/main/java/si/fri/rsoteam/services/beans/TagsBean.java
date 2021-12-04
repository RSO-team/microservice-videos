package si.fri.rsoteam.services.beans;

import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rsoteam.entities.TagEntity;
import si.fri.rsoteam.lib.dtos.TagDto;
import si.fri.rsoteam.services.mappers.TagMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class TagsBean {
    private Logger log = Logger.getLogger(TagsBean.class.getName());

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

    public TagDto createTag(TagDto tagDto) {
        List<TagDto> list = getAllTags();
        Optional<TagDto> foundTag = list.stream().filter(tagDto1 -> Objects.equals(tagDto1.description, tagDto.description)).findFirst();
        if(foundTag.isEmpty()){
            TagEntity tagEntity = TagMapper.dtoToEntity(tagDto);
            this.beginTx();
            em.persist(tagEntity);
            this.commitTx();
            return TagMapper.entityToDto(tagEntity);
        }else{
            return foundTag.get();
        }
    }

    public void deleteTag(Integer id) {
        if (em.find(TagEntity.class, id) != null) {
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
