package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.common.CustomException;
import com.sxs.reggie.dto.DishDto;
import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.DishFlavor;
import com.sxs.reggie.mapper.DishMapper;
import com.sxs.reggie.service.DishFlavorService;
import com.sxs.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sxs
 * @create 2022-08-22 18:32
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Value("${reggie.path}")
    private String basePath;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getWithFlavor(Long id) {
        Dish dish = this.getById(id);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(wrapper);
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品信息
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithFlavor(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        //查看菜品是否售卖中
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(Dish::getId, list);
        wrapper1.eq(Dish::getStatus,1);
        int count = this.count(wrapper1);
        if (count>0){
            throw new CustomException("菜品正在售卖中，不能删除！");
        }

        //删除口味
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(wrapper);
        //删除菜品
        List<Dish> dishes = this.listByIds(list);
        this.removeByIds(list);

        //删除图片
        dishes.forEach((dish)->{
            File file = new File(basePath + dish.getImage());
            if (file.exists()){
                file.delete();
            }
        });
    }


}
