package com.itjy.reggie.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjy.reggie.Service.ShoppingCartService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.ShoppingCart;
import com.itjy.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R<String> subtractionWithCart(ShoppingCart  shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        ShoppingCart one = null;
        if(dishId != null) {
            LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            one = this.getOne(lqw);
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber() - 1);

        }else {
            LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            one = this.getOne(lqw);
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber() - 1);
        }
        if(one.getNumber() - 1 == 0) {
            this.removeById(one.getId());
        }else {
            this.updateById(shoppingCart);
        }
        return null;
    }
}
