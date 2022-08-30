package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxs.reggie.common.R;
import com.sxs.reggie.dto.DishDto;
import com.sxs.reggie.dto.SetmealDto;
import com.sxs.reggie.entity.*;
import com.sxs.reggie.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sxs
 * @create 2022-08-23 15:42
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @ApiOperation("套餐分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "页大小",required = true),
            @ApiImplicitParam(name = "name",value = "页码",required = false),
    })
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, wrapper);

        Page<SetmealDto> setmealPageNew = new Page<>(page,pageSize);
        BeanUtils.copyProperties(setmealPage, setmealPageNew,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> recordsNew= new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                setmealDto.setCategoryName(category.getName());
            }
            recordsNew.add(setmealDto);
        }
        setmealPageNew.setRecords(recordsNew);
        return R.success(setmealPageNew);
    }
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐新增成功");
    };

    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public R<String> save(@RequestParam List<Long> ids){
//        log.info("ids: {}", ids);
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功");
    };

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam List<Long> ids){
//        log.info("ids: {}", ids);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmealService.update(setmeal,wrapper);
        return R.success("套餐状态修改成功");
    };

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDto(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list);

        return R.success(setmealDto);
    }
    @PutMapping()
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public R<String> put(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("套餐修改成功");
    }

    /**
     * 根据categoryId获取套餐
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId+'_'+#status")
    public R<List> getList( Long categoryId,Integer status){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId!=null,Setmeal::getCategoryId, categoryId);
        wrapper.eq(status!=null,Setmeal::getStatus, status);
        List<Setmeal> setmealList = setmealService.list(wrapper);

        return R.success(setmealList);
    }

    @GetMapping("/dish/{id}")
    public R<List<DishDto>> getDishes(@PathVariable Long id){
        if (id==null){
            return R.error("没有该套餐");
        }
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        ArrayList<DishDto> dishDtos = new ArrayList<>();
        if (list != null){
            for (SetmealDish setmealDish : list) {
                Long dishId = setmealDish.getDishId();
                Dish dish = dishService.getById(dishId);
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, dish.getId());
                List<DishFlavor> flavors = dishFlavorService.list(wrapper1);
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish, dishDto);
                dishDto.setFlavors(flavors);
                dishDto.setCopies(setmealDish.getCopies());
                dishDtos.add(dishDto);
            }
            return R.success(dishDtos);
        }
        return R.error("套餐下没有菜品");
    }
}
