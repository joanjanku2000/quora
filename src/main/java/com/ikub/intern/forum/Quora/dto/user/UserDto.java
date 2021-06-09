package com.ikub.intern.forum.Quora.dto.user;


import lombok.*;
import java.time.LocalDate;


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

    public UserDto(Long id, String firstName, String lastName,
                   String email, String username, String gender,
                   LocalDate birthday, String userRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.userRole = userRole;
    }
}
