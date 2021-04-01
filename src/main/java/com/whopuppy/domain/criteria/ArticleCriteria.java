package com.whopuppy.domain.criteria;

import io.swagger.annotations.ApiParam;

public class ArticleCriteria extends  BaseCriteria{
    @ApiParam(required = false, defaultValue = "서울")
    private String region = null;

    @ApiParam(hidden = true , required = false)
    private Long boardId = Long.valueOf(1);

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}
