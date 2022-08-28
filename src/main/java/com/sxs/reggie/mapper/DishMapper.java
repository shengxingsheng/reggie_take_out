package com.sxs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxs.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sxs
 * @create 2022-08-22 18:31
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
