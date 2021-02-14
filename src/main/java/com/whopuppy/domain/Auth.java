package com.whopuppy.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class Auth {
    @ApiModelProperty(hidden = true)
    private Long id;

    private String account;
    private String phoneNumber;
    private Integer flag;
    private String secret;

    @ApiModelProperty(hidden = true)
    private boolean is_authed;
    @ApiModelProperty(hidden = true)
    private Timestamp expired_at;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public boolean isIs_authed() {
        return is_authed;
    }

    public void setIs_authed(boolean is_authed) {
        this.is_authed = is_authed;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Timestamp getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(Timestamp expired_at) {
        this.expired_at = expired_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
