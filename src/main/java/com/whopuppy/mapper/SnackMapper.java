package com.whopuppy.mapper;

import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SnackMapper {

    Long postTmpArticle(SnackArticle snackArticle);
    Long uploadArticleImages(SnackImage snackImage);
    Long getTargetArticle(Long id);
    List<SnackImage> getImageList(Long id);
    void postArticle(SnackArticle snackArticle, List<SnackImage> result);
    List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria);
}
