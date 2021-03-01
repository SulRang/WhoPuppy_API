package com.whopuppy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    // 루트, 관리자, 일반유저
    enum Role { ROOT, MANAGER, NORMAL }

    // 유저 관리 권한, 분양해요 권한, 분양원해요 권한, 분양 후기 권한 관리자 권한 부역 권한
    // TODO 일반 유저 권한 = NONE
    enum Authority { USER, WANT_DO_ADOPT, WANT_TAKE_ADOPT, ADOPT_REVIEW, GRANT_MANAGER , NONE }

    Role role() default Role.NORMAL;
    Authority authority() default Authority.NONE;
}
