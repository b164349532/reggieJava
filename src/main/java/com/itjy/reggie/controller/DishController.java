package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itjy.reggie.Dto.DishDto;
import com.itjy.reggie.Service.CategoryService;
import com.itjy.reggie.Service.DishService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;
import com.itjy.reggie.entity.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

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
        return R.success("成功添加");
    }

    @GetMapping("/{id}")
    public R<DishDto> getList(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Long categoryId) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(lqw);
        List<DishDto> listDto = new ArrayList<>();
        BeanUtils.copyProperties(list,listDto);
        list.stream().map((item) -> {
            DishDto dishDto = dishService.getByIdWithFlavor(item.getId());
            listDto.add(dishDto);
            return listDto;
        }).collect(Collectors.toList());


        return R.success(listDto);
    }



}
