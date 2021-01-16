package com.whopuppy.controller;

import com.whopuppy.mapper.TestMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private TestMapper testMapper;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return testMapper.test();
    }
}
