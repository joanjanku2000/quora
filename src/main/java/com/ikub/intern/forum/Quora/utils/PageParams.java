package com.ikub.intern.forum.Quora.utils;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortField = "name";
}