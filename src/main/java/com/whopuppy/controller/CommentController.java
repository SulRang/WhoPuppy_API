package com.whopuppy.controller;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/insertcomment")
    public String commentInsert(@RequestParam Long board_id, @RequestParam String content, @RequestParam String writer) throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBoard_id(board_id);
        commentDTO.setContent(content);
        commentDTO.setWriter(writer);
        commentService.registerComment(commentDTO);
        return "Insert Comment test " + commentDTO.getContent();
    }
    @PostMapping("/readcomment")
    public List<CommentDTO> CommentReadTest(@RequestParam Long board_id) throws Exception {
        return commentService.getCommentList(board_id);
    }
    @PostMapping("/updatecomment")
    public String CommentUpdateTest(@RequestParam Long comment_id,@RequestParam Long board_id,@RequestParam String content, @RequestParam String writer) throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment_id(comment_id);
        commentDTO.setBoard_id(board_id);
        commentDTO.setContent(content);
        commentDTO.setWriter(writer);
        return "Update Comment test : " + commentService.registerComment(commentDTO);
    }
    @PostMapping(value = "/removecomment")
    public String CommentDeleteTest(@RequestParam Long comment_id) throws Exception{
        return "Delete Comment test : " + commentService.deleteComment(comment_id);
    }

}
