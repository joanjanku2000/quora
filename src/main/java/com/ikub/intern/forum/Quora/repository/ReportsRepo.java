package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.dto.group.GroupWithMostAskedQuestion;
import com.ikub.intern.forum.Quora.dto.question.MostUpvotedQuestions;
import com.ikub.intern.forum.Quora.dto.user.MostActiveUsersInGroup;

import java.util.List;

public interface ReportsRepo {
    List<MostActiveUsersInGroup> findMostActiveUsersInGroupList(Long groupId);
    List<GroupWithMostAskedQuestion> findGroupsWithMostAskedQuestions(Long userId);
    List<MostUpvotedQuestions> findMostUpvotedQuestionsOfUser(Long userId);
}
