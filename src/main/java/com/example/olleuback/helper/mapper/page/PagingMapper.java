package com.example.olleuback.helper.mapper.page;

import com.example.olleuback.utils.dto.page.Paging;

public class PagingMapper {
    public static Paging makePaging(int pageNumber, int pageSize, int totalPage, long totalElements) {
        return Paging.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .totalElements(totalElements)
                .build();
    }
}
