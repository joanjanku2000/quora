package com.ikub.intern.forum.Quora.entities;

import com.ikub.intern.forum.Quora.utils.NativeQueries;
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
        query = NativeQueries.FEED_QUERY,
        resultClass = Feed.class
)
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
