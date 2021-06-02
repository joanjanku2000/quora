package com.ikub.intern.forum.Quora.dto.user;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor// vendos @Valid te controlleri
@ToString
public class UserCreateRequest {
    @NotNull
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotNull
    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;
    @NotNull
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotNull
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotNull
    @NotBlank(message = "Gender cannot be empty")
    private String gender;
    @NotNull
    @NotBlank(message = "Birthday cannot be empty")
    private String birthday;
//    @NotNull
//    @NotBlank(message = "Password cannot be empty")
//    @Size(min = 1,max = 5,message = "Password should be at least 1 character in length")
//    private String password;
}
