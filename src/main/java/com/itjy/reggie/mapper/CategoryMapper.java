package com.itjy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itjy.reggie.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
