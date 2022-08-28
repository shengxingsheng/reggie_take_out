package com.sxs.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxs.reggie.entity.Category;

/**
 * @author sxs
 * @create 2022-08-22 17:23
 */
public interface CategoryService extends IService<Category> {


    void remove(Long id) ;
}
