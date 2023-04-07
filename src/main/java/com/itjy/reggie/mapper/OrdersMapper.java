package com.itjy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;
@Mapper
public interface OrdersMapper extends BaseMapper<Order> {
}
