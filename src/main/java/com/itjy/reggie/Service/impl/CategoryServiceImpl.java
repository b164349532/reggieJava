package com.itjy.reggie.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjy.reggie.Service.CategoryService;
import com.itjy.reggie.Service.DishService;
import com.itjy.reggie.Service.SetmealService;
import com.itjy.reggie.common.CustomException;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;
import com.itjy.reggie.entity.Dish;
import com.itjy.reggie.entity.Setmeal;
import com.itjy.reggie.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> remove(Long id) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>();
        lqw.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(lqw);
        if(count1 > 0) {
            //已经关联菜品
            throw new CustomException("当前分类下关联了菜品, 不能删除");
        }

        LambdaQueryWrapper<Setmeal> lqw2 = new LambdaQueryWrapper<Setmeal>();
        lqw2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(lqw2);
        if(count2 > 0) {
            //已关联套餐
            throw new CustomException("当前分类下关联了套餐, 不能删除");
        }

        //正常删除
        super.removeById(id);
        return R.success("信息删除成功");

    }
}
