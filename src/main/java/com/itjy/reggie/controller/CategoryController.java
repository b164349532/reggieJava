package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itjy.reggie.Service.CategoryService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;

import com.itjy.reggie.entity.Setmeal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category: {}",category);
        categoryService.save(category);

        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();

        lqw.like(name != null, Category::getName, name);
        lqw.orderByDesc(Category::getSort);

        categoryService.page(pageInfo,lqw);

        return R.success(pageInfo);
    }

    /**
     *
     * @param id
     * @return
     */

    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.remove(id);

        return R.success("删除成功");

    }

    /**
     *
     * @param category
     * @return
     */

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);

        return R.success("修改成功");

    }

    /**
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getList(String type) {
        log.info(type);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(type != null, Category::getType,type);
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }






}
