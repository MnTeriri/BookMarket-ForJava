package com.example.bookmarket.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.bookmarket.util.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/api/photo")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class CaptchaController {
    @Autowired
    private HttpSession session;

    @RequestMapping("/captcha.jpg")
    public String getCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 5, 30);
        String code = captcha.getCode();//获取验证码字符串
        session.setAttribute("captchaCode", code);//验证码字符串放入到session
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        captcha.write(out);
        return ImageUtils.encodeImageString(out.toByteArray());//将图片转化成BASE64字符串
    }
}
