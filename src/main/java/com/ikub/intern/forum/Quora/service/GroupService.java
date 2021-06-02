package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.CategoryRepo;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.Filter;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private UserGroupRepo groupRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuestionsRepo questionRepo;
    @Transactional
    public void createGroup(Long adminId,GroupDtoForCreateUpdate groupDtoForCreate){
        CategoryEntity categoryEntity = categoryRepo
                .findById(groupDtoForCreate.getCategoryId())
                .orElse(null);
        Optional<UserEntity> admin = userRepo.findById(adminId);
        if (!admin.isPresent()){
            throw new NotFoundException("Admin not found");
        }

        if (categoryEntity==null) throw new BadRequestException("The category doesn't exist");
        if (groupRepo.findByGroupName(groupDtoForCreate.getGroupName()).isPresent()){
           throw new BadRequestException("A Group with the same name already exists");
        }
        UserGroupEntity groupEntity = GroupConverter.toEntity(groupDtoForCreate,categoryEntity);
        groupEntity.setAdmin(admin.get());
        groupRepo.save(groupEntity);
        Optional<UserGroupEntity> savedGroup = groupRepo.findByGroupName(groupEntity.getGroupName());
        userRepo.addUserToGroup(adminId,savedGroup.get().getId());
        userRepo.activateUserMembership(adminId,savedGroup.get().getId());

    }
    public void updateGroup(Long id,GroupDtoForCreateUpdate groupDto){
        UserGroupEntity groupEntity = groupRepo.findById(id).orElse(null);
        CategoryEntity categoryEntity = categoryRepo.findById(groupDto.getCategoryId()).orElse(null);
        if (groupEntity==null) throw new BadRequestException("The group with this id doesn't exist");
        if (categoryEntity==null) throw new BadRequestException("The category with this id doesn't exist");
        if (groupRepo.findByGroupName(groupDto.getGroupName()).isPresent())
            throw new BadRequestException("The name is already taken" +
                ",please try a different one");
        groupEntity.setDescription(groupDto.getDescription());
        groupEntity.setGroupName(groupDto.getGroupName());
        groupEntity.setCategoryEntity(categoryEntity);
    }

    @Transactional
    public void deleteGroup(Long id){
        UserGroupEntity groupEntity = groupRepo.findById(id).orElse(null);
        if (groupEntity==null) throw new BadRequestException("The group with the given id does not exist");
        groupEntity.setActive(false);
        for (QuestionEntity questionEntity : groupEntity.getQuestions()){
            questionEntity.setActive(false);
            questionRepo.save(questionEntity);
        }
        groupRepo.save(groupEntity);
    }
    public UserGroupEntity findById(Long id){
        UserGroupEntity groupEntity = groupRepo.findById(id).orElse(null);
        if (groupEntity==null) throw new BadRequestException("The group with the given id does not exist");
        return groupEntity;
    }

    public Page<GroupDto> findALl(PageParams pageParams, Filter filter){

       if ((filter.getCategory()==null || filter.getCategory().isEmpty()) && (filter.getName()==null || filter.getName().isEmpty()) )
        return GroupConverter.entityPageToDtoPage(
                groupRepo.findAll(PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSort(),
                pageParams.getSortField())));
       else if (filter.getName()!=null && !filter.getName().isEmpty()) {
           return GroupConverter.entityPageToDtoPage(
                   groupRepo.findAllByGroupNameAndActiveTrue(
                           filter.getName(),PageRequest.of(
                           pageParams.getPageNumber(),
                           pageParams.getPageSize(),
                           pageParams.getSort(),
                           pageParams.getSortField())));
       }

           return GroupConverter.entityPageToDtoPage(
                   groupRepo.findAllByCategoryEntityCategoryNameAndActiveTrue(
                           filter.getCategory(),PageRequest.of(
                                   pageParams.getPageNumber(),
                                   pageParams.getPageSize(),
                                   pageParams.getSort(),
                                   pageParams.getSortField())));

    }

}
