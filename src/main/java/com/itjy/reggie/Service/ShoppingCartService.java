package com.itjy.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    public R<String> subtractionWithCart(ShoppingCart  shoppingCart);
}
