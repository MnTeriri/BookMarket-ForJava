package com.example.bookmarketpassport.controller;

import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:27000/", allowCredentials = "true")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(defaultValue = "") String uid,
                        @RequestParam(defaultValue = "") String password,
                        @RequestParam(defaultValue = "") String code) {
        String captchaCode = (String) session.getAttribute("captchaCode");
        if (!captchaCode.equals(code)) {
            return "{\"respCode\": {\"code\":-1},\"userFlag\": 1}";
        }
        User user = new User();
        user.setUid(uid);
        user.setPassword(password);
        return userService.login(user);
    }
}
