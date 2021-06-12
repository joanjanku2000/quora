package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.service.QuestionService;
import com.ikub.intern.forum.Quora.service.ReplyService;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/replies")
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private QuestionService questionService;

    @PostMapping("/{uid}/question/{questionId}")
    public String saveReply(@PathVariable Long uid, @PathVariable Long questionId,
                           @Valid @RequestBody ReplyRequest replyRequest,
                            ModelMap model,PageParams params,HttpSession httpSession){
        if (!replyRequest.getReply().isEmpty()){
            replyService.save(uid,questionId,replyRequest);
        }
        else{
            model.addAttribute("error","Please don't leave an empty body");
        }

        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        QuestionDto questionDto
                = questionService.findById(loggedUser.getId(),questionId);
        Page<ReplyDto> replies
                = replyService.getRepliesOfQuestion(questionId,params);

        model.addAttribute("replies",replies);
        model.addAttribute("pageSize",params.getPageSize());
        model.addAttribute("questionDto",questionDto);
        model.addAttribute("user", loggedUser);
        return "question::replies";
    }
    @PutMapping("/{id}")
    public String updateReply(@PathVariable Long id, @Valid @RequestBody ReplyRequest replyRequest, ModelMap model, PageParams params, HttpSession httpSession){
        Long questionId
                = (Long) httpSession.getAttribute("question");

        UserDto loggedUser
                = LoggedUserUtil.getLoggedUserDto(httpSession);

        replyService.update(loggedUser.getId(),id,replyRequest);
        QuestionDto questionDto
                = questionService.findById(loggedUser.getId(),questionId);

        Page<ReplyDto> replyDtos
                = replyService.getRepliesOfQuestion(questionId,params);
        if (Integer.parseInt(params.getPageNumber())>replyDtos.getTotalPages()){
            params.setPageNumber(String.valueOf(0));
            replyDtos = replyService.getRepliesOfQuestion(questionId,params);
        }
        model.addAttribute("questionDto",questionDto);
        model.addAttribute("user",loggedUser);
        model.addAttribute("replies", replyDtos);
        model.addAttribute("pageSize",params.getPageSize());
        return "question::replies";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,ModelMap model,PageParams params,HttpSession httpSession){
        Long questionId = (Long) httpSession.getAttribute("question");
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        replyService.deleteReply(loggedUser.getId(),id);

        Page<ReplyDto> replyDtos
                = replyService.getRepliesOfQuestion(questionId,params);
        if (Integer.parseInt(params.getPageNumber())>replyDtos.getTotalPages()){
            params.setPageNumber(String.valueOf(0));
            replyDtos = replyService.getRepliesOfQuestion(questionId,params);
        }
        QuestionDto questionDto = questionService.findById(loggedUser.getId(),questionId);

        model.addAttribute("questionDto",questionDto);
        model.addAttribute("user", loggedUser);
        model.addAttribute("replies", replyDtos);
        model.addAttribute("pageSize",params.getPageSize());

        return "question::replies";
    }

}
