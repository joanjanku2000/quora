package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.utils.PageParams;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionRepoCustom {
    List<QuestionEntity> findAllByGroup(Long id);


}
