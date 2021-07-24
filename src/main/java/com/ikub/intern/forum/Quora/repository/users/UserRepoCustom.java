package com.ikub.intern.forum.Quora.repository.users;

import com.ikub.intern.forum.Quora.entities.Feed;
import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserRepoCustom {
     int addUserToGroup(Long userId,Long groupId);
     int activateUserMembership(Long userId,Long groupId);
     boolean membershipExistsAsActive(Long userId,Long groupId);
     boolean membershipExistsAsInActive(Long userId,Long groupId);
     int deleteMembership(Long userId,Long groupId);
     List<UserGroup> findAllRequestsToGroups(Long uid);
     List<UserGroupEntity> findGroupsOfUser(Long uid);
     List<UserEntity> findUsersInGroup(Long gid);
     List<UserGroupEntity> findGroupsRequest(Long uid);
     Page<Feed> feed(Long uid, Pageable pageable);
}
