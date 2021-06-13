package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.group.GroupWithMostAskedQuestion;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.user.MostActiveUsersInGroup;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.service.*;
import com.ikub.intern.forum.Quora.utils.Filter;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public ModelAndView findALL(PageParams params, Filter filter, HttpSession httpSession) {
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        Page<GroupDto> groupDtos = groupService.findALl(params, filter);

        List<Long> groupRequests
                = userService.groupsRequestedToJoin(loggedUser.getId());
        for (GroupDto groupDto : groupDtos) {
            List<UserDto> users = userService.findUsersInGroup(groupDto.getId());
            groupDto.setUsers(users);
        }
        List<CategoryDto> allCategories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("discover_groups");
        modelAndView.addObject("loggedUser", loggedUser);
        modelAndView.addObject("groups", groupDtos);
        modelAndView.addObject("groupRequests", groupRequests);
        modelAndView.addObject("allCategories", allCategories);
        return modelAndView;
    }

    @GetMapping("/find")
    public String find(PageParams params, Filter filter, HttpSession httpSession, Model model) {
        UserDto loggedUser = (UserDto) httpSession.getAttribute("loggedUser");
        Page<GroupDto> groupDtos = groupService.findALl(params, filter);
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());

        for (GroupDto groupDto : groupDtos) {
            List<UserDto> users = userService.findUsersInGroup(groupDto.getId());
            groupDto.setUsers(users);
        }
        List<CategoryDto> allCategories = categoryService.findAll();

        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("groups", groupDtos);
        model.addAttribute("groupRequests", groupRequests);
        model.addAttribute("allCategories", allCategories);
        return "discover_groups::groups";
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable Long id, PageParams params, HttpSession httpSession) {
        UserDto loggedUser = (UserDto) httpSession.getAttribute("loggedUser");
        ModelAndView modelAndView = new ModelAndView("group_page");

        UserGroupEntity groupEntity = groupService.findById(id);
        List<UserDto> users = userService.findUsersInGroup(id);

        GroupDto groupDto = GroupConverter.entityToDto(groupEntity);
        groupDto.setUsers(users);

        boolean userIsPartOfTheGroup = false;
        for (UserDto userDto : users) {
            if (loggedUser.getUsername().compareTo(userDto.getUsername()) == 0) {
                userIsPartOfTheGroup = true;
            }
        }
        if (!userIsPartOfTheGroup) {
            return new ModelAndView("not_found");
        }
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());

        Page<QuestionDto> questions
                = questionService.findAllInAGroup(params, groupDto.getId());

        if (Integer.parseInt(params.getPageNumber()) > questions.getTotalPages() - 1 ) {
            params.setPageNumber(String.valueOf(0));
            questions = questionService.findAllInAGroup(params, groupDto.getId());
        }
        httpSession.setAttribute("group", groupDto.getId());

        modelAndView.addObject("loggedUser", loggedUser);
        modelAndView.addObject("group", groupDto);
        modelAndView.addObject("questions", questions);
        modelAndView.addObject("users", users);
        modelAndView.addObject("tags", tagService.findALl());
        modelAndView.addObject("pageSize", params.getPageSize());
        modelAndView.addObject("groupRequests", groupRequests);
        return modelAndView;
    }

    @PutMapping("/update/{id}")
    public GroupDto update(@PathVariable Long id, @RequestBody GroupDtoForCreateUpdate groupDto) {
        return groupService.updateGroup(id, groupDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }


    @PostMapping("/new")
    public ModelAndView save(@ModelAttribute GroupDtoForCreateUpdate groupDto, ModelMap map, HttpSession httpSession) {
        UserDto loggedUser = (UserDto) httpSession.getAttribute("loggedUser");
        groupService.createGroup(loggedUser.getId(), groupDto);
        return new ModelAndView("redirect:/users/groups", map);
    }

    @GetMapping("/most-asked-questions")
    @ResponseBody
    public List<GroupWithMostAskedQuestion> getGroupsWithMostAskedQuestions(HttpSession httpSession){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        return groupService.findGroupsWithMostAskedQuestions(loggedUser.getId());
    }

    @GetMapping("/most-active-users")
    @ResponseBody
    public List<MostActiveUsersInGroup> getMostActiveUsers(HttpSession httpSession,@RequestParam Long gid){
        return groupService.findMostActiveUsersInGroup(gid);
    }
}
