package com.whopuppy.controller;

import com.sun.org.apache.xpath.internal.operations.Mult;
import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleComment;
import com.whopuppy.domain.criteria.ArticleCriteria;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.BaseCommunity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping(value = "/community")
public class CommunityController {

    @Autowired
    private BaseCommunity baseCommunity;

    @GetMapping("/board/{id}")
    public ResponseEntity getBoard(@PathVariable Long id) {
        return new ResponseEntity(baseCommunity.getBoard(id), HttpStatus.OK);
    }

    @GetMapping("/board")
    public ResponseEntity getBoards() {
        return new ResponseEntity(baseCommunity.getBoards(), HttpStatus.OK);
    }

    @Xss
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @PostMapping("/article")
    @ApiOperation(value = "게시글 작성 시작", notes = "게시글 임시 작성시작, 작성 완료해야할 게시글의 id의 값을 return함", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity postTmpArticle(@RequestBody Article article) {
        return new ResponseEntity(baseCommunity.postTmpArticle(article), HttpStatus.OK);
    }

    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @PostMapping("/article/image/{id}")
    @ApiOperation(value = "게시글에 넣을 image url 반환", notes = "게시글에 넣을 image url 반환", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity uploadArticleImages(@RequestBody MultipartFile multipartFile, @PathVariable Long id) throws Exception {
        return new ResponseEntity(baseCommunity.uploadArticleImages(multipartFile, id), HttpStatus.OK);
    }

    @Xss
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @PostMapping("/article/{id}")
    @ApiOperation(value = "게시글 작성", notes = "게시글 작성", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity completeArticle(@RequestBody @Validated(ValidationGroups.postCommunity.class) Article article, @PathVariable Long id) throws Exception {
        return new ResponseEntity(baseCommunity.completeArticle(article, id), HttpStatus.CREATED);
    }

    @GetMapping("/article")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "게시글 조회", notes = "Board_id와 지역에 따른 게시글 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity getArticles(@ModelAttribute ArticleCriteria articleCriteria) {
        return new ResponseEntity(baseCommunity.getArticles( articleCriteria), HttpStatus.OK);
    }

    @Xss
    @PutMapping("/article/{id}")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "게시글 수정", notes = "게시글 수정", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity updateArticle(@RequestBody @Validated(ValidationGroups.postCommunity.class) Article article, @PathVariable Long id) {
        return new ResponseEntity(baseCommunity.updateArticle(article, id) , HttpStatus.OK);
    }

    @DeleteMapping("/article/{id}")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity deleteArticle(@PathVariable Long id) {
        return new ResponseEntity(baseCommunity.deleteArticle(id), HttpStatus.OK);
    }

    @Xss
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @PostMapping("/article/{id}/comment")
    @ApiOperation(value = "게시글에 댓글 작성", notes = "게시글에 댓글 작성", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity postComment(@RequestBody @Validated(ValidationGroups.postComment.class) ArticleComment articleComment, @PathVariable Long id) throws Exception {
        return new ResponseEntity(baseCommunity.postComment(articleComment,id), HttpStatus.CREATED);
    }

    @DeleteMapping("/article/{id}/comment/{commentId}")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "게시글에 댓글 삭제", notes = "게시글에 댓글 삭제", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity deleteComment(@PathVariable Long commentId, @PathVariable Long id) {
        return new ResponseEntity(baseCommunity.deleteComment(id ,commentId ), HttpStatus.OK);
    }


    @GetMapping("/article/{id}/comment")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "게시글에 댓글 조회", notes = "게시글에 댓글 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity deleteComment( @PathVariable Long id) {
        return new ResponseEntity(baseCommunity.getComment(id ), HttpStatus.OK);
    }

}
