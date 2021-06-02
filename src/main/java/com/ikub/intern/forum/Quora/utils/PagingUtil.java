package com.ikub.intern.forum.Quora.utils;

import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PagingUtil {
    public static List getPagedList(List list, int pageSize, int pageNumber){

        int fromIndex = pageSize*pageNumber;
        int toIndex = pageSize+fromIndex;
        toIndex = Math.min(toIndex, list.size());
        if (toIndex<fromIndex)
            throw new BadRequestException("You have reached the end of pages");
        return list.subList(fromIndex,toIndex);
    }
    public static Page getFullPage(Pageable pageable, List filteredResult) {
        int size = filteredResult.size();
        filteredResult = getPagedList(filteredResult,pageable.getPageSize(),pageable.getPageNumber());
        return new PageImpl<>(
                filteredResult,
                pageable,
                size
        );
    }
    public static Page getFullPageWhenListIsGiven(Pageable pageable, List filteredResult) {
        int size = filteredResult.size();
        return new PageImpl<>(
                filteredResult,
                pageable,
                size
        );
    }
    public static Pageable toPage(PageParams pageParams) {
        if (pageParams.getPageSize()==null || pageParams.getPageNumber()==null){
            throw new BadRequestException("Please don't leave the fields of page size or page number empty");
        }
        if (pageParams.getPageNumber()<0)
            throw new BadRequestException("You cannot enter a negative page number.");
        if (pageParams.getPageSize()<=0)
            throw new BadRequestException("You cannot enter a negative page size.");

        Pageable pageable = PageRequest.of(pageParams.getPageNumber(),pageParams.getPageSize());
        return pageable;
    }
}
