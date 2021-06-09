package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.converter.UserConverter;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.user.UserCreateRequest;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.dto.user.UserUpdateRequest;
import com.ikub.intern.forum.Quora.entities.Feed;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.security.oauth.CustomOauth2User;
import com.ikub.intern.forum.Quora.service.CategoryService;
import com.ikub.intern.forum.Quora.service.GroupService;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.Filter;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public UserDto find(@PathVariable Long id) {
        return UserConverter.entityToDto(userService.find(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/update")
    public ModelAndView updateView() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        UserEntity userEntity = userService.findByEmail(principal.getAttribute("email"));
        ModelAndView modelAndView = new ModelAndView("update_user");
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        userUpdateRequest.setFirstName(userEntity.getFirstName());
        userUpdateRequest.setLastName(userEntity.getLastName());
        userUpdateRequest.setEmail(userEntity.getEmail());
        userUpdateRequest.setBirthday(userEntity.getBirthday().toString());
        userUpdateRequest.setUsername(userEntity.getUsername());
        userUpdateRequest.setGender(userEntity.getGender());
        modelAndView.addObject("userUpdateDTO", userUpdateRequest);
        return modelAndView;
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute UserUpdateRequest userUpdateRequest
            , Model model, HttpSession httpSession) {

        UserEntity userEntity = (UserEntity) httpSession.getAttribute("loggedUser");
        UserDto userDto = UserConverter.entityToDto(userEntity);
        List<UserGroup> userGroupRequests
                = userService.findUserJoinRequests(userEntity.getId());
        model.addAttribute("user", userDto);
        model.addAttribute("requests", userGroupRequests);
        model.addAttribute("userUpdateDTO", userUpdateRequest);

        try {
            userService.update(userEntity.getId(), userUpdateRequest);
            return "redirect:/users/profile";
        } catch (BadRequestException e) {
            model.addAttribute("error", "Sorry, that username was already taken");
            return "profile";
        }

    }

    @PostMapping("/group/{gid}")
    public String saveUserToGroup(@PathVariable Long gid, Filter filter, PageParams params, HttpSession httpSession, ModelMap model) {
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        userService.addUserToGroup(loggedUser.getId(), gid);

        Page<GroupDto> groupDtos = groupService.findALl(params, filter);
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());

        for (GroupDto groupDto : groupDtos) {
            List<UserDto> users = userService.findUsersInGroup(groupDto.getId());
            groupDto.setUsers(users);
        }
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("groups", groupDtos);
        model.addAttribute("groupRequests", groupRequests);
        return "discover_groups::groups";
    }

    @PostMapping("/activate/{uid}/group/{gid}")
    public String activateMembership(@PathVariable Long uid, @PathVariable Long gid, ModelMap model, PageParams params, HttpSession httpSession) {
        userService.activateUserMembership(uid, gid);

        UserEntity userEntity = (UserEntity) httpSession.getAttribute("loggedUser");
        UserGroupEntity groupEntity = groupService.findById(gid);
        if (groupEntity.getAdmin().getId() != userEntity.getId()) {
            model.addAttribute("error", "You are not this group's admin");
            return "profile::error";
        }

        UserDto userDto = UserConverter.entityToDto(userEntity);
        List<UserGroup> userGroupRequests = userService.findUserJoinRequests(userEntity.getId());
        model.addAttribute("user", userDto);
        model.addAttribute("requests", userGroupRequests);
        return "profile::requests";
    }

    @DeleteMapping("/delete/membership/{gid}")
    public String deleteMembership(@PathVariable Long gid, Filter filter,
                                   HttpSession httpSession, PageParams params, ModelMap model) {

        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        userService.deleteMembership(loggedUser.getId(), gid);

        Page<GroupDto> groupDtos = groupService.findALl(params, filter);
        List<Long> groupRequests = userService.groupsRequestedToJoin(loggedUser.getId());
        for (GroupDto groupDto : groupDtos) {
            List<UserDto> users = userService.findUsersInGroup(groupDto.getId());
            groupDto.setUsers(users);
        }
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("groups", groupDtos);
        model.addAttribute("groupRequests", groupRequests);
        return "discover_groups::groups";
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession httpSession) {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        CustomOauth2User principal
                = (CustomOauth2User) authentication.getPrincipal();
        ModelAndView modelAndView
                = new ModelAndView("profile");
        UserEntity userEntity
                = userService.findByEmail(principal.getEmail());

        httpSession.setAttribute("loggedUser", userEntity);
        UserDto userDto
                = UserConverter.entityToDto(userEntity);
        List<UserGroup> userGroupRequests
                = userService.findUserJoinRequests(userEntity.getId());
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("requests", userGroupRequests);

        return modelAndView;
    }

    @GetMapping("/postlogin")
    public ModelAndView postLogin(ModelMap map, @AuthenticationPrincipal CustomOauth2User user) {
        if (userService.userExists(user)) {
            return new ModelAndView("redirect:/users/profile", map);
        }
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        ModelAndView model = new ModelAndView("postlogin");
        model.addObject("userDTO", userCreateRequest);
        return model;
    }

    @PostMapping("/postlogin")
    public String postLogin(@Valid @ModelAttribute UserCreateRequest userCreateRequest
            , @AuthenticationPrincipal CustomOauth2User user, Model model) {
        if (userService.userExists(user)) {
            return "redirect:/users/profile";
        }
        //todo in a separate method
        userCreateRequest.setFirstName(user.getGivenName());
        userCreateRequest.setLastName(user.getLastName());
        userCreateRequest.setEmail(user.getEmail());

        try {
            userService.saveUser(userCreateRequest);
            return "redirect:/users/profile";
        } catch (BadRequestException e) {
            model.addAttribute("usernameError", "Username is already taken");
            model.addAttribute("userDTO", new UserCreateRequest());
            return "postlogin";
        }

    }

    @GetMapping("/groups")
    public ModelAndView viewSubscribedGroups(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("user_groups");
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<CategoryDto> categoryDtos
                = categoryService.findAll();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate = new GroupDtoForCreateUpdate();
        List<GroupDto> groupDtoList
                = userService.findGroupsOfUser(loggedUser.getId());
        modelAndView.addObject("groups", groupDtoList);
        modelAndView.addObject("groupCreateDto", groupDtoForCreateUpdate);
        modelAndView.addObject("categories", categoryDtos);
        modelAndView.addObject("loggedUser", loggedUser);
        return modelAndView;
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logoutPage(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ModelAndView("redirect:/users/login", map);
    }

    @GetMapping("/feed")
    public String displayFeed(HttpSession httpSession, Model model, PageParams params) {
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        Page<Feed> feed = userService.feed(loggedUser.getId(), params);

        if (params.getPageNumber() > feed.getTotalPages() - 1 || params.getPageNumber() < 0) {
            params.setPageNumber(0);
            feed = userService.feed(loggedUser.getId(), params);
        }
        model.addAttribute("feed", feed);
        return "feed";
    }


}
