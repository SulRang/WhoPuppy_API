package com.whopuppy.serviceImpl;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.mapper.CommentMapper;
import com.whopuppy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public CommentDTO createComment(Long board_id, String content, String writer)throws Exception{
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBoard_id(board_id);
        commentDTO.setContent(content);
        commentDTO.setWriter(writer);
        return commentDTO;
    }

    @Override
    public boolean registerComment(CommentDTO params) throws Exception{
        int queryResult = 0;

        if (params.getComment_id() == null) {
            queryResult = commentMapper.insertComment(params);
        } else {
            queryResult = commentMapper.updateComment(params);
        }

        return (queryResult == 1) ? true : false;
    }

    @Override
    public boolean deleteComment(Long comment_id) throws Exception{
        int queryResult = 0;

        CommentDTO commentDTO = commentMapper.selectCommentDetail(comment_id);

        if (commentDTO != null && "N".equals(commentDTO.getDeleteYn())) {
            queryResult = commentMapper.deleteComment(comment_id);
        }
        else
            throw new Exception();


        return (queryResult == 1) ? true : false;
    }

    @Override
    public List<CommentDTO> getCommentList(Long board_idx) throws Exception{
        List<CommentDTO> commentList = Collections.emptyList();

        int commentTotalCount = commentMapper.selectCommentTotalCount(board_idx);
        if (commentTotalCount > 0) {
            commentList = commentMapper.selectCommentList(board_idx);
        }
        else
            throw new Exception();

        return commentList;
    }
}
