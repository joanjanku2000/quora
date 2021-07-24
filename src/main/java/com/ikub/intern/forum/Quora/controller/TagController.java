package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.tag.TagDto;
import com.ikub.intern.forum.Quora.dto.tag.TagDtoForCreate;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.service.TagService;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String findAll(Model model){
        List<TagDto> tagDtoList = tagService.findALl();
        model.addAttribute("tags",tagDtoList);
        return "tags";
    }
    @PostMapping("/save")
    public String save(@Valid @RequestBody TagDtoForCreate tagDtoForCreate, Model model, HttpSession httpSession){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        UserEntity createdBy = userService.find(loggedUser.getId());
        List<TagDto> tagDtoList;
     try {
         if (!tagDtoForCreate.getTagName().isEmpty())
         tagService.saveTag(tagDtoForCreate,createdBy);
         tagDtoList = tagService.findALl();
         model.addAttribute("tags",tagDtoList);
     } catch (NotAllowedException e){
         tagDtoList = tagService.findALl();
         model.addAttribute("tags",tagDtoList);
         model.addAttribute("error","Cannot add 2 tags with the same name");
         return "tags::tags";
     }
     return "tags::tags";
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        tagService.deleteTag(id);
    }
    @GetMapping("/{id}")
    public TagEntity findById(@PathVariable Long id){
        return tagService.findById(id);
    }
}
