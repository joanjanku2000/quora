package com.ikub.intern.forum.Quora.dto.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotNull
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @NotNull
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;
    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotNull
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotNull
    @NotEmpty(message = "Gender cannot be empty")
    private String gender;
    @NotNull
    @NotEmpty(message = "Birthday cannot be empty")
    private String birthday;

}
