package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.dto.LoggedUser;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.TagRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestionService {
    @Autowired
    private QuestionsRepo questionsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserGroupRepo groupRepo;
    @Autowired
    private TagRepo tagRepo;

    public void newQuestion(QuestionCreateRequest questionCreateRequest){
        UserEntity userEntity = userRepo.findById(questionCreateRequest.getUserId())
                .orElse(null);
        UserGroupEntity userGroupEntity = groupRepo.findById(questionCreateRequest.getGroupId())
                .orElse(null);
        if (userEntity==null ){
            throw new BadRequestException("The user does not exist");
        }
        if (userGroupEntity==null){
            throw new BadRequestException("The Group does not exist");
        }
        if (!userIsPartOfTheGroup(userEntity,userGroupEntity)){
            throw new NotAllowedException("The user isn't part of the group");
        }
        Set<TagEntity> tagEntities = new HashSet<>();
        for(Long tag : questionCreateRequest.getTags()){
            Optional<TagEntity> tagEntity = tagRepo.findById(tag);
            if (tagEntity.isPresent()){
                tagEntities.add(tagEntity.get());
            }
        }
        QuestionEntity question
                = QuestionConverter.toEntity(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        if (question.getTagList() != null && !question.getTagList().isEmpty()){
            List<Long> tagIds = questionCreateRequest.getTags();
            Set<TagEntity> tagEntitySet = new HashSet<>();
            for (Long tagId : tagIds){
                TagEntity tagEntity = tagRepo.findById(tagId).orElse(null);
                if (tagEntity!=null) tagEntitySet.add(tagEntity);
            }
            question.setTagList(tagEntitySet);
        }
        questionsRepo.save(question);
    }
    public QuestionDto findById(Long id){
        Optional<QuestionEntity> questionEntity = questionsRepo.findById(id);
        if (!questionEntity.isPresent()){
            throw new NotFoundException("Question not found");
        }
        if (!userIsPartOfTheGroup(LoggedUser.getLoggedUser(),questionEntity.get().getGroup())){
            throw new NotAllowedException(" You are not allowed to see this question");
        }
        return QuestionConverter.entityToDto(questionEntity.get());
    }
    public Page<QuestionDto> findAllInAGroup(PageParams pageParams,Long id){
        if (pageParams==null) pageParams=new PageParams();
        pageParams.setSort(Sort.Direction.DESC);
        pageParams.setSortField("date");
        return QuestionConverter.entityPageToDtoPage(questionsRepo.findAllByGroupId(id, PageRequest.of(pageParams.getPageNumber(),pageParams.getPageSize()
                ,pageParams.getSort(),pageParams.getSortField())));
    }
    private boolean userIsPartOfTheGroup(UserEntity user, UserGroupEntity groupEntity){
        List<UserEntity> users = userRepo.findUsersInGroup(groupEntity.getId());
        for (UserEntity u : users){
            if(u.getId()==user.getId()) return true;
        }
        return false;
    }
    public Page<QuestionEntity> findALl(PageParams pageParams){
        return questionsRepo.findAll(PageRequest.of(pageParams.getPageNumber(),pageParams.getPageSize()));
    }
    public void deleteQuestion(Long id){
        QuestionEntity questionEntity = questionsRepo.findById(id)
                .orElse(null);
        if (questionEntity==null) throw new BadRequestException("Question does not exist");
        questionEntity.setActive(false);
        questionsRepo.save(questionEntity);
    }
    public void updateQuestion(Long id, QuestionUpdateRequest requestForUpdate){
        System.out.println("FNIN");
        Optional<QuestionEntity> questionEntity = questionsRepo.findById(id);

        if (!questionEntity.isPresent()) throw new BadRequestException("Question does not exist");
        questionEntity.get().setQuestion(requestForUpdate.getQuestion());
        questionEntity.get().setUpdatedAt(LocalDateTime.now());
        questionsRepo.save(questionEntity.get());
    }

}
