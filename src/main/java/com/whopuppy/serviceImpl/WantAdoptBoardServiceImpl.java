package com.whopuppy.serviceImpl;

import com.whopuppy.domain.WantAdoptBoard;
import com.whopuppy.service.WantAdoptBoardService;
import com.whopuppy.util.S3Util;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WantAdoptBoardServiceImpl implements WantAdoptBoardService {

    @Resource
    S3Util s3Util;

    @Override
    public List getThumnail(String wantAdoptBoard){
        Pattern nonValidPattern = Pattern
                .compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        List result = new ArrayList();
        Matcher matcher = nonValidPattern.matcher(wantAdoptBoard);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

    @Override
    public String getImageUrl(MultipartFile multipartFile) throws Exception {
        return s3Util.uploadObject(multipartFile);
    }
}
