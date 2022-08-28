package com.sxs.reggie.dto;

import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object(数据传输对象)，一般用于展示层与服务层之间的数据传输。
 * @author sxs
 * @create 2022-08-22 22:45
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;
}
