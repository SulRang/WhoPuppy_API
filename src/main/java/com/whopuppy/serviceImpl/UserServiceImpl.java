package com.whopuppy.serviceImpl;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.service.UserService;
import com.whopuppy.util.CoolSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CoolSmsUtil coolSmsUtil;
    @Autowired
    private UserMapper userMapper;

    public Map<String,String> login(User user)throws Exception{
        return null;
    }
    public Map<String,Object> refresh()throws Exception{
        return null;

    }
    public void signUp(User user) throws Exception{

    }

    // 문자 발송
    public String sendSms(AuthNumber authNumber) throws Exception{
        if ( authNumber.getFlag() == 0 ){ // 회원가입 발송
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhoneNumber()); // 포탈아이디로 검색
            if (id != null) { // 가입한 아이디라면 
                throw new RequestInputException(ErrorMessage.SMS_ALREADY_AUTHED); // 이미 가입했다는 에러 발생
            }
        }
        //비밀번호 찾기 요청이라면, 가입하지 않은 아이디면 throw
        if (authNumber.getFlag() == 1) {

            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhoneNumber());
            if (id == null) {
                throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
            }
        }

        // 이전 이력들은 모두 삭제 시킴 - 재발송 이전 INVALIDATE
        userMapper.deleteAllAuthNumber(authNumber);



        //get random string for secret String
        Random rnd = new Random();
        String secret = "";
        for( int i=0; i<6; i++){
            secret  += rnd.nextInt(10);// 글자의 random numbers
        }

        //db에 전송 이력을 저장
        authNumber.setSecret(secret);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 만료기한 10분
        authNumber.setExpired_at(new Timestamp( (calendar.getTime()).getTime()));
        userMapper.setAuthNumber(authNumber);

        coolSmsUtil.singleSms(authNumber.getPhoneNumber(), secret);// sms 발송

        return "sms를 발송 했습니다.";
    }
    public String configSms(AuthNumber authNumber) throws Exception{
        //회원가입 요청이라면 , 가입한 아이디이면 throw
        if (authNumber.getFlag() == 0) {

            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhoneNumber());
            if (id != null) {
                throw new RequestInputException(ErrorMessage.SMS_ALREADY_AUTHED);
            }
        }
        //비밀번호 찾기 요청이라면, 가입하지 않은 아이디면 throw
        if (authNumber.getFlag() == 1) {
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhoneNumber());
            if (id == null) {
                throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
            }
        }
        //account, flag, phoneNumber 값으로 select 해옴
        ArrayList<AuthNumber> list = userMapper.getSecret(authNumber);

        //문자를 보낸적 없다면 email인증을 신청하라고 알림
        if (list.size() == 0){
            throw new RequestInputException(ErrorMessage.SMS_NONE_AUTH_EXCEPTION);
        }

        //request secret값이 일치하는지 확인
        AuthNumber dbAuthNumber = null;
        for(int i=0;i<list.size();i++){
            if ( authNumber.getSecret().equals(list.get(i).getSecret()) ){
                dbAuthNumber = list.get(i);
                break;
            }
        }
        // secret 값이 다르다면 인증번호를 확인하라는 알림
        if ( dbAuthNumber == null){
            throw new RequestInputException(ErrorMessage.SMS_SECRET_INVALID_EXCEPTION);
        }

        //만료시간보다 현재시간이 크다면 만료되었다고 알림
        Timestamp exp = dbAuthNumber.getExpired_at();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if( exp.getTime() < now.getTime()) {
            throw new RequestInputException(ErrorMessage.SMS_EXPIRED_AUTH_EXCEPTION);
        }
        // 만료되지않았고 / secret 같고 // account 같다면 ==> is_authed = 1
        userMapper.setIs_authed(true,dbAuthNumber.getId());

        return "sms 인증에 성공했습니다.";
    }

    public boolean checkNickname(String nickname) {
        return true;
    }
    public void findPassword(User user)throws  Exception{

    }
    public User getLoginUser() throws Exception{
        return null;
    }
}
