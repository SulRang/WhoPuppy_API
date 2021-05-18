package com.whopuppy.controller;

import com.whopuppy.service.AnimalService;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class AnimalController {

    @Resource
    AnimalService animalService;

    @PostMapping("/saveAnimalList")
    public ResponseEntity saveAnimalList() throws IOException, ParseException {
        return animalService.insertAnimalList();
    }
}
