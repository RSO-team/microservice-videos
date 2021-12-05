package si.fri.rsoteam.services.mappers;

import si.fri.rsoteam.entities.VideoEntity;
import si.fri.rsoteam.lib.dtos.TagDto;
import si.fri.rsoteam.lib.dtos.VideoDto;

import java.util.Set;
import java.util.stream.Collectors;

public class VideoMapper {
    public static VideoDto entityToDto(VideoEntity et) {
        VideoDto videoDto = new VideoDto();
        videoDto.id = et.getId();
        videoDto.link = et.getLink();
        videoDto.description = et.getDescription();
        videoDto.createdAt = et.getCreatedAt();
        videoDto.tags = et.getTags().stream().map(TagMapper::entityToDto).collect(Collectors.toList());

        return videoDto;
    }

    public static VideoEntity dtoToEntity(VideoDto videoDto) {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setDescription(videoDto.description);
        videoEntity.setCreatedAt(videoDto.createdAt);
        videoEntity.setLink(videoDto.link);
        videoEntity.setTags(videoDto.tags.stream().map(TagMapper::dtoToEntity).collect(Collectors.toList()));
        videoEntity.setLink(videoDto.link);

        return  videoEntity;
    }
}
