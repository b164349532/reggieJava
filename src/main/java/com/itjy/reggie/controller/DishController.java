package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itjy.reggie.Dto.DishDto;
import com.itjy.reggie.Service.CategoryService;
import com.itjy.reggie.Service.DishFlavorService;
import com.itjy.reggie.Service.DishService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;
import com.itjy.reggie.entity.Dish;
import com.itjy.reggie.entity.DishFlavor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate  redisTemplate;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
            Page<Dish> pageInfo = new Page<>(page,pageSize);

        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);


        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
//
        lqw.like(name != null,Dish::getName, name);

        lqw.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, lqw);
        BeanUtils.copyProperties(pageInfo,dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        String keys = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(keys);
        return R.success("成功添加");
    }

    /**
     *
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<DishDto> getList(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
//        Set keys = redisTemplate.keys("dish_*");
        String keys = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(keys);
        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish) {
        List<DishDto> listDto = new ArrayList<>();

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus(); //dish_397844303408574465_1
        listDto = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(listDto != null) {
             return R.success(listDto);
         }

         //redis不存在
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null,Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);
        List<Dish> list = dishService.list(lqw);

        listDto = list.stream().map((item) -> {
           DishDto dishDto = new DishDto();
           BeanUtils.copyProperties(item,dishDto);
           LambdaQueryWrapper<DishFlavor> lqw2 = new LambdaQueryWrapper<>();
           lqw2.eq(DishFlavor::getDishId , item.getId());
           dishDto.setFlavors(dishFlavorService.list(lqw2));
           return dishDto;
        }).collect(Collectors.toList());


       redisTemplate.opsForValue().set(key, listDto, 60, TimeUnit.MINUTES);
        return R.success(listDto);
    }



}
