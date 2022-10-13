package com.bh.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bh.reggie.common.Result;
import com.bh.reggie.entity.Employee;
import com.bh.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpSession session, @RequestBody Employee employee) {
//        将页面提交的代码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//      根据用户提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//       条件构造器
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
//        查询出一个雇员
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp==null){
            return Result.error("登录失败");
        }if (!emp.getPassword().equals(password)){
            return Result.error("登录失败");
        }if (emp.getStatus()==0){
            return Result.error("账号已禁用");
        }
        session.setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

}
