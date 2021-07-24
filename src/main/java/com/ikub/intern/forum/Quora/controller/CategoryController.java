package com.ikub.intern.forum.Quora.controller;

import com.ikub.intern.forum.Quora.dto.category.CategoryCreateRequest;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.service.CategoryService;
import com.ikub.intern.forum.Quora.service.UserService;
import com.ikub.intern.forum.Quora.utils.LoggedUserUtil;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public String saveCategory(@RequestBody CategoryCreateRequest categoryCreateRequest,
                               Model model, HttpSession httpSession){
        UserDto loggedUser = LoggedUserUtil.getLoggedUserDto(httpSession);
        UserEntity createdBy = userService.find(loggedUser.getId());
        List<CategoryDto> categoryEntityList;
        try{
            if (!categoryCreateRequest.getName().isEmpty()){
                categoryService.saveCategory(categoryCreateRequest,createdBy);
            }
            categoryEntityList
                    = categoryService.findAll();
        } catch (Exception e){
            categoryEntityList
                    = categoryService.findAll();
            model.addAttribute("page",categoryEntityList);
            model.addAttribute("error","Cannot add duplicate category");
            return "all_categories::categories";
        }
        model.addAttribute("page",categoryEntityList);
        return "all_categories::categories";
    }

    @GetMapping
    public ModelAndView categoryEntityPage(){
        ModelAndView modelAndView = new ModelAndView("all_categories");
        List<CategoryDto> categoryEntityList
                = categoryService.findAll();
        modelAndView.addObject("page",categoryEntityList);
        return modelAndView;
    }


    @GetMapping("/{id}")
    @ResponseBody
    public CategoryDto findById(@PathVariable Long id){
        return categoryService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
}
