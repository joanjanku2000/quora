package com.ikub.intern.forum.Quora.utils;

public class NativeQueries {
    public static final String USER_REQUESTS_NATIVE_QUERY = "select user_group_connection.id_user as user_id ,users.username as username , " +
            "user_group_connection.id_group as group_id,user_group.group_name as group_name  from user_group_connection " +
            "inner join user_group on user_group.id =  user_group_connection.id_group " +
            " inner join users on user_group_connection.id_user = users.id " +
            "where user_group_connection.id_group in " +
            "(select user_group.id from user_group where user_group.id_administrator = :id) " +
            "and user_group_connection.active=false " +
            "and user_group.active = true";
    public static final String USERS_OF_GROUP = "select * from users " +
            "inner join user_group_connection on users.id = user_group_connection.id_user " +
            "where user_group_connection.active = true " +
            "and user_group_connection.id_group=?1 order by users.id";
    public static final String GROUPS_OF_USER_ACTIVE = "select * from user_group " +
            "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
            "where user_group_connection.active = true and user_group.active = true " +
            "and user_group_connection.id_user=?1 order by user_group.id ";
    public static final String GROUPS_OF_USER_INACTIVE = "select * from user_group " +
            "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
            "where user_group_connection.active = false and user_group.active = true " +
            "and user_group_connection.id_user=?1 order by user_group.id ";
    public static final String FEED_QUERY =
            "select distinct question.id, user_group.group_name, question.question,question.date,count(upvotes_question.id) over (partition by question.id) as count from user_group "+
            "inner join user_group_connection on user_group.id = user_group_connection.id_group " +
            "inner join users on users.id = user_group_connection.id_user " +
            "inner join question on question.id_group=user_group.id " +
            "inner join upvotes_question on upvotes_question.id_question = question.id " +
            "where users.id = ?1 and user_group.active = true and " +
            "user_group_connection.active=true and question.active=true " +
            "order by question.date desc";

}
