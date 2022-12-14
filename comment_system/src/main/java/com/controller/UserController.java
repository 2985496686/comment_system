package com.controller;


import com.dto.LoginFormDTO;
import com.dto.Result;
import com.dto.UserDTO;
import com.entity.User;
import com.entity.UserInfo;
import com.service.IUserInfoService;
import com.service.IUserService;
import com.utils.RegexUtils;
import com.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam("email") String email, HttpSession session) {
        //  发送短信验证码并保存验证码
        return userService.sendCode(email,session);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        //  实现登录功能
        return userService.login(loginForm,session);
    }

    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "authorization") String token){
        //实现登出功能
        //获取token

        //从redis中删除token对应的user
        return userService.logout(token);
    }

    @GetMapping("/me")
    public Result me(){
        //  获取当前登录的用户并返回
        return Result.ok(UserHolder.getUser());
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }

    @GetMapping("/{userId}")
    public Result queryUser(@PathVariable Long userId){
        return userService.queryUser(userId);
    }
}
