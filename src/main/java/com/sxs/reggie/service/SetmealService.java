package com.sxs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxs.reggie.dto.SetmealDto;
import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-22 18:32
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);

    void updateWithDish(SetmealDto setmealDto);
}
