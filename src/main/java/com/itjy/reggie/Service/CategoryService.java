package com.itjy.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public R<String> remove(Long id);
}
