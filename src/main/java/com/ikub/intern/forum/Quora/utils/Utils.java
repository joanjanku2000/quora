package com.ikub.intern.forum.Quora.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static <T> List<T> setToList(Set<T> e) {
        List<T> list = new ArrayList<>();
        e.forEach((setElement) -> {
            list.add(setElement);
        });
        return list;
    }

    public static <T> Set<T> listToSet(List<T> obj) {
        Set<T> set = new HashSet<>();
        obj.forEach((setElement) -> {
            set.add(setElement);
        });
        return set;
    }

}
