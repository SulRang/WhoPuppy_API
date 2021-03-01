package com.whopuppy.service.admin;

import com.whopuppy.domain.user.AuthNumber;

public interface AdminUserService {
    String grantAuthority(AuthNumber authNumber);
    String deleteAuthority(AuthNumber authNumber);
}
