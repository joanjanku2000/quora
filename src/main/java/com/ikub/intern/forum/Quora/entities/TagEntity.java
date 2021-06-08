package com.ikub.intern.forum.Quora.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "active=true")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tagName;
    @ManyToOne
    @JoinColumn(name="created_by",referencedColumnName = "id")
    private UserEntity createdBy;
    @Column
    private LocalDateTime createdAt;
    @Column
    private boolean active;

    public TagEntity(String tagName, UserEntity createdBy, LocalDateTime createdAt, boolean active) {
        this.tagName = tagName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active;
    }
}
