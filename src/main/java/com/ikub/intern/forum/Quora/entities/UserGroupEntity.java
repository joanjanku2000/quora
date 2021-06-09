package com.ikub.intern.forum.Quora.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
@SqlResultSetMapping(
        name = "GroupsOfUser",
        entities = @EntityResult(
                entityClass = UserGroupEntity.class,
                fields = {
                        @FieldResult(name = "id",column = "id"),
                        @FieldResult(name = "groupName",column = "group_name"),
                        @FieldResult(name = "description",column = "description"),
                        @FieldResult(name = "createdAt",column = "created_at"),
                        @FieldResult(name = "createdBy",column = "created_by"),
                        @FieldResult(name = "updatedBy",column = "updated_by"),
                        @FieldResult(name = "updatedAt",column = "updated_at"),
                        @FieldResult(name = "admin",column = "id_administrator"),
                        @FieldResult(name = "categoryEntity",column = "id_category"),
                        @FieldResult(name = "active",column = "active")
                }
        )
)

@NamedNativeQuery(
        name="groups_user",
        query =  "select * from user_group " +
                "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
                "where user_group_connection.active = true and user_group.active = true " +
                "and user_group_connection.id_user=?1 order by user_group.id ",
        resultSetMapping = "GroupsOfUser"
)
@NamedNativeQuery(
        name="groups_user_inactive",
        query =  "select * from user_group " +
                "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
                "where user_group_connection.active = false and user_group.active = true " +
                "and user_group_connection.id_user=?1 order by user_group.id ",
        resultSetMapping = "GroupsOfUser"
)
@Entity
@Table(name="user_group")
@Where(clause = "active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String groupName;
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name="created_by",referencedColumnName = "id")
    @JsonIgnore
    private UserEntity createdBy;
    @ManyToOne
    @JoinColumn(name="updated_by",referencedColumnName = "id")
    @JsonIgnore
    private UserEntity updatedBy;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @OneToOne
    @JoinColumn(name="id_administrator",referencedColumnName = "id")
    private UserEntity admin;
    @OneToOne
    @JoinColumn(name="id_category",referencedColumnName = "id")
    @JsonIgnoreProperties("createdBy")
    private CategoryEntity categoryEntity;
    @Column
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="id_group",referencedColumnName = "id")
    @Where(clause = "active=true")
   // @OrderBy("date DESC")
    private Set<QuestionEntity> questions;
}
