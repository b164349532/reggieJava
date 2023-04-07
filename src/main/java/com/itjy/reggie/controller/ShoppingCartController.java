package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itjy.reggie.Service.ShoppingCartService;
import com.itjy.reggie.common.BaseContext;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long dishId = shoppingCart.getDishId();
        ShoppingCart one = null;
        if(dishId != null) {
            LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            one = shoppingCartService.getOne(lqw);
        }else {
            LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            one = shoppingCartService.getOne(lqw);
        }


        if(one != null) {
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart);
        }else {
            shoppingCartService.save(shoppingCart);
        }

        return R.success(shoppingCart);
    }

    /**
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.in(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = shoppingCartService.list(lqw);

        return R.success(list);
    }

    @PostMapping("/sub")
    public R<String>  subtraction(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.subtractionWithCart(shoppingCart);
        return R.success("-1");
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lqw);
        return R.success("清空成功");
    }
}
