package com.whopuppy.service;

import com.whopuppy.domain.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(Long board_id, String content, String writer)throws Exception;

    boolean registerComment(CommentDTO params)throws Exception;

    boolean deleteComment(Long comment_id)throws Exception;

    List<CommentDTO> getCommentList(Long params)throws Exception;

}