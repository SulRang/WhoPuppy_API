package com.whopuppy.service;

import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import com.whopuppy.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackService {
    Long postTmpArticle();
    BaseResponse postArticle(SnackArticle snackArticle, Long id);
    SnackImage uploadArticleImages(MultipartFile multipartFile, Long id) throws Exception;
    List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria);
    List<String> getImagesFromContent(String value);
}
