package com.sxs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxs.reggie.common.CustomException;
import com.sxs.reggie.dto.SetmealDto;
import com.sxs.reggie.entity.Setmeal;
import com.sxs.reggie.entity.SetmealDish;
import com.sxs.reggie.mapper.SetmealMapper;
import com.sxs.reggie.service.SetmealDishService;
import com.sxs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author sxs
 * @create 2022-08-22 18:36
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 新增
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : dishList) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(dishList);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {

        //查询套餐状态
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(Setmeal::getId, ids);
        wrapper1.eq(Setmeal::getStatus, 1);
        int count = this.count(wrapper1);
        //如果不可删除 抛出一个异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除
        List<Setmeal> setmeals = this.listByIds(ids);
        this.removeByIds(ids);
        //删除setmealDish
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(wrapper);

        //删除图片
        for (Setmeal setmeal : setmeals) {
            File file = new File(basePath + setmeal.getImage());
            if (file.exists()){
                file.delete();
            }
        }
    }

    //更新套餐
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //更新setmeal表
        this.updateById(setmealDto);
        //删除关联的setmealDish
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(wrapper);
        //更新setmealDish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach((setmealDish)->{
            setmealDish.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes);
    }
}
