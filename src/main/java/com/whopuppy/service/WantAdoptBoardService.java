package com.whopuppy.service;


import com.whopuppy.domain.WantAdoptBoard;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WantAdoptBoardService  {
    List getThumnail(String wantAdoptBoard);
    String getImageUrl(MultipartFile multipartFile) throws Exception;
}
