package com.ikub.intern.forum.Quora.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="upvotes_question")
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "active=true")
public class UpvotesQuestion extends Upvotes {
    @ManyToOne
    @JoinColumn(name="id_question",referencedColumnName = "id")
    private QuestionEntity question;

    public UpvotesQuestion(UserEntity user, boolean active, LocalDateTime upvoteDate, QuestionEntity question) {
        super(user, active, upvoteDate);
        this.question = question;
    }
}
