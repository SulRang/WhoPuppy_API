package com.whopuppy.service;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import com.whopuppy.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface BaseCommunity {


    Board getBoard(Long id);
    List<Board> getBoards();
    void postArticle(Article article);
    ArticleImage uploadArticleImages(MultipartFile multipartFile, Long id) throws Exception;
    Long postTmpArticle(Article article);
    BaseResponse completeArticle(Article article, Long id);
    List<Article> getArticles(ArticleCriteria articleCriteria);
    BaseResponse updateArticle(Article article, Long id);
    BaseResponse deleteArticle(Long id);
}
