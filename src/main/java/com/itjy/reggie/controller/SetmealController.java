package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itjy.reggie.Dto.SetmealDto;
import com.itjy.reggie.Service.CategoryService;
import com.itjy.reggie.Service.SetmealDishService;
import com.itjy.reggie.Service.SetmealService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;
import com.itjy.reggie.entity.Dish;
import com.itjy.reggie.entity.Setmeal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
//        log.info(setmealDto.toString());
        setmealService.saveWithSetmealDish(setmealDto);
        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> newPageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null, Setmeal::getName, name);
        lqw.orderByDesc(Setmeal::getCreateTime);

        setmealService.page(pageInfo,lqw);
        BeanUtils.copyProperties(pageInfo,newPageInfo, "records");
        List<Setmeal> records = pageInfo.getRecords();
       List<SetmealDto> list = records.stream().map((item) -> {
           SetmealDto setmealDto = new SetmealDto();
           BeanUtils.copyProperties(item,setmealDto);
           Category category = categoryService.getById(item.getCategoryId());
           setmealDto.setCategoryName(category.getName());

           return setmealDto;
       }).collect(Collectors.toList());

       newPageInfo.setRecords(list);

        return R.success(newPageInfo);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getList(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getWithById(id);

        return R.success(setmealDto);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);

        return R.success("删除成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status" )
    public R<List<Setmeal>> getSetmealList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.eq (setmeal.getStatus() != null, Setmeal::getStatus,1);
        List<Setmeal> list = setmealService.list(lqw);
        return R.success(list);
    }

    @PostMapping("/status/{status}")
    public R<String> updataStatus(@PathVariable("status") int status, @RequestParam List<Long> ids) {
        setmealService.sellStatus(status,ids);
        return R.success("修改成功");
    }


}
