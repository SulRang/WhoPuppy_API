package com.whopuppy.mapper;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import org.apache.ibatis.annotations.Mapper;


import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface UserMapper {
    void setSalt(String salt,Long user_id);
    void signUp(User user);
    Long getUserIdFromPhoneNumber(String phone_number);
    String getUserByNickName(String nickname);
    String getSalt(Long id);
    void setAuthNumber(AuthNumber authNumber);
    List<AuthNumber> getSecret(AuthNumber authNumber);
    void setIs_authed(boolean is_authed,Long id);
    List<AuthNumber> getAuthTrue(String account, Integer flag, String phone_number);
    void deleteAllAuthNumber(AuthNumber authNumber);
    void passwordUpdate(User user);
    void expirePastAuthNumber(AuthNumber authNumber);
    User getUserIdFromAccount(String account);
    List<String> getAllAuthHistory(AuthNumber authNumber);
    void authNumberSoftDelete(Long id);
    User getMe(Long id);
    Integer authNumberAllSoftDeleteAfterUse(String phoneNumber, String ip, Timestamp start, Timestamp end);
    void setProfile(Long id, String url);
}
