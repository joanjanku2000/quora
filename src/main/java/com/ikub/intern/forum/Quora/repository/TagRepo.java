package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagRepo extends JpaRepository<TagEntity,Long> {

    @Query("Select t from TagEntity t where t.id = :id and t.active = true")
    Optional<TagEntity> findById(@Param("id")Long id);

    @Query("Select t from TagEntity t where t.tagName = :name and t.active = true")
    Optional<TagEntity> findByTagName(@Param("name") String name);

}
