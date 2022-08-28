package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.entity.ShoppingCart;
import com.sxs.reggie.entity.User;
import com.sxs.reggie.mapper.ShoppingCartMapper;
import com.sxs.reggie.mapper.UserMapper;
import com.sxs.reggie.service.ShoppingCartService;
import com.sxs.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author sxs
 * @create 2022-08-23 21:03
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
