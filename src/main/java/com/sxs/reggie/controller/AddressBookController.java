package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxs.reggie.common.BaseContext;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.AddressBook;
import com.sxs.reggie.entity.OrderDetail;
import com.sxs.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sxs
 * @create 2022-08-24 14:53
 */
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;


    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }
    /**
     * 获取地址列表
     * @return
     */
    @GetMapping("/list")
    public R<List> list(){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.orderByAsc(AddressBook::getCreateTime);
        List<AddressBook> list = addressBookService.list(wrapper);
        return R.success(list);
    }

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("地址新增成功");
    }
    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public R<String> defaultAddr(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,  0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("默认地址设置成功");
    }
    /**
     * 根据id获取地址
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id){
        AddressBook byId = addressBookService.getById(id);
        if (byId!=null){
            return R.success(byId);
        }else {
            return R.error("没有找到该地址");
        }
    }
    /**
     * 更新地址
     */
    @PutMapping()
    public R<String> update(@RequestBody AddressBook addressBook){
        boolean b = addressBookService.updateById(addressBook);
        if (b){
            return R.success("修改成功");
        }else {
            return R.error("修改失败");
        }

    }
    /**
     * 删除地址
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        boolean b = addressBookService.removeById(ids);
        if (b) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }
}
