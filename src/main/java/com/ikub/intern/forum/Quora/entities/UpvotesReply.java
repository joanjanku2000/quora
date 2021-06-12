package com.ikub.intern.forum.Quora.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="upvotes_reply")
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "active=true")
public class UpvotesReply extends Upvotes {

    @ManyToOne
    @JoinColumn(name="id_reply",referencedColumnName = "id")
    private ReplyEntity reply;

    public UpvotesReply(UserEntity user, boolean active, LocalDateTime upvoteDate, ReplyEntity reply) {
        super(user, active, upvoteDate);
        this.reply = reply;
    }
}
