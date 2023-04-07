package com.itjy.reggie.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjy.reggie.Dto.SetmealDto;
import com.itjy.reggie.Service.SetmealDishService;
import com.itjy.reggie.Service.SetmealService;
import com.itjy.reggie.common.CustomException;
import com.itjy.reggie.entity.Setmeal;
import com.itjy.reggie.entity.SetmealDish;
import com.itjy.reggie.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public String saveWithSetmealDish(SetmealDto setmealDto) {
        super.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
        return null;
    }

    @Override
    public SetmealDto getWithById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = super.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto, "records");
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     *
     * @param ids
     * @return
     */
    @Override
    public String removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus, 1);
        int count = super.count(lqw);

        if(count > 0) {
            throw new CustomException("套餐正在售卖中, 不能删除");
        }

        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId, ids);
        setmealDishService.removeById(lqw2);
        return null;
    }

    @Override
    public String sellStatus(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(ids != null,Setmeal::getId, ids);

        List<Setmeal> setmeals = this.list(lqw);
        for (Setmeal setmeal : setmeals) {
            setmeal.setStatus(status);
            this.updateById(setmeal);
        }

        return null;
    }
}
