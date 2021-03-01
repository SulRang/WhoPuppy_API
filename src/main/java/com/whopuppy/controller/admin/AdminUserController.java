package com.whopuppy.controller.admin;

import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequestMapping(value = "/admin/user")
@RestController
public class AdminUserController {


    @Autowired
    private AdminUserService adminUserService;

    //유저에게 권한을 부여한다
    // 매니저 (혹은 루트)이며 GRANT_MANAGER 권한을 가져야만 실행이 가능하다.
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.GRANT_MANAGER)
    @PostMapping("/authority")
    public ResponseEntity grantWantTakeAdopt(@RequestBody  @Validated(ValidationGroups.authority.class) AuthNumber authNumber){
        return new ResponseEntity( new BaseResponse(adminUserService.grantAuthority(authNumber), HttpStatus.OK), HttpStatus.OK);
    }

    //유저의 권한을 제거한다
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.GRANT_MANAGER)
    @DeleteMapping("/authority")
    public ResponseEntity deleteAuthority(@RequestBody @Validated(ValidationGroups.authority.class) AuthNumber authNumber){
        return new ResponseEntity( new BaseResponse(adminUserService.deleteAuthority(authNumber), HttpStatus.OK), HttpStatus.OK);
    }


    //루트용 api는 딱히 authority를 작성하지 않아도 좋다.
    @Auth(role = Auth.Role.ROOT)
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
