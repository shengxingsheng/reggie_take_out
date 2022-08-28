package com.sxs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxs.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sxs
 * @create 2022-08-24 22:35
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
