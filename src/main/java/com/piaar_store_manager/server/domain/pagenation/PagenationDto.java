package com.piaar_store_manager.server.domain.pagenation;

import lombok.Data;

@Data
public class PagenationDto {
    private Integer itemSize;
    private Integer itemPerPage;
    private Integer pageSize;
    private Integer currPage;
    private Integer nextPage;
    private Integer prevPage;
    private Integer firstPage;
    private Integer lastPage;
    private Integer pageGroupIdx;
}
