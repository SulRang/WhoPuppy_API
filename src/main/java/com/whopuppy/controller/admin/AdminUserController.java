package com.whopuppy.controller.admin;

import com.whopuppy.annotation.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AdminUserController {

    //루트용 api는 딱히 authority를 작성하지 않아도 좋다.
    @Auth(role = Auth.Role.ROOT, authority = Auth.Authority.USER)
    @GetMapping(value = "/test")
    public String test(){
        return "admin api test";
    }

    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.USER)
    @GetMapping(value = "/test2")
    public String test2(){
        return "admin api test2";
    }

    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.WANT_DO_ADOPT)
    @GetMapping(value = "/test3")
    public String test3(){
        return "admin api test3";
    }
}
