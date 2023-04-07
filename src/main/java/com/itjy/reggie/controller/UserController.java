package com.itjy.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itjy.reggie.Service.UserService;
import com.itjy.reggie.common.BaseContext;
import com.itjy.reggie.common.R;

import com.itjy.reggie.entity.ShoppingCart;
import com.itjy.reggie.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpServletRequest HttpServletRequest) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone, user.getPhone());
        User one = userService.getOne(lqw);
        if(one != null) {
            HttpServletRequest.getSession().setAttribute("user", one.getId());
            return R.success("登陆成功");
        }
        return R.error("用户未注册");
    }


}
