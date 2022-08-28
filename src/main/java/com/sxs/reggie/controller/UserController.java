package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.User;
import com.sxs.reggie.service.UserService;
import com.sxs.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author sxs
 * @create 2022-08-23 21:04
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpSession session){
        String phone =(String) map.get("phone");
        String code = (String) map.get("code");
        String codeInSession = (String) session.getAttribute(phone);
        if (codeInSession!=null&&code!=null&&code.equals(codeInSession)){
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user==null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success("登录成功");
        }
        return R.error("验证码错误");
    }
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        session.setAttribute(user.getPhone(),code);
        log.info("验证码：{}", code);
        return R.success("验证发送成功");
    }
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
