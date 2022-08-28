package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.entity.Employee;
import com.sxs.reggie.mapper.EmployeeMapper;
import com.sxs.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author sxs
 * @create 2022-08-19 16:38
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
