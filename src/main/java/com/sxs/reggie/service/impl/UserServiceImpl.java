package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.entity.User;
import com.sxs.reggie.mapper.UserMapper;
import com.sxs.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author sxs
 * @create 2022-08-23 21:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
