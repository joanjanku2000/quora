package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.question.MostUpvotedQuestions;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.service.QuestionService;
import com.ikub.intern.forum.Quora.service.ReplyService;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private ReplyService replyService;

    @PostMapping("/new")
    public String saveQuestion(@Valid @RequestBody QuestionCreateRequest questionCreateRequest,
                                     ModelMap map,PageParams params,HttpSession httpSession){
        if (!questionCreateRequest.getQuestion().isEmpty()) {
            questionService.newQuestion(questionCreateRequest);
        }
        else {
            map.addAttribute("error", "Question cannot be empty");
        }
        UserDto loggedUser
                = LoggedUserUtil.getLoggedUserDto(httpSession);
        params.setPageNumber(String.valueOf(0));  /** always go to first page when new question is added */
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
        UserDto loggedUser
                = LoggedUserUtil.getLoggedUserDto(httpSession);
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());
        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser",loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }
    @GetMapping
    public ModelAndView findById(@RequestParam Long id,PageParams params,HttpSession httpSession){

        httpSession.setAttribute("question",id);
        UserDto loggedUser
                = LoggedUserUtil.getLoggedUserDto(httpSession);
        QuestionDto questionDto
                =  questionService.findById(loggedUser.getId(),id);
        ModelAndView modelAndView = new ModelAndView("question");
        Page<ReplyDto> replyDtos
                = replyService.getRepliesOfQuestion(id,params);

        /** Handling page number mistake */
        if (Integer.parseInt(params.getPageNumber())>replyDtos.getTotalPages()-1 ){
            params.setPageNumber(String.valueOf(0));
            replyDtos = replyService.getRepliesOfQuestion(id,params);
        }
        modelAndView.addObject(questionDto);
        modelAndView.addObject("user",loggedUser);
        modelAndView.addObject("replies",replyDtos);
        modelAndView.addObject("pageSize",params.getPageSize());
        return modelAndView;
    }

    @PutMapping("/update/question/{id}")
    public String updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateRequest requestForUpdate,
                               PageParams params,HttpSession httpSession,ModelMap map){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);

        if (!requestForUpdate.getQuestion().isEmpty()){
            questionService.updateQuestion(id,loggedUser.getId(),requestForUpdate);
        }
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,(Long) httpSession.getAttribute("group"));
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());

        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id,Long uid,ModelMap map, PageParams params,HttpSession httpSession){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        questionService.deleteQuestion(id,loggedUser.getId());
        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params,(Long) httpSession.getAttribute("group"));

        List<Long> groupRequests
                = userService.groupsRequestedToJoin(loggedUser.getId());
        map.addAttribute("questions",questions);
        map.addAttribute("loggedUser", loggedUser);
        map.addAttribute("groupRequests",groupRequests);
        return "group_page::questions";
    }

    @GetMapping("/most-upvoted")
    @ResponseBody
    public List<MostUpvotedQuestions> findMostUpvotedOfUser(HttpSession httpSession){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        return questionService.findAllOfUser(loggedUser.getId());
    }

}
