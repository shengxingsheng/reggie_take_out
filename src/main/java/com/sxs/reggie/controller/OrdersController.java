package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxs.reggie.common.BaseContext;
import com.sxs.reggie.common.R;
import com.sxs.reggie.dto.OrdersDto;
import com.sxs.reggie.entity.OrderDetail;
import com.sxs.reggie.entity.Orders;
import com.sxs.reggie.service.OrderDetailService;
import com.sxs.reggie.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sxs
 * @create 2022-08-24 22:43
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersPageNew = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        wrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage, wrapper);

        List<Orders> records = ordersPage.getRecords();
        ArrayList<OrdersDto> recordsNew = new ArrayList<>();
        for (Orders orders : records) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders, ordersDto);

            Long ordersId = orders.getId();
            LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrderDetail::getOrderId,ordersId);
            List<OrderDetail> list = orderDetailService.list(wrapper1);

            ordersDto.setOrderDetails(list);
            recordsNew.add(ordersDto);
        }
        BeanUtils.copyProperties(ordersPage, ordersPageNew,"records");
        ordersPageNew.setRecords(recordsNew);
        return R.success(ordersPageNew);
    }
}
