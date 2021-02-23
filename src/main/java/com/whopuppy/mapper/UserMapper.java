package com.whopuppy.mapper;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface UserMapper {
    void setMajor(String major,Long user_id);
    void setSalt(String salt,Long user_id);
    void signUp(User user);
    Long getUserIdFromPhoneNumber(String phoneNumber);
    String getUserByNickName(String nickname);
    String getNickNameByUserId(Long id);
    User getPasswordFromPortal(String portal_account);
    String getSalt(Long id);

    void setAuthNumber(AuthNumber authNumber);
    ArrayList<AuthNumber> getSecret(AuthNumber authNumber);
    void setIs_authed(boolean is_authed,Long id);
    ArrayList<AuthNumber> getAuthTrue(String portal_account, Integer flag);
    void deleteAllAuthNumber(AuthNumber authNumber);
    void findPassword(User user);

    User getMe(Long id);
}
