package com.ikub.intern.forum.Quora.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import com.ikub.intern.forum.Quora.utils.Provider;
@Entity
@Proxy(lazy = false)
@Table(name="users")
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
                                        @ColumnResult(name="user_id"),
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
                                        @FieldResult(name = "id",column = "id"),
                                        @FieldResult(name = "firstName",column = "first_name"),
                                        @FieldResult(name = "lastName",column = "last_name"),
                                        @FieldResult(name = "email",column = "email"),
                                        @FieldResult(name = "gender",column = "gender"),
                                        @FieldResult(name = "username",column = "username"),
                                        @FieldResult(name = "birthday",column = "birthday"),
                                        @FieldResult(name = "userRole",column = "user_role"),
                                        @FieldResult(name = "createdAt",column = "created_at"),
                                        @FieldResult(name = "updatedAt",column = "updated_at"),
                                        @FieldResult(name = "active",column = "active")
                                }
                        )
                )

        }
)
@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "UserReqs",
                        query =
                                "select user_group_connection.id_user as user_id ,users.username as username , " +
                                        "user_group_connection.id_group as group_id,user_group.group_name as group_name  from user_group_connection " +
                                        "inner join user_group on user_group.id =  user_group_connection.id_group " +
                                        " inner join users on user_group_connection.id_user = users.id "+
                                        "where user_group_connection.id_group in " +
                                        "(select user_group.id from user_group where user_group.id_administrator = :id) " +
                                        "and user_group_connection.active=false " +
                                        "and user_group.active = true",

                        resultSetMapping = "UserRequests"),
                @NamedNativeQuery(
                        name="user_group",
                        query =  "select * from users " +
                                "inner join user_group_connection on users.id = user_group_connection.id_user " +
                                "where user_group_connection.active = true " +
                                "and user_group_connection.id_group=?1 order by users.id",
                        resultSetMapping = "UsersOfGroup"
                )


        }
)


//@AllArgsConstructor -> krijoje vete konstruktorin per ta perdorur per krijim
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
//
//    @WhereJoinTable(clause = "active=true")
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name="user_group_connection",
//            joinColumns = @JoinColumn(name="id_user"),
//            inverseJoinColumns = @JoinColumn(name="id_group")
//    )
//
//    @JsonIgnoreProperties({"users","createdBy","updatedBy","admin","questions"})
//    @Where(clause = "active=true") //group is not deleted
//    private Set<UserGroupEntity> userGroupEntityList;
}
