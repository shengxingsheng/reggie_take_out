package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.entity.OrderDetail;
import com.sxs.reggie.mapper.OrderDetailMapper;
import com.sxs.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author sxs
 * @create 2022-08-24 22:55
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
