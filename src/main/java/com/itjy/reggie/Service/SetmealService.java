package com.itjy.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjy.reggie.Dto.SetmealDto;
import com.itjy.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public String saveWithSetmealDish(SetmealDto setmealDto);
    public SetmealDto getWithById(Long id);
    public String removeWithDish(List<Long> ids);

    public String sellStatus(Integer status, List<Long> ids);
}
