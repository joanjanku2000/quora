package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.service.QuestionService;
import com.ikub.intern.forum.Quora.service.UpvotesService;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/upvotes")
public class UpvotesController {
    @Autowired
    private UpvotesService service;
    @Autowired
    private QuestionService questionService;


    @PostMapping("/{uid}/question/{questionId}")
    public String upvoteQuestion(@PathVariable Long uid, @PathVariable Long questionId,
                                 PageParams params, ModelMap map, HttpSession httpSession){
        service.upvoteQuestion(uid,questionId);
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,(Long) httpSession.getAttribute("group"));

        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", (UserEntity) httpSession.getAttribute("loggedUser"));
        return "group_page::questions";
    }

    @PostMapping("/{uid}/reply/{replyId}")
    public void upvoteReply(@PathVariable Long uid,@PathVariable Long replyId){
        service.upvoteReply(uid,replyId);
    }

    @GetMapping("/question/{id}")
    public List<UpvotesQuestion> getUpvotesOfQuestion(@PathVariable Long id){
        return service.getUpvotesOfQuestion(id);
    }
    @GetMapping("/reply/{id}")
    public List<UpvotesReply> getUpvotesOfReply(@PathVariable Long id){
        return  service.getUpvotesOfReply(id);
    }

}
