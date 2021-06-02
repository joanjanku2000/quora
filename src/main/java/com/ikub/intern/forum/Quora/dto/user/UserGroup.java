package com.ikub.intern.forum.Quora.dto.user;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class UserGroup {
    private Integer userId;
    private String username;
    private Integer groupId;
    private String groupName;

    public UserGroup(Integer userId, String username, Integer groupId, String groupName) {
        this.userId = userId;
        this.username = username;
        this.groupId = groupId;
        this.groupName = groupName;
    }


}
