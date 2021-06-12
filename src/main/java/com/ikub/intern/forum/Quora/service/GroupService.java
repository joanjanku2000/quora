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
        Optional<CategoryEntity> categoryEntity = categoryRepo
                .findById(groupDtoForCreate.getCategoryId());
        Optional<UserEntity> admin = userRepo.findById(adminId);

        if (!admin.isPresent()){
            throw new NotFoundException("Admin not found");
        }

        if (!categoryEntity.isPresent()) {
            throw new BadRequestException("The category doesn't exist");
        }
        if (groupRepo.findByGroupName(groupDtoForCreate.getGroupName().trim()).isPresent()){
           throw new BadRequestException("A Group with the same name already exists");
        }
        UserGroupEntity groupEntity
                = GroupConverter.toEntity(groupDtoForCreate,categoryEntity.get());
        groupEntity.setAdmin(admin.get());

        UserGroupEntity savedGroup = groupRepo.save(groupEntity);

        userRepo.addUserToGroup(adminId,savedGroup.getId());
        userRepo.activateUserMembership(adminId,savedGroup.getId());
    }
    public GroupDto updateGroup(Long id,GroupDtoForCreateUpdate groupDto){
        Optional<UserGroupEntity> groupEntity = groupRepo.findById(id);
        Optional<CategoryEntity> categoryEntity = categoryRepo.findById(groupDto.getCategoryId());

        if (!groupEntity.isPresent()){
            throw new BadRequestException("The group with this id doesn't exist");
        }
        if (!categoryEntity.isPresent()) {
            throw new BadRequestException("The category with this id doesn't exist");
        }
        if (groupRepo.findByGroupName(groupDto.getGroupName()).isPresent()){
            throw new BadRequestException("The name is already taken" +
                    ",please try a different one");
        }
        groupEntity.get().setDescription(groupDto.getDescription());
        groupEntity.get().setGroupName(groupDto.getGroupName());
        groupEntity.get().setCategoryEntity(categoryEntity.get());
        return GroupConverter.entityToDto(groupRepo.save(groupEntity.get()));
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteGroup(Long id){
        UserGroupEntity groupEntity = groupRepo.findById(id).orElse(null);
        if (groupEntity==null) {
            throw new BadRequestException("The group with the given id does not exist");
        }
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
