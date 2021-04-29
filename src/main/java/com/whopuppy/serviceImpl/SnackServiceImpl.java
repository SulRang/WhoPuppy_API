package com.whopuppy.serviceImpl;

import com.amazonaws.services.xray.model.Http;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.SnackMapper;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.SnackService;
import com.whopuppy.service.UserService;
import com.whopuppy.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
public class SnackServiceImpl implements SnackService {
    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    private SnackMapper snackMapper;
    @Autowired
    private S3Util s3Util;
    @Value("${default.article.image}")
    private String defaultArticleImage;
    
    //게시글 작성을 시작한다
    @Override
    public Long postTmpArticle(){
        SnackArticle snackArticle = new SnackArticle();
        snackArticle.setUser_id(userService.getLoginUserId());
        return snackMapper.postTmpArticle(snackArticle);
    }

    //게시글에 업로드하기 위한 이미지의 링크를 반환한다
    @Override
    public SnackImage uploadArticleImages(MultipartFile multipartFile, Long id) throws Exception{
        // id 가 null 인 경우
        if ( id == null )
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        // multipartfile이 null인 경우
        if ( multipartFile == null){
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NULL);
        }
        //multipartfile의 content type이 jpeg, png가 아닌경우
        if( !multipartFile.getContentType().equals("image/jpeg") && !multipartFile.getContentType().equals("image/png"))
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NOT_IMAGE);

        String url = s3Util.uploadObject(multipartFile);
        SnackImage snackImage = new SnackImage();
        snackImage.setSnack_id(id);
        snackImage.setImage_url(url);
        // 추후 s3에서 최종 게시글 업로드 되지 않은 이미지를 삭제할 수 있도록 이력을 관리한다.
        snackImage.setId(snackMapper.uploadArticleImages(snackImage));
        return snackImage;
    }

    //게시글을 완성한다
    @Override
    public BaseResponse postArticle(SnackArticle snackArticle, Long id){
        snackArticle.setId(id);
        // 완성하려는 게시글이 없는 경우
        if (snackMapper.getTargetArticle(snackArticle.getId()) == null) {
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }

        //본문의 삽입된 이미지 추출
        List<String> imgList = this.getImagesFromContent(snackArticle.getContent());

        //이미지가 옴따면 ?
        if (imgList == null || imgList.size() == 0) {
            //default 이미지를 만들어 db에 이력 저장
            SnackImage snackImage = new SnackImage();
            snackImage.setSnack_id(id);
            snackImage.setImage_url(defaultArticleImage);
            snackImage.setId(snackMapper.uploadArticleImages(snackImage));
            // 만든 이미지를 썸네일로 사용
            snackArticle.setThumbnail(snackImage.getImage_url());
            imgList.add(snackImage.getImage_url());
        }

        //정상적으로 등록된 이미지만 사용하였는!가!?
        List<SnackImage> dbImage = snackMapper.getImageList(snackArticle.getId());
        List<SnackImage> result = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            boolean check = false;
            for (int j = 0; j < dbImage.size(); j++) {
                if (imgList.get(i).equals(dbImage.get(j).getImage_url())) {
                    result.add(dbImage.get(j));
                    check = true;
                    break;
                }
            }
            // 만약 등록된 이미지가 아닌 이미지가 들어왔따면?!!@?!??!?
            if (!check) {
                throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
            }
        }



        // 섬네일 등록
        snackArticle.setThumbnail(imgList.get(0));

        snackMapper.postArticle(snackArticle, result);
        return new BaseResponse("게시글이 작성되었습니다", HttpStatus.CREATED);
    }

    // 게시글의 img 태그의 src="" 내용을 추출한다
    public List<String> getImagesFromContent(String value){
        Pattern nonValidPattern = Pattern
                .compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        List<String> result = new ArrayList();
        Matcher matcher = nonValidPattern.matcher(value);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    @Override
    public List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria){
        return snackMapper.getAllSnackArticles(baseCriteria);
    }
}
