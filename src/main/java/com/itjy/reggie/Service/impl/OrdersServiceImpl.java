package com.itjy.reggie.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjy.reggie.Service.OrdersService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Orders;
import com.itjy.reggie.mapper.OrdersMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Order> implements OrdersService {
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @Transactional
    public R<String> submit(Orders orders) {

        return null;
    }
}
