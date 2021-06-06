package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.converter.UserConverter;
import com.ikub.intern.forum.Quora.entities.Feed;
import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.user.*;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
//import com.ikub.intern.forum.Quora.security.CustomOAuth2User;
import com.ikub.intern.forum.Quora.security.oauth.CustomOauth2User;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService  {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserGroupRepo groupRepo;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public List<UserEntity> findAll(){
        return userRepo.findAll();
    }
    public UserEntity find(Long id){
        return userRepo.getOne(id);
    }
    public void saveUser(UserCreateRequest userCreateRequest){
        if (emailExists(userCreateRequest.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        if (usernameExists(userCreateRequest.getUsername())){
            throw new BadRequestException("The username already exists");
        }
        UserEntity userEntity = UserConverter.toEntity(userCreateRequest);
        logger.info("Saving user {}",userEntity);
        userRepo.save(userEntity);
    }
    private boolean emailExists(String email){
        return userRepo.findByEmail(email)!=null;
    }
    private boolean usernameExists(String username){
        return userRepo.findByUsername(username)!=null;
    }
    public UserEntity findByEmail(String email){
        return userRepo.findByEmail(email);
    }
    public void update(Long id, UserUpdateRequest userUpdateRequest){
           Optional<UserEntity> userToBeUpdated = userRepo.findById(id);
           if (!userToBeUpdated.isPresent())
               throw new BadRequestException("The user with the given id does not exist");

            if (!userToBeUpdated.get().getUsername().equals(userUpdateRequest.getUsername())){
                if(usernameExists(userUpdateRequest.getUsername())){
                    throw new BadRequestException("Sorry, this username already exists");
                }
            }
           UserConverter.updateDtoToEntity(userToBeUpdated.get(), userUpdateRequest);
           if (!userToBeUpdated.get().getEmail().equals(userUpdateRequest.getEmail())){
               if (emailExists(userUpdateRequest.getEmail())){
                   throw new BadRequestException("Sorry, this email is already registered");
               }
           }
           logger.info("User updated to {}",userToBeUpdated.get());
           userRepo.save(userToBeUpdated.get());
    }
    public void deleteUser(Long id){
        Optional<UserEntity> userToBeDeleted = userRepo.findById(id);
        if (!userToBeDeleted.isPresent()){
            throw new BadRequestException("The user with the given id does not exist");
        }
        userToBeDeleted.get().setActive(false);
        userRepo.save(userToBeDeleted.get());
        logger.info("User deleted {}",userToBeDeleted.get().isActive());
    }
    public void addUserToGroup(Long userId,Long groupId){
        UserEntity userEntity = userRepo.findById(userId).orElse(null);
        UserGroupEntity groupEntity = groupRepo.findById(groupId).orElse(null);
        if (userEntity==null || groupEntity==null)
            throw new BadRequestException("The user or the group doesn't exist");
        if (userRepo.membershipExistsAsActive(userId,groupId)){
            throw new BadRequestException("You're already joined in this group");
        }
        if (userRepo.membershipExistsAsInActive(userId,groupId)){
            userRepo.activateUserMembership(userId,groupId);
        } else{
            userRepo.addUserToGroup(userId,groupId);
        }
        logger.info("Added user {} to group {}",userEntity,groupEntity);

    }
    public void activateUserMembership(Long userId,Long groupId){

        Optional<UserEntity> userEntity = userRepo.findById(userId);
        Optional<UserGroupEntity> groupEntity = groupRepo.findById(groupId);

        if (!userEntity.isPresent() || !groupEntity.isPresent()){
            throw new BadRequestException("The user or the group doesn't exist");
        }
        userRepo.activateUserMembership(userId,groupId);
        logger.info("Activated membership of user"+userId+ " to group "+groupId);
    }
    public void deleteMembership(Long userId,Long groupId){
        Optional<UserEntity> userEntity = userRepo.findById(userId);
        Optional<UserGroupEntity> groupEntity = groupRepo.findById(groupId);

        if (!userEntity.isPresent() || !groupEntity.isPresent()) {
            throw new BadRequestException("The user or the group doesn't exist");
        }
        if (userRepo.deleteMembership(userId,groupId)<=0){
            throw new BadRequestException("The deletion could not be made");
        }

        logger.info("Deleted membership of user"+userId+ " to group "+groupId);
    }
    public boolean userExists(CustomOauth2User user) {
        UserEntity existingUser = userRepo.findByEmail(user.getEmail());
        System.out.println(user.getAttributes());

        if (existingUser == null) {
            return false;
        } else {
            logger.info("Existing user logged");
           return true;
        }
    }
    public List<UserGroup> findUserJoinRequests(Long uid){
        return userRepo.findAllRequestsToGroups(uid);
    }
    public List<UserDto> findUsersInGroup(Long gid){
        return UserConverter.entityListToDtoList((userRepo.findUsersInGroup(gid)));
    }
    public List<GroupDto> findGroupsOfUser(Long uid){
        return GroupConverter.entityListToDtoList(userRepo.findGroupsOfUser(uid));
    }
    public List<Long> groupsRequestedToJoin(Long id){
        List<UserGroupEntity> groupEntities = userRepo.findGroupsRequest(id);
        List<Long> ids = new ArrayList<>();
        for (UserGroupEntity userGroupEntity : groupEntities){
            ids.add(userGroupEntity.getId());
        }
        return ids;
    }
    public Page<Feed> feed(Long uid, PageParams params){
        return userRepo.feed(uid, PageRequest.of(params.getPageNumber(),params.getPageSize()));
    }
}