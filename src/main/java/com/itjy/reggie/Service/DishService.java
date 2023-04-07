package com.itjy.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjy.reggie.Dto.DishDto;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public R<String> saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
