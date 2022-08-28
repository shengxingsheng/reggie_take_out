package com.sxs.reggie.dto;

import com.sxs.reggie.entity.OrderDetail;
import com.sxs.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-25 0:22
 */
@Data
public class OrdersDto extends Orders {
    List<OrderDetail> orderDetails;
}
