package com.sxs.reggie;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxs.reggie.entity.Employee;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author sxs
 * @create 2022-08-19 22:33
 */
public class ReggieTest {

    @Test
    void test_jackson() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Employee employee = new Employee();
        employee.setName("123123");
        employee.setId(1L);
        System.out.println( mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee));
        System.out.println(JSON.toJSONString(employee));
    }

    @Test
    void test_ID()  {

        System.out.println(IdWorker.getId());
        System.out.println(IdWorker.getIdStr());
        System.out.println(UUID.randomUUID());
    }
}
