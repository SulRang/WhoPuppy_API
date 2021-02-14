package com.whopuppy.domain;

import org.springframework.stereotype.Component;

@Component
public class WantAdoptBoard {
    private String thumanail;

    public String getThumanail() {
        return thumanail;
    }

    public void setThumanail(String thumanail) {
        this.thumanail = thumanail;
    }
}
