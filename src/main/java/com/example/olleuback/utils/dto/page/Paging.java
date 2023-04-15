package com.example.olleuback.utils.dto.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Paging {
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private Integer totalPage;
}
