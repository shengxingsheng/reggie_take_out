package com.sxs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxs.reggie.dto.DishDto;
import com.sxs.reggie.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sxs
 * @create 2022-08-22 18:32
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品 需要新增两张表
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    DishDto getWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(Long[] ids);
}
