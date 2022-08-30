package com.sxs.reggie.dto;

import com.sxs.reggie.entity.Setmeal;
import com.sxs.reggie.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-23 15:54
 */
@Data

public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
