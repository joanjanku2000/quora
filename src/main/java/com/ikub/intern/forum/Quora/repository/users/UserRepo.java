package com.ikub.intern.forum.Quora.repository.users;

import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity,Long>,UserRepoCustom{
    @Query("Select user from UserEntity user where user.username = :username and user.active = true")
    UserEntity findByUsername(@Param("username") String username);
    @Query("Select user from UserEntity user where user.email = :email and user.active = true")
    UserEntity findByEmail(@Param("email") String email);

}
