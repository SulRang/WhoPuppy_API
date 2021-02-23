package com.whopuppy.controller;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.WantAdoptBoard;
import com.whopuppy.service.WantAdoptBoardService;
import com.whopuppy.util.S3Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RequestMapping("/adopt")
@RestController
public class WantAdobtBoardController {


    @Resource
    WantAdoptBoardService wantAdoptBoardService;

    @PostMapping(value = "/image")
    public ResponseEntity getImageUrl(@RequestBody MultipartFile multipartFile) throws Exception{
        return new ResponseEntity(wantAdoptBoardService.getImageUrl(multipartFile), HttpStatus.CREATED); // 201 created status
    }
    @Xss
    @PostMapping(value="/board")
    public ResponseEntity postBoard(@RequestBody String wantAdoptBoard){
        return new ResponseEntity(wantAdoptBoardService.getThumnail(wantAdoptBoard),HttpStatus.OK);
    }

    @Xss
    @PostMapping(value="/board2")
    public ResponseEntity postBoard2(@RequestBody String wantAdoptBoard){
        return new ResponseEntity(wantAdoptBoard ,HttpStatus.OK);
    }
}
