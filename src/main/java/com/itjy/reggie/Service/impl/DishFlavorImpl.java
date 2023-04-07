package com.itjy.reggie.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjy.reggie.Service.DishFlavorService;
import com.itjy.reggie.entity.DishFlavor;
import com.itjy.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
