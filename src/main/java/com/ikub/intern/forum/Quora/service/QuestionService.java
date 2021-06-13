package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.TagRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    public QuestionEntity newQuestion(QuestionCreateRequest questionCreateRequest){
        logger.info("Saving question {}",questionCreateRequest);
        Optional<UserEntity> userEntity = userRepo.findById(questionCreateRequest.getUserId());
        Optional<UserGroupEntity> userGroupEntity = groupRepo.findById(questionCreateRequest.getGroupId());
        if (!userEntity.isPresent() ){
            throw new BadRequestException("The user does not exist");
        }
        if (!userGroupEntity.isPresent()){
            throw new BadRequestException("The Group does not exist");
        }
        if (!userIsPartOfTheGroup(userEntity.get(),userGroupEntity.get())){
            throw new NotAllowedException("The user isn't part of the group");
        }
        Set<TagEntity> tagEntities = new HashSet<>();
        for(Long tag : questionCreateRequest.getTags()){
            Optional<TagEntity> tagEntity = tagRepo.findById(tag);
            tagEntity.ifPresent(tagEntities::add);
        }
        QuestionEntity question
                = QuestionConverter.toEntity(questionCreateRequest,userGroupEntity.get(),userEntity.get(),tagEntities);
        logger.info("Save succes  {} ",question.getQuestion());
        return questionsRepo.save(question);
    }
    public QuestionDto findById(Long userId,Long id){
        Optional<QuestionEntity> questionEntity = questionsRepo.findById(id);
        Optional<UserEntity> loggedUser = userRepo.findById(userId);
        if (!questionEntity.get().getGroup().isActive()){
            throw new NotFoundException("Group isn't available");
        }
        if (!questionEntity.isPresent()){
            throw new NotFoundException("Question not found");
        }
        if (!userIsPartOfTheGroup(loggedUser.get(),questionEntity.get().getGroup())){
            throw new NotAllowedException(" You are not allowed to see this question");
        }
        logger.info("Found question  {} ",questionEntity.get().getQuestion());
        return QuestionConverter.entityToDto(questionEntity.get());
    }
    public Page<QuestionDto> findAllInAGroup(PageParams pageParams,Long id){
        if (!pageParams.isValid()){
            throw new BadRequestException("Please enter the correct arguments");
        }
        Optional<UserGroupEntity> userGroupEntity = groupRepo.findById(id);
        if (!userGroupEntity.isPresent()){
            throw new NotFoundException("Group with id "+id+" wasn't found");
        }
        if (pageParams==null) {
            pageParams=new PageParams();
        }
        pageParams.setSort(Sort.Direction.DESC);
        pageParams.setSortField("date");
        logger.info("Finding all questions in group {} ",userGroupEntity.get().getGroupName());
        return QuestionConverter.entityPageToDtoPage(questionsRepo.findAllByGroupId(id,
                PageRequest.of( Integer.parseInt(pageParams.getPageNumber()), Integer.parseInt(pageParams.getPageSize())
                ,pageParams.getSort(),pageParams.getSortField())));
    }
    private boolean userIsPartOfTheGroup(UserEntity user, UserGroupEntity groupEntity){
        List<UserEntity> users = userRepo.findUsersInGroup(groupEntity.getId());
        for (UserEntity u : users){
            if(u.getId()==user.getId()) return true;
        }
        return false;
    }

    public void deleteQuestion(Long id,Long uid){
        Optional<QuestionEntity> questionEntity = questionsRepo.findById(id);
        Optional<UserEntity> loggedUser
                = userRepo.findById(uid);
        if (!loggedUser.isPresent()) {
            throw new NotFoundException("User not found");
        }
        if (!questionEntity.isPresent()) {
            throw new NotFoundException("Question does not exist");
        }
        if (!userIsPartOfTheGroup(loggedUser.get(),questionEntity.get().getGroup())){
            throw new NotAllowedException("You cannot delete this question, because you are not part of the group");
        }

        questionEntity.get().setActive(false);
        logger.info("Deleting question  {} ",questionEntity.get().isActive());
        questionsRepo.save(questionEntity.get());
    }
    public void updateQuestion(Long id,Long uid, QuestionUpdateRequest requestForUpdate){
        Optional<QuestionEntity> questionEntity = questionsRepo.findById(id);
        Optional<UserEntity> loggedUser
                = userRepo.findById(uid);
        if (!loggedUser.isPresent()) {
            throw new NotFoundException("USer not found");
        }
        if (!userIsPartOfTheGroup(loggedUser.get(),questionEntity.get().getGroup())){
            throw new NotAllowedException("You cannot update this question, because you are not part of the group");
        }
        if (!questionEntity.isPresent()) {
            throw new NotFoundException("Question does not exist");
        }
        logger.info("Updating question  {} ",questionEntity.get().getQuestion());
        questionEntity.get().setQuestion(requestForUpdate.getQuestion());
        questionEntity.get().setUpdatedAt(LocalDateTime.now());
        logger.info("Updated question to {} ",questionEntity.get().getQuestion());
        questionsRepo.save(questionEntity.get());
    }

}
