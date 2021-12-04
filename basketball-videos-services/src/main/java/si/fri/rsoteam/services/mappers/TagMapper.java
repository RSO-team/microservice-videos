package si.fri.rsoteam.services.mappers;

import si.fri.rsoteam.entities.TagEntity;
import si.fri.rsoteam.entities.VideoEntity;
import si.fri.rsoteam.lib.dtos.TagDto;
import si.fri.rsoteam.lib.dtos.VideoDto;

public class TagMapper {
    public static TagDto entityToDto(TagEntity et) {
        TagDto tagDto = new TagDto();
        tagDto.id = et.getId();
        tagDto.description = et.getDescription();

        return tagDto;
    }

    public static TagEntity dtoToEntity(TagDto tagDto){
        TagEntity tagEntity = new TagEntity();
        tagEntity.setDescription(tagDto.description);

        return tagEntity;
    }
}
