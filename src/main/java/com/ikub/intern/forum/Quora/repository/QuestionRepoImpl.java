package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class QuestionRepoImpl implements QuestionRepoCustom {
    @Autowired
    private EntityManager entityManager;
    private static String FIND_ALL_IN_GROUP = "SELECT question from QuestionEntity question" +
            " where question.group.id=?1 and question.active = true order by question.date";

    @Override
    public List<QuestionEntity> findAllByGroup(Long id) {
        TypedQuery<QuestionEntity> questionQuery = entityManager
                .createQuery(FIND_ALL_IN_GROUP,QuestionEntity.class)
                .setParameter(1,id);
        return questionQuery.getResultList();
    }

}
