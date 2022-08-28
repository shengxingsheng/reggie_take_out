package com.sxs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxs.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sxs
 * @create 2022-08-19 16:38
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
