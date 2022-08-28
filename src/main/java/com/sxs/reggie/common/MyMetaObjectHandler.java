package com.sxs.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 自定义元对象处理器
 * @author sxs
 * @create 2022-08-22 16:23
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
//        log.info("自动填充[insert]..."+ metaObject.toString());

//        String[] setterNames = metaObject.getSetterNames();
//        if (Arrays.binarySearch(setterNames, "updateTime")>=0){
//            metaObject.setValue("updateTime", LocalDateTime.now());
//        }
//        if (Arrays.binarySearch(setterNames, "createUser")>=0){
//            metaObject.setValue("createUser", BaseContext.getCurrentId());//从当前线程中取值
//        }
//        if (Arrays.binarySearch(setterNames, "updateUser")>=0){
//            metaObject.setValue("updateUser", BaseContext.getCurrentId());
//        }
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());//从当前线程中取值
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("自动填充[update]..."+metaObject.toString());
/*        String[] setterNames = metaObject.getSetterNames();
        if (Arrays.binarySearch(setterNames, "updateTime")>=0){
            metaObject.setValue("updateTime",LocalDateTime.now());
        }
        if (Arrays.binarySearch(setterNames, "updateUser")>=0){
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }*/
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
