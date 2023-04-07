package com.itjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itjy.reggie.Service.EmployeeService;
import com.itjy.reggie.common.R;
import com.itjy.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
//    private EmployeeMapper employeeMapper;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);
        System.out.println(emp);
        if(emp == null) {
            return R.error("登录失败");
        }
//
        if(!emp.getPassword().equals(password)) {
            return R.error("密码不正确");
        }
//
        if(emp.getStatus() == 0) {
            return R.error("账号已冻结");
        }
//
        //登陆成功 放session
        httpServletRequest.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest,@RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(lqw);

        if(one == null) {
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
//            System.out.println(empId);
//            employee.setCreateTime(LocalDateTime.now());
//            employee.setUpdateTime(LocalDateTime.now());
//            employee.setCreateUser(empId);
//            employee.setUpdateUser(empId);
            employeeService.save(employee);

        }else{
            return R.error("  账号已存在");
        }
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name) {
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //添加过滤条件
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        if(name != null) {
            lqw.like(Employee::getName, name);
        }
        lqw.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,lqw);

        return R.success(pageInfo);
    }
    //禁用
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(employee.getId());
        employeeService.updateById(employee);
        return R.success("success");
    }


    //修改编辑
    @GetMapping("/{id}")
    public R<Employee> upDate(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);

        return R.success(employee);
    }

    //删除功能
    @DeleteMapping
    public R<String> delete(Long id) {
        employeeService.removeById(id);
       return R.success("删除员工成功");
    }

}
