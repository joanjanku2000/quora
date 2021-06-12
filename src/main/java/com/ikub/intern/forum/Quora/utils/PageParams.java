package com.ikub.intern.forum.Quora.utils;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {
    private String pageNumber = String.valueOf(0);
    private String pageSize = String.valueOf(10);
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortField = "name";

    public boolean isValid(){
      try{
          int p = Integer.parseInt(pageNumber);
          int s = Integer.parseInt(pageSize);
      } catch (NumberFormatException e){
          return false;
      }
      return true;
    }
}