package com.example.bookmarketfront.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarketfront.model.User;
import com.example.bookmarketfront.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class UserController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public String login(@RequestParam(defaultValue = "") String uid,
                        @RequestParam(defaultValue = "") String password,
                        @RequestParam(defaultValue = "") String code) {
        String captchaCode = (String) session.getAttribute("captchaCode");
        if (!captchaCode.equals(code)) {
            return "{\"code\": -1,\"userFlag\": 1}";
        }
        User user = new User();
        user.setUid(uid);
        user.setPassword(password);
        String respCode = userService.login(user);
        Integer flag = 1;
        String userJSON = "";
        if (respCode.equals("1")) {
            user = userService.searchUser(uid);
            flag = user.getFlag();
            session.setAttribute("user", user);
            userJSON = JSON.toJSONString(user);
        }
        return "{\"code\":" + respCode + ",\"userFlag\":" + flag + ",\"userJSON\":" + userJSON + "}";
    }

    @RequestMapping(value = "/register", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public String register(@RequestParam(defaultValue = "") String uid,
                           @RequestParam(defaultValue = "") String password,
                           @RequestParam(defaultValue = "") String rePassword,
                           @RequestParam(defaultValue = "") String code) {
        String captchaCode = (String) session.getAttribute("captchaCode");
        if (!captchaCode.equals(code)) {//验证码不符
            return "-1";
        }
        if (!rePassword.equals(password)) {//两次密码不一致
            return "-2";
        }
        User user = new User();
        user.setUid(uid);
        user.setPassword(password);
        return userService.register(user);
    }

    @RequestMapping(value = "/updatePassword", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword) {
//        String uid = ((User) session.getAttribute("user")).getUid();
        return userService.updateUserPassword("123456789", oldPassword, newPassword);
    }
}
