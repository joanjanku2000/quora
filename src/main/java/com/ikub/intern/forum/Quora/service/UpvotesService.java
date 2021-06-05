package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;
import com.ikub.intern.forum.Quora.repository.UpvoteQuestionRepo;
import com.ikub.intern.forum.Quora.repository.UpvotesReplyRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UpvotesService {
    @Autowired
    private UpvoteQuestionRepo questionUpvotesRepo;
    @Autowired
    private UpvotesReplyRepo replyUpvotesRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuestionsRepo questionRepo;
    @Autowired
    private ReplyRepo repliesRepo;
    private static final Logger logger = LoggerFactory.getLogger(UpvotesService.class);
    public boolean isUserGroupMember(UserEntity user,QuestionEntity questionEntity){
        List<UserGroupEntity> userGroupEntities = userRepo.findGroupsOfUser(user.getId());
        for (UserGroupEntity group : userGroupEntities){
            if (group.getId()==questionEntity.getGroup().getId())
                return true;
        }
        return false;
    }
    public boolean isUserGroupMember(UserEntity user, ReplyEntity replyEntity){
        List<UserGroupEntity> userGroupEntities = userRepo.findGroupsOfUser(user.getId());
        for (UserGroupEntity group : userGroupEntities){
            if (group.getId()==replyEntity.getQuestion().getGroup().getId())
                return true;
        }
        return false;
    }
    public void upvoteQuestion(Long uid,Long questionId){
        Optional<UserEntity> user = userRepo.findById(uid);
        Optional<QuestionEntity> questionEntity = questionRepo.findById(questionId);
        logger.info("Upvoting question {} by user {}"+questionId,uid);
        if (!user.isPresent()) {
            throw new BadRequestException("User does not exist");
        }
        if (!questionEntity.isPresent()) {
            throw new BadRequestException("Question does not exist");
        }
        if (!isUserGroupMember(user.get(),questionEntity.get())) {
            throw new NotAllowedException("User not allowed to upvote");
        }
        UpvotesQuestion upvotesQuestion
                = questionUpvotesRepo.findByQuestionIdAndUserId(questionId,uid).orElse(null);
       if (upvotesQuestion!=null){
           if (upvotesQuestion.isActive()) {
               logger.info("Upvoting already exists, removing it");
               upvotesQuestion.setActive(false);
           }
           else {
               upvotesQuestion.setActive(true);
               upvotesQuestion.setUpvoteDate(LocalDateTime.now());
           }

       } else {
           upvotesQuestion  = new UpvotesQuestion(user.get(),true, LocalDateTime.now(),questionEntity.get());
       }
        logger.info("Upvoting done");
        questionUpvotesRepo.save(upvotesQuestion);

    }
    public void upvoteReply(Long uid,Long replyId){
        Optional<UserEntity> user = userRepo.findById(uid);
        Optional<ReplyEntity> replyEntity = repliesRepo.findById(replyId);
        logger.info("Upvoting reply {} by user {}"+replyId,uid);
        if (!user.isPresent()) {
            throw new BadRequestException("User does not exist");
        }
        if (!replyEntity.isPresent()) {
            throw new BadRequestException("Reply does not exist");
        }
        if (!isUserGroupMember(user.get(), replyEntity.get())) {
            throw new BadRequestException("User not allowed to upvote");
        }
        UpvotesReply upvotesReply = replyUpvotesRepo.findByReplyIdAndUserId(replyId,uid).orElse(null);
        if (upvotesReply!=null){
            if (upvotesReply.isActive()) {
                upvotesReply.setActive(false);
                logger.info("Upvoting already exists, removing it");
            }
            else{
                upvotesReply.setActive(true);
                upvotesReply.setUpvoteDate(LocalDateTime.now());
            }
        } else{
            upvotesReply = new UpvotesReply(user.get(),true, LocalDateTime.now(), replyEntity.get());
        }
        logger.info("Upvoting done");
        replyUpvotesRepo.save(upvotesReply);
    }

    public List<UpvotesQuestion>  getUpvotesOfQuestion(Long id){
        Optional<QuestionEntity> questionEntity = questionRepo.findById(id);
        if (!questionEntity.isPresent()){
            throw new BadRequestException("Question does not exist");
        }
        logger.info("Finding upvotes of {} "+questionEntity);
        return questionUpvotesRepo.findAllByQuestionId(id);
    }
    public List<UpvotesReply>  getUpvotesOfReply(Long id){
        Optional<ReplyEntity> replyEntity = repliesRepo.findById(id);
        if (!replyEntity.isPresent()) {
            throw new BadRequestException("Reply does not exist");
        }
        logger.info("Finding upvotes of {} "+replyEntity);
        return replyUpvotesRepo.findAllByReplyId(id);
    }
}
