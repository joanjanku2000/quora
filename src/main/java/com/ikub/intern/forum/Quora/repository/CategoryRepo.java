package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    @Query("Select c from CategoryEntity c where c.id = :id and c.active=true")
    Optional<CategoryEntity> findById(@Param("id") Long id);
    CategoryEntity findByCategoryName(String categoryName);
}
