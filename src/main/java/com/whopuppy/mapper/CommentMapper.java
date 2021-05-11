package com.whopuppy.mapper;

import com.whopuppy.domain.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    Integer insertComment(CommentDTO commentDTO);
    CommentDTO selectCommentDetail(Long comment_id);
    Integer updateComment(CommentDTO commentDTO);
    Integer deleteComment(Long comment_id);
    List<CommentDTO> selectCommentList(Long board_idx);
    Integer selectCommentTotalCount(Long board_idx);
    Integer getAuthUserList(String[] userArray);
}
