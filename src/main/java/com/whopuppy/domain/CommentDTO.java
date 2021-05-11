package com.whopuppy.domain;

import org.springframework.stereotype.Component;

@Component
public class CommentDTO {
    private Long comment_id;
    private Long board_id;
    private String content;
    private String writer;
    private String delete_yn;

    public Long getComment_id(){ return comment_id;}
    public Long getBoard_id(){ return board_id;}
    public String getContent(){ return content;}
    public String getWriter(){ return writer;}
    public String getDeleteYn() { return delete_yn; }

    public void setComment_id(Long comment_id){ this.comment_id = comment_id; }
    public void setBoard_id(Long board_id){ this.board_id = board_id; }
    public void setContent(String content){ this.content = content; }
    public void setWriter(String writer){ this.writer = writer; }
}
