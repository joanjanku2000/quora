package com.ikub.intern.forum.Quora.entities;

import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.utils.NativeQueries;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Proxy(lazy = false)
@Table(name = "users")
@Where(clause = "active=true") //soft deletion handling
@Getter
@Setter
@ToString
@NoArgsConstructor
@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "UserRequests",
                        classes = @ConstructorResult(
                                targetClass = UserGroup.class,
                                columns = {
                                        @ColumnResult(name = "user_id"),
                                        @ColumnResult(name = "username"),
                                        @ColumnResult(name = "group_id"),
                                        @ColumnResult(name = "group_name")
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "UsersOfGroup",
                        entities = @EntityResult(
                                entityClass = UserEntity.class,
                                fields = {
                                        @FieldResult(name = "id", column = "id"),
                                        @FieldResult(name = "firstName", column = "first_name"),
                                        @FieldResult(name = "lastName", column = "last_name"),
                                        @FieldResult(name = "email", column = "email"),
                                        @FieldResult(name = "gender", column = "gender"),
                                        @FieldResult(name = "username", column = "username"),
                                        @FieldResult(name = "birthday", column = "birthday"),
                                        @FieldResult(name = "userRole", column = "user_role"),
                                        @FieldResult(name = "createdAt", column = "created_at"),
                                        @FieldResult(name = "updatedAt", column = "updated_at"),
                                        @FieldResult(name = "active", column = "active")
                                }
                        )
                )

        }
)
@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "UserReqs",
                        query = NativeQueries.USER_REQUESTS_NATIVE_QUERY,

                        resultSetMapping = "UserRequests"),
                @NamedNativeQuery(
                        name = "user_group",
                        query = NativeQueries.USERS_OF_GROUP,
                        resultSetMapping = "UsersOfGroup"
                )


        }
)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    @NotBlank
    private String firstName;
    @Column
    @NotNull
    @NotBlank
    private String lastName;
    @Column
    @NotNull
    @NotBlank
    private String email;
    @Column
    @NotNull
    @NotBlank
    private String gender;
    @Column
    @NotNull
    @NotBlank
    private String username;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotBlank
    private LocalDate birthday;
    @Column
    private LocalDateTime createdAt;
    @Column
    @NotBlank
    private LocalDateTime updatedAt;
    @Column
    @NotNull
    private String userRole;
    @Column
    @NotNull
    private boolean active;

    public UserEntity(String firstName, String lastName, String email, String gender, String username, LocalDate birthday, LocalDateTime createdAt, String userRole, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.username = username;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.userRole = userRole;
        this.active = active;
    }
}
