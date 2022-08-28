package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.common.CustomException;
import com.sxs.reggie.entity.Category;
import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.Setmeal;
import com.sxs.reggie.mapper.CategoryMapper;
import com.sxs.reggie.service.CategoryService;
import com.sxs.reggie.service.DishService;
import com.sxs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-22 17:25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;


    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(wrapper1);
        if (count1>0){
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(wrapper2);
        if (count2 >0){
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
