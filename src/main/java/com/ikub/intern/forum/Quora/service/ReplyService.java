package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.ReplyConverter;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
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
    @Autowired
    private UserGroupRepo groupRepo;

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
        UserEntity userWhoReplied = userRepo.findById(userId).orElse(null);
        QuestionEntity questionToBeReplied = questionRepo.findById(questionId).orElse(null);
        if (userWhoReplied==null ) throw new BadRequestException("User does not exist");
        if (questionToBeReplied==null) throw new BadRequestException("Question does not exist");
        if (!userIsPartOfTheGroup(userWhoReplied,questionToBeReplied.getGroup())){
            throw new BadRequestException("This user isn't part of the group");
        }
        ReplyEntity reply = ReplyConverter.toEntity(request,userWhoReplied,questionToBeReplied);
        replyRepo.save(reply);
        logger.info("Added successfully");
    }
    public void update(Long replyId ,ReplyRequest request){
        ReplyEntity reply = replyRepo.findById(replyId).orElse(null);
        if (reply == null) throw new BadRequestException("Reply doesn't exist");
        logger.info("Updating reply {}",reply);
        reply.setReply(request.getReply());
        reply.setUpdatedAt(LocalDateTime.now());
        replyRepo.save(reply);
        logger.info("Update successful");
    }
    public void deleteReply(Long replyId){
        ReplyEntity reply = replyRepo.findById(replyId).orElse(null);
        if (reply == null) throw new BadRequestException("Reply doesn't exist");
        logger.info("Deleting reply {}",reply);
        reply.setActive(false);
        replyRepo.save(reply);
        logger.info("Deletion successful");
    }
    public ReplyDto findById(Long id){
        Optional<ReplyEntity> replyEntity = replyRepo.findById(id);
        if (!replyEntity.isPresent()){
            throw new NotFoundException("Reply Not Found") ;
        }
        return ReplyConverter.entityToDto(replyEntity.get());
    }
    public Page<ReplyDto> getRepliesOfQuestion(Long id, PageParams pageParams){
        return ReplyConverter.entityPageToDtoPage(
                replyRepo.findAllByQuestionIdAndActiveTrueOrderByCreatedAtDesc(id, PageRequest.of(pageParams.getPageNumber()
                        ,pageParams.getPageSize())));
    }
}
