package com.ikub.intern.forum.Quora.repository.users;

import com.ikub.intern.forum.Quora.entities.Feed;
import com.ikub.intern.forum.Quora.dto.user.UserGroup;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


@Repository

public class UserRepoCustomImpl implements UserRepoCustom{
    @Autowired
    private EntityManager entityManager;
    private static final String ADD_USER_TO_GROUP_NATIVE = "INSERT into user_group_connection(id_user,id_group,active)" +
            "values (?1,?2,false)";
    private static final String ACTIVATE_USER_MEMBERSHIP = "" +
            "UPDATE user_group_connection SET active=true " +
            "where user_group_connection.id_user=?1 and user_group_connection.id_group=?2 ";
    private static final String FIND_ACTIVE_REGISTRATIONS = "select * from user_group_connection " +
            "where user_group_connection.id_user = ?1 and user_group_connection.id_group=?2 " +
            "and user_group_connection.active=true ";
    private static final String FIND_INACTIVE_REGISTRATIONS = "select * from user_group_connection " +
            "where user_group_connection.id_user = ?1 and user_group_connection.id_group=?2 " +
            "and user_group_connection.active=false ";
    private static final String DEACTIVATE_MEMBERSHIP = "UPDATE user_group_connection SET active=false " +
            "where user_group_connection.id_user=?1 and user_group_connection.id_group=?2 ";
    private static final String DELETE_MEMBERSHIP = "delete from user_group_connection  " +
            "where user_group_connection.id_user=?1 and user_group_connection.id_group=?2 ";
    private static final String FIND_GROUPS = "select * from user_group " +
            "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
            "where user_group_connection.active = true and user_group.active = true " +
            "and user_group_connection.id_user=?1";
    private static final String FIND_USERS = "select * from users " +
            "inner join user_group_connection on users.id = user_group_connection.id_user " +
            "where user_group_connection.active = true " +
            "and user_group_connection.id_group=?1";
    private static final  String FEED_COUNT = "select count( distinct question.id) from user_group "+
            "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
            "inner join users on users.id = user_group_connection.id_user " +
            "inner join question on question.id_group=user_group.id " +
            "inner join upvotes_question on upvotes_question.id_question = question.id " +
            "where users.id = ?1 and user_group.active = true and user_group_connection.active=true and question.active=true ";
    @Override
    public int addUserToGroup(Long userId, Long groupId) {
        Query query = entityManager.createNativeQuery(ADD_USER_TO_GROUP_NATIVE)
                .setParameter(1,userId)
                .setParameter(2,groupId);
        return query.executeUpdate();
    }

    @Override
    public int activateUserMembership(Long userId, Long groupId) {
        Query query = entityManager.createNativeQuery(ACTIVATE_USER_MEMBERSHIP)
                .setParameter(1,userId)
                .setParameter(2,groupId);
        return query.executeUpdate();
    }

    @Override
    public boolean membershipExistsAsActive(Long userId, Long groupId) {
        Query query = entityManager.createNativeQuery(FIND_ACTIVE_REGISTRATIONS)
                .setParameter(1,userId)
                .setParameter(2,groupId);
        return query.getResultList().size()>0;
    }

    @Override
    public boolean membershipExistsAsInActive(Long userId, Long groupId) {
        Query query = entityManager.createNativeQuery(FIND_INACTIVE_REGISTRATIONS)
                .setParameter(1,userId)
                .setParameter(2,groupId);
        return query.getResultList().size()>0;
    }

    @Override
    public int deleteMembership(Long userId, Long groupId) {
        Query query = entityManager.createNativeQuery(DELETE_MEMBERSHIP)
                .setParameter(1,userId)
                .setParameter(2,groupId);
        return query.executeUpdate();
    }
    @Override
    public List<UserGroup> findAllRequestsToGroups(Long uid){
        List list =
                entityManager.createNamedQuery("UserReqs")
                        .setParameter("id",uid).getResultList();
        return list;
    }
    public List<UserGroupEntity> findGroupsOfUser(Long uid){
        return entityManager.createNamedQuery("groups_user")
                .setParameter(1,uid).getResultList();
    }
    @Override
    public List findUsersInGroup(Long gid){
        return entityManager.createNamedQuery("user_group")
                .setParameter(1,gid).getResultList();
    }

    @Override
    public List<UserGroupEntity> findGroupsRequest(Long uid) {
        return entityManager.createNamedQuery("groups_user_inactive")
                .setParameter(1,uid).getResultList();
    }

    @Override
    public Page<Feed> feed(Long uid, Pageable pageable) {
        BigInteger size =
                (BigInteger) entityManager.createNativeQuery(FEED_COUNT)
                        .setParameter(1,uid).getSingleResult();
        long total = size.longValue();
        List feeds = entityManager.createNamedQuery("feed")
                .setParameter(1,uid)
                .setFirstResult((pageable.getPageNumber())*pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new PageImpl<>(feeds,pageable,total);
    }


}
