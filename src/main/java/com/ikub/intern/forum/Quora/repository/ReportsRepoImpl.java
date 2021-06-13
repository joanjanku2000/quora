package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.dto.user.MostActiveUsersInGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ReportsRepoImpl implements ReportsRepo {

    private final EntityManager entityManager;

    public ReportsRepoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<MostActiveUsersInGroup> findMostActiveUsersInGroupList(Long groupId) {
        return entityManager.createNamedQuery("MOST_ACTIVE_USERS")
                .setParameter(1,groupId)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List findGroupsWithMostAskedQuestions(Long userId) {
        return entityManager.createNamedQuery("GROUPS_WITH_MOST_QUESTIONS")
                .setParameter(1,userId)
                .setMaxResults(5)
                .getResultList();
    }
    @Override
    public List findMostUpvotedQuestionsOfUser(Long userId){
        Query query =
                entityManager.createNamedQuery("MOST_UPVOTED_QUESTIONS")
                        .setParameter(1,userId)
                        .setMaxResults(5);
        return query.getResultList();
    }
}
