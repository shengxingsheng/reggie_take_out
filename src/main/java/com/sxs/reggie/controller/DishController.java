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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    RedisTemplate redisTemplate;
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
    public R<List<DishDto>> getList(Dish dish){
        List<DishDto> dishDtos = null;
        //动态构造
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //从redis中获取菜品数据
        dishDtos = (List<DishDto>)redisTemplate.opsForValue().get(key);
        //有，直接返回
        if (dishDtos!=null){
            return R.success(dishDtos);
        }
        //没有，在mysql中查，然后放在redis中
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(Dish::getStatus, 1);
        List<Dish> dishList = dishService.list(wrapper);
        dishDtos = new ArrayList<>();
        for (Dish d : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(d, dishDto);
            dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(DishFlavor::getDishId, d.getId());
            List<DishFlavor> list = dishFlavorService.list(wrapper1);
            dishDto.setFlavors(list);
            dishDtos.add(dishDto);
        }
        redisTemplate.opsForValue().set(key, dishDtos,60, TimeUnit.MINUTES);
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
        //精确清理某个分类菜
        redisTemplate.delete("dish_"+dishDto.getCategoryId()+"_1");
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
        //清理所有菜品缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
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
        //清理所有菜品缓存
//        redisTemplate.delete(redisTemplate.keys("dish_*"));
        //精确清理某个分类菜
        redisTemplate.delete("dish_"+dishDto.getCategoryId()+"_1");

        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> delete( Long[] ids){
        dishService.deleteWithFlavor(ids);
        //清理所有菜品缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
        return R.success("菜品删除成功");
    }
}
