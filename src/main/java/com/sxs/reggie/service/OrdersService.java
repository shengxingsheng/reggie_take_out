package com.sxs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxs.reggie.entity.Orders;

/**
 * @author sxs
 * @create 2022-08-24 22:36
 */

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
