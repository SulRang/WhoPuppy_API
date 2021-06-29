package com.whopuppy.controller;

import com.whopuppy.service.AnimalService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping(value = "/animal")
public class AnimalController {

    @Resource
    AnimalService animalService;

    @PostMapping
    @ApiOperation(value = "유기견 리스트 입력")
    public ResponseEntity saveAnimalList() throws IOException, ParseException {
        return animalService.insertAnimalList();
    }

    @GetMapping
    @ApiOperation(value = "유기견 조회", notes = "유기견 조회")
    public ResponseEntity searchAnimalList(@RequestParam String address) throws Exception {
        return new ResponseEntity(animalService.searchAnimal(address), HttpStatus.OK);
    }
}
