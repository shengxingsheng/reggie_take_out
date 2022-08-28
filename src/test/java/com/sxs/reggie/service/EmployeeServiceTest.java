package com.sxs.reggie.service;

import com.sxs.reggie.entity.Employee;
import com.sxs.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-19 16:43
 */
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeMapper employeeMapper;

    @Test
    void test_getAll(){
        List<Employee> list = employeeMapper.selectList(null);
        System.out.println(list);
    }
}
