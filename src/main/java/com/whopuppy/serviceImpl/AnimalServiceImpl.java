package com.whopuppy.serviceImpl;

import com.whopuppy.mapper.AnimalMapper;
import com.whopuppy.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private AnimalMapper animalMapper;

    @Override
    public void insertAnimalJson(List animalDTOList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("animalList", animalDTOList);
        animalMapper.insertAnimal(map);
    }
}
