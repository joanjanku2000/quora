package com.ikub.intern.forum.Quora.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoMini;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String gender;
    private LocalDate birthday;
    private String userRole;
    private Set<GroupDtoMini> userGroupEntityList;

    public UserDto(Long id, String firstName, String lastName,
                   String email, String username, String gender,
                   LocalDate birthday, String userRole, Set<GroupDtoMini> userGroupEntityList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.userRole = userRole;
        //this.userGroupEntityList = userGroupEntityList;
    }
}
