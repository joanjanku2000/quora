package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepo extends JpaRepository<UserGroupEntity,Long> {
    @Query("Select g from UserGroupEntity g where g.id = :id and g.active=true")
     Optional<UserGroupEntity> findById(@Param("id")Long id);

    @Query("Select g from UserGroupEntity g where g.groupName = :name and g.active=true")
     Optional<UserGroupEntity> findByGroupName(@Param("name") String name);

    @Query("Select g from UserGroupEntity g where g.active=true")
     Page<UserGroupEntity> findAll(Pageable pageable);

    @Query("Select g from UserGroupEntity g where g.active=true and g.categoryEntity.categoryName=:name")
    Page<UserGroupEntity> findAllByCategoryEntityCategoryNameAndActiveTrue(@Param("name") String name,Pageable pageable);

    @Query("Select g from UserGroupEntity g where g.active=true and g.groupName like concat('%',:name,'%') ")
    Page<UserGroupEntity> findAllByGroupNameAndActiveTrue(@Param("name") String name,Pageable pageable);

}
