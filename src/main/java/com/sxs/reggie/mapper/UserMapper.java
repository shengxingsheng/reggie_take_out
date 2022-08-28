package com.sxs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxs.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sxs
 * @create 2022-08-23 21:01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
