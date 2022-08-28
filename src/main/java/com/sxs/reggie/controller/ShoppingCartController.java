package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxs.reggie.common.BaseContext;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.ShoppingCart;
import com.sxs.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sxs
 * @create 2022-08-24 12:32
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        if (one == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        } else {
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            shoppingCart=one;
        }
        return R.success(shoppingCart);
    }
    /**
     * 减一
     */
    @PostMapping("/sub")
    public R sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if (dishId!=null){
            wrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        if (one != null) {
            Integer number = one.getNumber();
            shoppingCart.setId(one.getId());
            if (number == 1) {
                shoppingCart.setNumber(number-1);
                shoppingCartService.removeById(one.getId());
                return R.success(shoppingCart);
            }else {
                shoppingCart.setNumber(number-1);
                shoppingCartService.updateById(shoppingCart);
                return R.success(shoppingCart);

            }
        }
        return R.error("未找到该记录");
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(wrapper);
        return R.success("清空成功");
    }
}
