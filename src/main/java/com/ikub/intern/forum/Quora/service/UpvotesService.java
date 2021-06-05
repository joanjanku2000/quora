package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
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
        if (!user.isPresent()) {
            throw new BadRequestException("User does not exist");
        }
        if (!questionEntity.isPresent()) {
            throw new BadRequestException("Question does not exist");
        }
        if (!isUserGroupMember(user.get(),questionEntity.get())) {
            throw new BadRequestException("User not allowed to upvote");
        }
        UpvotesQuestion upvotesQuestion
                = questionUpvotesRepo.findByQuestionIdAndUserId(questionId,uid).orElse(null);
       if (upvotesQuestion!=null){
           if (upvotesQuestion.isActive())
               upvotesQuestion.setActive(false);
           else
           {
               upvotesQuestion.setActive(true);
               upvotesQuestion.setUpvoteDate(LocalDateTime.now());
           }

       } else {
           upvotesQuestion  = new UpvotesQuestion(user.get(),true, LocalDateTime.now(),questionEntity.get());
       }

        questionUpvotesRepo.save(upvotesQuestion);

    }
    public void upvoteReply(Long uid,Long replyId){
        UserEntity user = userRepo.findById(uid).orElse(null);
        ReplyEntity replyEntity = repliesRepo.findById(replyId).orElse(null);
        if (user==null) throw new BadRequestException("User does not exist");
        if (replyEntity == null) throw new BadRequestException("Reply does not exist");
        if (!isUserGroupMember(user, replyEntity)) throw new BadRequestException("User not allowed to upvote");
        UpvotesReply upvotesReply = replyUpvotesRepo.findByReplyIdAndUserId(replyId,uid).orElse(null);
        if (upvotesReply!=null){
            if (upvotesReply.isActive())
                upvotesReply.setActive(false);
            else{
                upvotesReply.setActive(true);
                upvotesReply.setUpvoteDate(LocalDateTime.now());
            }
        } else{
            upvotesReply = new UpvotesReply(user,true, LocalDateTime.now(), replyEntity);
        }


        replyUpvotesRepo.save(upvotesReply);
    }
    public void deleteUpvoteQuestion(Long id){
        UpvotesQuestion upvotesQuestion = questionUpvotesRepo.findById(id).orElse(null);
        if (upvotesQuestion==null) throw new BadRequestException("Reply does not exist");
        upvotesQuestion.setActive(false);
        questionUpvotesRepo.save(upvotesQuestion);

    }
    public void deleteUpvoteReply(Long id){
        UpvotesReply upvotesReply = replyUpvotesRepo.findById(id).orElse(null);
        if (upvotesReply ==null) throw new BadRequestException("Reply does not exist");
        upvotesReply.setActive(false);
        replyUpvotesRepo.save(upvotesReply);
    }
    public List<UpvotesQuestion>  getUpvotesOfQuestion(Long id){
        QuestionEntity questionEntity = questionRepo.findById(id).orElse(null);
        if (questionEntity==null) throw new BadRequestException("Question does not exist");
        return questionUpvotesRepo.findAllByQuestionId(id);
    }
    public List<UpvotesReply>  getUpvotesOfReply(Long id){
        ReplyEntity replyEntity = repliesRepo.findById(id).orElse(null);
        if (replyEntity ==null) throw new BadRequestException("Reply does not exist");
        return replyUpvotesRepo.findAllByReplyId(id);
    }
}
