package com.itjy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itjy.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
