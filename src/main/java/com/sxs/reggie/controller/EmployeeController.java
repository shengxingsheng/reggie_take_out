package com.sxs.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.Employee;
import com.sxs.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author sxs
 * @create 2022-08-19 16:40
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        if (one != null) {
            String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());;
            //判断密码
            if (one.getPassword().equals(password)){
                //判断状态
                if (one.getStatus()==1){
                    request.getSession().setAttribute("employee", one.getId());
                    return R.success(one);
                }else {
                    return R.error("用户已禁用！");
                }
            }else {
                return R.error("用户名或密码错误！");
            }
        } else {
            return R.error("用户名或密码错误！");
        }
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        //删除session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     */
    @PostMapping()
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(id);
        employee.setUpdateUser(id);*/
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Employee> employeePage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null, Employee::getName,name);
        employeeService.page(employeePage, wrapper);
        return R.success(employeePage);
    }

    @PutMapping()
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
/*        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateTime(LocalDateTime.now());*/

        boolean b = employeeService.updateById(employee);
        if (b){
            return R.success("更新成功");
        }else {
            return R.error("更新失败");

        }
    }
    @GetMapping("/{id}")
    public R<Employee> update(@PathVariable Long id){

        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
