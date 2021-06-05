package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.ReplyConverter;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ReplyService {
    @Autowired
    private ReplyRepo replyRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuestionsRepo questionRepo;


    private static final Logger logger = LoggerFactory.getLogger(ReplyService.class);
    public boolean userIsPartOfTheGroup(UserEntity user, UserGroupEntity groupEntity){
        List<UserEntity> userEntityList = userRepo.findUsersInGroup(groupEntity.getId());
        for (UserEntity u : userEntityList){
            if(u.getId()==user.getId()) return true;
        }
        return false;
    }
    public void save(Long userId, Long questionId, ReplyRequest request){
        logger.info("Adding reply of user with id {} to question with id {} , reply {}"
                ,userId,questionId,request.getReply());
        Optional<UserEntity> userWhoReplied = userRepo.findById(userId);
        Optional<QuestionEntity> questionToBeReplied = questionRepo.findById(questionId);
        if (!userWhoReplied.isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        if (!questionToBeReplied.isPresent()) {
            throw new NotFoundException("Question does not exist");
        }
        if (!userIsPartOfTheGroup(userWhoReplied.get(),questionToBeReplied.get().getGroup())){
            throw new NotAllowedException("This user isn't part of the group");
        }
        ReplyEntity reply = ReplyConverter.toEntity(request,userWhoReplied.get(),questionToBeReplied.get());
        replyRepo.save(reply);
        logger.info("Added successfully");
    }
    public void update(Long uid,Long replyId ,ReplyRequest request){
        Optional<ReplyEntity> reply = replyRepo.findById(replyId);
        Optional<UserEntity> user = userRepo.findById(uid);
        if (!reply.isPresent()) {
            logger.info("Reply {} not found",replyId);
            throw new BadRequestException("Reply doesn't exist");
        }
        if (reply.get().getUser().getId()!=user.get().getId()){
            logger.info("User {} not allowed to update reply {} ",uid,replyId);
            throw new NotAllowedException("You are not allowed to update this question");
        }
        logger.info("Updating reply {}",reply);
        reply.get().setReply(request.getReply());
        reply.get().setUpdatedAt(LocalDateTime.now());
        replyRepo.save(reply.get());
        logger.info("Update successful");
    }
    public void deleteReply(Long uid,Long replyId){
        Optional<ReplyEntity> reply = replyRepo.findById(replyId);
        Optional<UserEntity> user = userRepo.findById(uid);
        if (!reply.isPresent()){
            logger.info("Reply {} not found",replyId);
            throw new BadRequestException("Reply doesn't exist");
        }
        if (reply.get().getUser().getId()!=user.get().getId()){
            logger.info("User {} not allowed to delete reply {} ",uid,replyId);
            throw new NotAllowedException("You are not allowed to update this question");
        }
        logger.info("Deleting reply {}",reply);
        reply.get().setActive(false);
        replyRepo.save(reply.get());
        logger.info("Deletion successful");
    }
    public ReplyDto findById(Long id){
        Optional<ReplyEntity> replyEntity = replyRepo.findById(id);
        if (!replyEntity.isPresent()){
            throw new NotFoundException("Reply Not Found") ;
        }
        logger.info("Succes on find by id {} "+replyEntity);
        return ReplyConverter.entityToDto(replyEntity.get());
    }
    public Page<ReplyDto> getRepliesOfQuestion(Long id, PageParams pageParams){
        return ReplyConverter.entityPageToDtoPage(
                replyRepo.findAllByQuestionIdAndActiveTrueOrderByCreatedAtDesc(id, PageRequest.of(pageParams.getPageNumber()
                        ,pageParams.getPageSize())));
    }
}
