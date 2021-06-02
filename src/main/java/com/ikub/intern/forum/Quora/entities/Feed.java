package com.ikub.intern.forum.Quora.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import java.math.BigInteger;
import java.time.LocalDateTime;
@NamedNativeQuery(
        name = "feed",
        query ="select distinct question.id, user_group.group_name, question.question,question.date,count(upvotes_question.id) over (partition by question.id) as count from user_group "+
                "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
                "inner join users on users.id = user_group_connection.id_user " +
                "inner join question on question.id_group=user_group.id " +
                "inner join upvotes_question on upvotes_question.id_question = question.id " +
                "where users.id = ?1 and user_group.active = true and user_group_connection.active=true and question.active=true " +
                "order by question.date desc",

        resultClass = Feed.class)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Feed {
    @Id
    private Integer id;
    private String groupName;
    private String question;
    private LocalDateTime date;
    private BigInteger count;

    public Feed(Integer questionId, String groupName, String question, LocalDateTime date, BigInteger numberOfUpvotes) {
        this.id = questionId;
        this.groupName = groupName;
        this.question = question;
        this.date = date;
        this.count = numberOfUpvotes;
    }
}
