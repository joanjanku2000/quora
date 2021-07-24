package com.ikub.intern.forum.Quora.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Table(name="question_tags")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class QuestionTagRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="id_tag",referencedColumnName = "id")
    private TagEntity tag;

    @OneToOne
    @JoinColumn(name="id_question",referencedColumnName = "id")
    private QuestionEntity question;
}
