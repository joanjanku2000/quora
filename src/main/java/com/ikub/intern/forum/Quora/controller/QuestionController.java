package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.LoggedUser;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.service.QuestionService;
import com.ikub.intern.forum.Quora.service.ReplyService;
import com.ikub.intern.forum.Quora.service.TagService;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private TagService tagService;
    /**
     * Should i get group and users as parameter ?
     * @param questionCreateRequest
     */
    @PostMapping("/new")
    public String saveQuestion(@RequestBody QuestionCreateRequest questionCreateRequest,
                                     ModelMap map,PageParams params,HttpSession httpSession){
        if (!questionCreateRequest.getQuestion().isEmpty())
            questionService.newQuestion(questionCreateRequest);
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        params.setPageNumber(0);
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,questionCreateRequest.getGroupId());
        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }
    @GetMapping("/group/{id}")
    public String getQuestionOfGroup(@PathVariable Long id, PageParams params,ModelMap map,HttpSession httpSession){
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,id);
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());
        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser",loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }
    @GetMapping
    public ModelAndView findById(@RequestParam Long id,PageParams params,HttpSession httpSession){
        httpSession.setAttribute("question",id);

        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        QuestionDto questionDto =  questionService.findById(id);
        ModelAndView modelAndView = new ModelAndView("question");


        Page<ReplyDto> replyDtos = replyService.getRepliesOfQuestion(id,params);
        if (params.getPageNumber()>replyDtos.getTotalPages()){
            params.setPageNumber(0);
            replyDtos = replyService.getRepliesOfQuestion(id,params);
        }


        modelAndView.addObject(questionDto);
        modelAndView.addObject("user",loggedUser);
        modelAndView.addObject("replies",replyDtos);
        modelAndView.addObject("pageSize",params.getPageSize());
        return modelAndView;
    }
    @GetMapping("/all")
    public Page<QuestionEntity> findAll(PageParams pageParams){
        return questionService.findALl(pageParams);
    }

    @PutMapping("/update/question/{id}")
    public String updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateRequest requestForUpdate,
                               PageParams params,HttpSession httpSession,ModelMap map){
        if (!requestForUpdate.getQuestion().isEmpty()){
            questionService.updateQuestion(id,requestForUpdate);
        }
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,(Long) httpSession.getAttribute("group"));
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());

        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id,ModelMap map, PageParams params,HttpSession httpSession){
        questionService.deleteQuestion(id);
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,(Long) httpSession.getAttribute("group"));
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());
        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }

}
