package com.ikub.intern.forum.Quora.entities;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="category")
@Where(clause = "active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String categoryName;
    @ManyToOne
    @JoinColumn(name="created_by",referencedColumnName = "id")
    private UserEntity createdBy;
    @Column
    private LocalDateTime createdAt;
    @Column
    private boolean active;
}
