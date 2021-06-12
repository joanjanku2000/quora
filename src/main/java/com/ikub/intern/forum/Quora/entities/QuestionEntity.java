package com.ikub.intern.forum.Quora.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "active=true")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_group", referencedColumnName = "id")
    private UserGroupEntity group;

    @Column
    private LocalDateTime date;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "id_question"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<TagEntity> tagList;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_question",
            referencedColumnName = "id"
    )
    private Set<UpvotesQuestion> upvotesQuestion;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @Where(clause = "active=true")
    private Set<ReplyEntity> replies;

    public QuestionEntity(String question, UserEntity user, UserGroupEntity group,
                          LocalDateTime date, boolean active, Set<TagEntity> tagList) {
        this.question = question;
        this.user = user;
        this.group = group;
        this.date = date;
        this.active = active;
        this.tagList = tagList;
    }
}

