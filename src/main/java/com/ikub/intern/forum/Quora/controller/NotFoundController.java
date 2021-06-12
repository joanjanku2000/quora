package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class NotFoundController implements ErrorController {
    @Autowired
    private UserService userService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model,HttpSession httpSession){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status!=null){
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()){
                UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
                List<UserGroup> userGroupRequests
                        = userService.findUserJoinRequests(loggedUser.getId());
                model.addAttribute("user", loggedUser);
                model.addAttribute("requests", userGroupRequests);
                return "profile";
            }
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
