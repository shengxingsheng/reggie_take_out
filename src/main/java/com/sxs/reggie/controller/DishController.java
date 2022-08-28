package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxs.reggie.common.R;
import com.sxs.reggie.dto.DishDto;
import com.sxs.reggie.entity.Category;
import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.DishFlavor;
import com.sxs.reggie.service.CategoryService;
import com.sxs.reggie.service.DishFlavorService;
import com.sxs.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author sxs
 * @create 2022-08-22 19:22
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;
    /**
     * 根据categoryId获取菜品
     */
    @GetMapping("/list")
    public R<List> getList( Long categoryId){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId!=null,Dish::getCategoryId, categoryId);
        wrapper.eq(Dish::getStatus, 1);
        List<Dish> dishList = dishService.list(wrapper);
        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryService.getById(categoryId).getName());
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> list = dishFlavorService.list(wrapper1);
            dishDto.setFlavors(list);
            dishDtos.add(dishDto);
        }
        return R.success(dishDtos);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto byId = dishService.getWithFlavor(id);
        return R.success(byId);
    }
    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){

        Page<Dish> dishPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null, Dish::getName, name);
        dishService.page(dishPage,wrapper);

        //TODO 对象拷贝
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> recordsNew = new ArrayList<>();
        for (Dish dish: records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                dishDto.setCategoryName(category.getName());
            }
            recordsNew.add(dishDto);
        }
        dishDtoPage.setRecords(recordsNew);
        return R.success(dishDtoPage);
    }

    /**
     * 新增
     * @param dishDto
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }

    /**
     * 菜品的状态 批量/单个 启用或停售
     * @param status
     * @param
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> save(@PathVariable Integer status, Long[] ids){
        List<Dish> dishList = new ArrayList<>();
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(id);
            dishList.add(dish);
        }
        dishService.updateBatchById(dishList);
        return R.success("状态修改成功");
    }

    /**
     * 同时更新flavor
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> put(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> delete( Long[] ids){
        dishService.deleteWithFlavor(ids);

        return R.success("菜品删除成功");
    }
}
