package com.whopuppy.controller;


import com.amazonaws.services.xray.model.Http;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;


    @Xss
    @PostMapping(value = "/sms")
    public ResponseEntity sendSms( @Validated(ValidationGroups.sendSms.class) @RequestBody AuthNumber authNumber) throws Exception{
        return new ResponseEntity(new BaseResponse( userService.sendSms(authNumber), HttpStatus.OK), HttpStatus.OK);
    }

    @Xss
    @PostMapping(value = "/sms/config")
    public ResponseEntity configSms(@Validated(ValidationGroups.configSms.class)  @RequestBody AuthNumber authNumber) throws Exception{
        return new ResponseEntity(new BaseResponse( userService.configSms(authNumber), HttpStatus.OK), HttpStatus.OK);
    }

    @Xss
    @PostMapping(value = "/sign-up")
    public ResponseEntity signUp(@RequestBody @Validated(ValidationGroups.signUp.class) User user) throws Exception{
        userService.signUp(user);
        return new ResponseEntity( new BaseResponse("회원가입에 성공했습니다.", HttpStatus.OK),HttpStatus.OK);
    }
}
