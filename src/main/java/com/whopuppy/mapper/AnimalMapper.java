package com.whopuppy.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import com.whopuppy.domain.AnimalDTO;

@Mapper
public interface AnimalMapper {
    Integer insertAnimal(Map<String, Object> map);
    List<AnimalDTO> searchAnimal(String address);
}
