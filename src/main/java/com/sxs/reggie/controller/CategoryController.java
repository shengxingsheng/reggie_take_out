package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.Category;
import com.sxs.reggie.entity.Dish;
import com.sxs.reggie.entity.Setmeal;
import com.sxs.reggie.service.CategoryService;
import com.sxs.reggie.service.DishService;
import com.sxs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-22 17:20
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,wrapper);
        return R.success(categoryPage);
    }

    /**
     * 新增
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("分类新增成功");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping()
    public R<String> deleteById(Long id) {
        if (id==null){
            return R.error("分类删除失败");
        }
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(wrapper);
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId ,id);
        int count1 = setmealService.count(wrapper1);
        if (count<=0&&count1<=0){
            categoryService.remove(id);
            return R.success("分类删除成功");
        }else {
            return R.error("分类下关联套餐或菜品，不能删除");
        }
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("分类修改成功");
    }

    /**
     * 菜品分类列表
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List> update(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type!=null,Category::getType, type);
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
