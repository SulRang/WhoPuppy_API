package com.whopuppy.mapper;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface CommunityMapper {

    List<Board> getBoards();
    Board getBoard(Long id);
    Long postTmpArticle(Article article);
    Long postArticleImage(ArticleImage articleImage);
    Long getTargetArticle(Long id);
    void completePostArticle(Article article);
    List<ArticleImage> getImageUrls(Long id);
    List<Article> getArticles(ArticleCriteria articleCriteria);
    Long getArticleAuthor(Long id);
    void softDeleteArticle(Long id);
}
