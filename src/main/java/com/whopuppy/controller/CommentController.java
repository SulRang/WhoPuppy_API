package com.whopuppy.controller;

import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.CommentDTO;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @RequestMapping(value = "/registration" , method = RequestMethod.POST)
    public ResponseEntity commentInsert(@RequestBody @Validated(ValidationGroups.animalComment.class) CommentDTO commentDTO) throws Exception {
        commentService.registerComment(commentDTO);
        return new ResponseEntity(new BaseResponse(commentDTO.getContent(), HttpStatus.CREATED), HttpStatus.OK);
    }

    @RequestMapping(value = "/list" , method = RequestMethod.POST)
    public ResponseEntity commentRead(@RequestParam Long article_id) throws Exception {
        return new ResponseEntity(commentService.getCommentList(article_id), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}" , method = RequestMethod.POST)
    public ResponseEntity commentUpdate(@RequestBody @Validated(ValidationGroups.animalComment.class) CommentDTO commentDTO, @PathVariable Long id) throws Exception {
        return new ResponseEntity(new BaseResponse(commentService.updateComment(commentDTO, id), HttpStatus.OK), HttpStatus.OK);
    }

    @RequestMapping(value = "/deletion/{id}" , method = RequestMethod.POST)
    public ResponseEntity commentDelete(@PathVariable Long id) throws Exception{
        return new ResponseEntity(new BaseResponse(commentService.deleteComment(id), HttpStatus.OK), HttpStatus.OK);
    }

}
