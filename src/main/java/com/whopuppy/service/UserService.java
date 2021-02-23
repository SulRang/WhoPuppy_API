package com.whopuppy.service;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;

import java.util.Map;

public interface UserService {
    Map<String,String> login(User user)throws Exception;
    Map<String,Object> refresh()throws Exception;
    void signUp(User user) throws Exception;
    String sendSms(AuthNumber authNumber) throws Exception;
    String configSms(AuthNumber authNumber) throws Exception;
    boolean checkNickname(String nickname);
    void findPassword(User user)throws  Exception;
    User getLoginUser() throws Exception;
}
