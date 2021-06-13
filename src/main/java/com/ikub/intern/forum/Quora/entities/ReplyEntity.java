package com.ikub.intern.forum.Quora.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "reply")
@Where(clause = "active=true")
@NoArgsConstructor
@Getter
@Setter
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reply;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_question", referencedColumnName = "id")
    private QuestionEntity question;

    @Column
    private boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reply", fetch = FetchType.EAGER)
   // @Where(clause = "active=true")
    private Set<UpvotesReply> upvotesReplyList;

    public ReplyEntity(UserEntity user, QuestionEntity question,
                       String reply, boolean active, LocalDateTime createdAt) {
        this.user = user;
        this.question = question;
        this.reply = reply;
        this.active = active;
        this.createdAt = createdAt;
    }
}
