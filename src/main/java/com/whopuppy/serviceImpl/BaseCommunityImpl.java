package com.whopuppy.serviceImpl;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.ForbiddenException;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.CommunityMapper;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.BaseCommunity;
import com.whopuppy.service.UserService;
import com.whopuppy.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BaseCommunityImpl implements BaseCommunity {
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;
    @Autowired
    private S3Util s3Util;
    @Value("${default.article.image}")
    private String defaultArticleImage;

    @Override
    public void postArticle(Article article){

    }

    @Override
    public Long postTmpArticle(Article article){
        article.setUser_id(userService.getLoginUserId());
        return communityMapper.postTmpArticle(article);
    }

    @Override
    public Board getBoard(Long id){
        return communityMapper.getBoard(id);
    }

    @Override
    public List<Board> getBoards(){
        return communityMapper.getBoards();
    }

    @Override
    public ArticleImage uploadArticleImages(MultipartFile multipartFile, Long id) throws Exception{
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
        ArticleImage articleImage = new ArticleImage();
        articleImage.setArticle_id(id);
        articleImage.setImage_url(url);
        // 추후 s3에서 최종 게시글 업로드 되지 않은 이미지를 삭제할 수 있도록 이력을 관리한다.
        articleImage.setId(communityMapper.postArticleImage(articleImage));
        return articleImage;
    }

    @Override
    public BaseResponse completeArticle(Article article, Long id){
        article.setId(id);
        // 완성하려는 게시글이 없는 경우
        if ( communityMapper.getTargetArticle(article.getId()) == null ){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }

        //이미지가 없는 경우 default 이미지를 넣어준다.
        if ( article.getImages() == null || article.getImages().size() == 0){
            ArticleImage articleImage = new ArticleImage();
            articleImage.setImage_url(defaultArticleImage);
            articleImage.setArticle_id(article.getId());
            articleImage.setId(communityMapper.postArticleImage(articleImage));
            List<ArticleImage> list = new ArrayList<>();
            list.add(articleImage);
            article.setImages(list);
        }

        // 정상적인 경로로 이미지를 입력하였는지 검사한다. ( 해당 image url이 target 게시글의 id로 존재하지 않는 경우 )
        List<ArticleImage> list = communityMapper.getImageUrls(article.getId());
        //TODO validation images
        //사진이 없는 경우 디폴트 이미지를 넣어주기 때문에 0 이 될 수 없다
        if ( list == null || list.size() == 0){
            throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
        }
        // 해당 게시글의 사진 업로드가 제대로 되었는지 검사한다.
        // image를 upload할때의 id와 같은지?
        // image를 upload할때의 url string이 같은지?
        // 만약 최종적으로 업로드를 하지 않은 image가 있을 수 있으므로 2중 for문으로 최종 업로드 입력받은 url list 검사.
        for (int i = 0; i < article.getImages().size(); i++) {
            boolean check = false;
            for (int j = 0; j < list.size(); j++) {
                // id가 같다면
                if (article.getImages().get(i).getId() == list.get(j).getId() ) {
                    // image url또한 같은지 검사
                    if (!article.getImages().get(i).getImage_url().equals(list.get(j).getImage_url())) {
                        throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
                    }
                    check = true;
                    break;
                }
            }
            if (!check)
                throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
        }
        // 섬네일을 가장 첫 사진으로 지정한다.
        article.setThumbnail(article.getImages().get(0).getImage_url());

        // article 작성
        // 사진들 use 처리
        communityMapper.completePostArticle(article);

        return new BaseResponse("게시글이 작성되었습니다.", HttpStatus.CREATED);
    }

    @Override
    public List<Article> getArticles(ArticleCriteria articleCriteria){
        Long id = articleCriteria.getBoardId();
        if ( id == null || id < 0 || id > 3 ){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }
        articleCriteria.setBoardId(id);
        return communityMapper.getArticles(articleCriteria);
    }
    //TODO 다중 이미지 업로드 함수


    // 현재의 이미지 리스트를 물고있다
    // 이미지를 추가로 업로드하는 경우
    // 이미지 추가 api를 사용한다다
    // 이미지를 제거하는 경우?
    // 이미지를 제거하는 API를 만든다.--> 수정하다가 취소하는 경우가 문제가 생긴다.
    // 이미지를 제거하는 경우에 대한 api를 만들지말고 최종 api가 들어왔을 때, 기존 이미지들과 비교하는 절차를 밟아야할 것 같다.
    // 전원 is_posted = 0으로 바꾼뒤
    // is_Deleted = 0 인 것 중에서
    // 들어온것들을 검사한 후
    // 최종 내역들을 다시 is_posted = 1로 변경한다.

    // 최종 UPDATE 시킬 LIST가 전달된다.
    // 2중 FOR 문으로 제대로 업로드 절차를 거친 것인지 확인한다. DELETED = 0인 것으로 SELECT 해와서
    @Override
    public BaseResponse updateArticle(Article article, Long id){

        Long userId = communityMapper.getArticleAuthor(id);

        // 실제로 있는 게시글인지?
        if ( userId == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }
        // 저자가 같은 지 검사한다.
        if ( userId != userService.getLoginUserId()){
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }

        article.setId(id);
        // 현재 게시글의 이미지들을 모두 미사용 처리로 한뒤, 현재 해당 게시글에 적용되어있으며 삭제되지 않은 이미지를 모두 가져온다.
        List<ArticleImage> list = communityMapper.initImages(id);

        //최종 수정 후 이미지가 없는 경우 default 이미지를 넣어준다.
        if ( article.getImages() == null || article.getImages().size() == 0){
            ArticleImage articleImage = new ArticleImage();
            articleImage.setImage_url(defaultArticleImage);
            articleImage.setArticle_id(article.getId());
            articleImage.setId(communityMapper.postArticleImage(articleImage));
            List<ArticleImage> tmp = new ArrayList<>();
            tmp.add(articleImage);
            article.setImages(tmp);
        }
        // 최종 수정 후 이미지가 있는 경우 
        else {
            //사진이 없는 경우 최소 디폴트 이미지를 넣어주기 때문에 0 이 될 수 없다
            if (list == null || list.size() == 0) {
                throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
            }

            // reqeust로 들어온 image들이 모두 db에 삭제되지 않은 채 존재하고 있는 것인지 검사한다.
            // 즉 해당 url이 정상적으로 유효한 지 검사
            for (int i = 0; i < article.getImages().size(); i++) {
                boolean check = false;
                for (int j = 0; j < list.size(); j++) {
                    // id가 같다면
                    if (article.getImages().get(i).getId() == list.get(j).getId() ) {
                        // image url또한 같은지 검사
                        if (!article.getImages().get(i).getImage_url().equals(list.get(j).getImage_url())) {
                            throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
                        }

                        check = true;
                        break;
                    }
                }
                if (!check)
                    throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
            }
        }
        // 섬네일을 최종 가장 첫 사진으로 변경한다.
        article.setThumbnail(article.getImages().get(0).getImage_url());

        // article 작성
        // 사진들 use 처리
        communityMapper.completePostArticle(article);

        return new BaseResponse("수정이 완료되었습니다", HttpStatus.OK);
    }

    @Override
    public BaseResponse deleteArticle(Long id){
        // 저자가 같은 지 검사한다.
        if ( communityMapper.getArticleAuthor(id) != userService.getLoginUserId()){
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }
        // 해당 article과 해당 article에게 속해있는 모든 image soft delete
        communityMapper.softDeleteArticle(id);
        return new BaseResponse("삭제가 완료되었습니다." ,HttpStatus.OK);
    }
}
