package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.TagConverter;
import com.ikub.intern.forum.Quora.dto.tag.TagDto;
import com.ikub.intern.forum.Quora.dto.tag.TagDtoForCreate;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.repository.TagRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepo tagRepo;
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    public void saveTag(TagDtoForCreate tag, UserEntity userEntity){
        Optional<TagEntity> tagEntity = tagRepo.findByTagName(tag.getTagName());
        logger.info("Saving tag by {}",userEntity);
        if (tagEntity.isPresent()) throw new BadRequestException("Sorry, the tag with the given name already exists");
        TagEntity newTag = TagConverter.toEntity(tag,userEntity);
        logger.info("Saving tag {}",newTag);
        tagRepo.save(newTag);
    }
    public List<TagDto> findALl(){
        return TagConverter.entityListToDtoList(tagRepo.findAll());
    }
    public void deleteTag(Long id){
        TagEntity tagEntity = tagRepo.findById(id).orElse(null);
        if (tagEntity==null) throw new BadRequestException("Tag with the given id doesn't exist");
        tagEntity.setActive(false);
        tagRepo.save(tagEntity);
    }

    public TagEntity findById(Long id){
        TagEntity tagEntity = tagRepo.findById(id).orElse(null);
        if (tagEntity==null) throw new BadRequestException("The tag with the given id does not exist");
        return tagEntity;
    }


}
