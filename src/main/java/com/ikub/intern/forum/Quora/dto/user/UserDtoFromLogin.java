package com.ikub.intern.forum.Quora.dto.user;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDtoFromLogin {
    private Long id;
    private String name;
    private String given_name;
    private String  family_name;
    private String   link;
    private String  picture;
    private String    gender;
    private String    birthday;
    private String   locale;
}
