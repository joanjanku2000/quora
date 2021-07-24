package com.ikub.intern.forum.Quora.entities;

import com.ikub.intern.forum.Quora.dto.group.GroupWithMostAskedQuestion;
import com.ikub.intern.forum.Quora.dto.user.MostActiveUsersInGroup;
import com.ikub.intern.forum.Quora.utils.NativeQueries;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "GroupsOfUser",
                entities = @EntityResult(
                        entityClass = UserGroupEntity.class,
                        fields = {
                                @FieldResult(name = "id", column = "id"),
                                @FieldResult(name = "groupName", column = "group_name"),
                                @FieldResult(name = "description", column = "description"),
                                @FieldResult(name = "createdAt", column = "created_at"),
                                @FieldResult(name = "createdBy", column = "created_by"),
                                @FieldResult(name = "updatedBy", column = "updated_by"),
                                @FieldResult(name = "updatedAt", column = "updated_at"),
                                @FieldResult(name = "admin", column = "id_administrator"),
                                @FieldResult(name = "categoryEntity", column = "id_category"),
                                @FieldResult(name = "active", column = "active")
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "groupsWithMostQuestions",
                classes = @ConstructorResult(
                        targetClass = GroupWithMostAskedQuestion.class,
                        columns = {
                                @ColumnResult(name = "group_name"),
                                @ColumnResult(name = "total_questions")
                        }
                )

        ),
        @SqlResultSetMapping(
                name = "mostActiveUsers",
                classes = @ConstructorResult(
                        targetClass = MostActiveUsersInGroup.class,
                        columns = {
                                @ColumnResult(name = "username"),
                                @ColumnResult(name = "total_questions")
                        }
                )
        )
}
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "groups_user",
                query = NativeQueries.GROUPS_OF_USER_ACTIVE,
                resultSetMapping = "GroupsOfUser"
        ),
        @NamedNativeQuery(
                name = "groups_user_inactive",
                query = NativeQueries.GROUPS_OF_USER_INACTIVE,
                resultSetMapping = "GroupsOfUser"
        ),
        @NamedNativeQuery(
                name = "GROUPS_WITH_MOST_QUESTIONS",
                query = NativeQueries.GROUP_WHERE_ASKED_MOST_QUESTIONS,
                resultSetMapping = "groupsWithMostQuestions"
        ),
        @NamedNativeQuery(
                name = "MOST_ACTIVE_USERS",
                query = NativeQueries.MOST_ACTIVE_USERS_IN_GROUP,
                resultSetMapping = "mostActiveUsers"
        )
}
)
@Entity
@Table(name = "user_group")
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
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private UserEntity updatedBy;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "id_administrator", referencedColumnName = "id")
    private UserEntity admin;

    @OneToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private CategoryEntity categoryEntity;

    @Column
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_group", referencedColumnName = "id")
    private Set<QuestionEntity> questions;

    public UserGroupEntity(String groupName, String description,
                           LocalDateTime createdAt, CategoryEntity categoryEntity, boolean active) {
        this.groupName = groupName;
        this.description = description;
        this.createdAt = createdAt;
        this.categoryEntity = categoryEntity;
        this.active = active;
    }
}
