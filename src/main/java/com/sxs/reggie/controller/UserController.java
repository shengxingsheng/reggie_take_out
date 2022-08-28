package com.sxs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxs.reggie.common.R;
import com.sxs.reggie.entity.User;
import com.sxs.reggie.service.UserService;
import com.sxs.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sxs
 * @create 2022-08-23 21:04
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody Map map, HttpSession session){
        String phone =(String) map.get("phone");
        String code = (String) map.get("code");
//        String codeInSession = (String) session.getAttribute(phone);
        //从redis获取
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);
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
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("验证码错误");
    }
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
//        session.setAttribute(user.getPhone(),code); minutes
        log.info("验证码：{}", code);
        redisTemplate.opsForValue().set(user.getPhone(),code,5, TimeUnit.MINUTES);

        return R.success("验证发送成功");
    }
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
