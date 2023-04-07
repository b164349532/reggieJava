package com.itjy.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Orders;
import org.springframework.core.annotation.Order;


public interface OrdersService extends IService<Order> {
    public R<String> submit(Orders orders);
}
