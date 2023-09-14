package com.example.bookmarket.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.bookmarket.dao.IUserDao;
import com.example.bookmarket.model.User;
import com.example.bookmarket.service.IUserService;
import com.example.bookmarket.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;


@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public User searchUser(String uid) {
        User user = userDao.selectOne(new QueryWrapper<User>().eq("uid", uid));
        user = imageHandle(user);
        return user;
    }

    @Override
    public String updateUser(User user) {
        return null;
    }

    @Override
    public String updateUserPassword(String uid, String oldPassword, String newPassword) {
        User user = new User();
        user.setUid(uid);
        user.setPassword(oldPassword);
        String loginCode = login(user);
        if (loginCode.equals("1")) {//如果老密码正确
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
            String md5Password = DigestUtil.md5Hex(newPassword);
            updateWrapper.set("password", md5Password).eq("uid", uid);
            if (userDao.update(null, updateWrapper) == 1) {
                return "1";//修改密码成功
            } else {
                return "-1";//修改密码失败
            }
        }
        return loginCode;
    }

    @Override
    public String login(User user) {
        User selected = userDao.selectOne(new QueryWrapper<User>().eq("uid", user.getUid()));
        if (selected == null) {
            return "-2";//没有用户
        }
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        if (!selected.getPassword().equals(md5Password)) {
            return "-3";//密码错误
        }
        return "1";//登录成功
    }

    @Override
    public String register(User user) {
        if (userDao.selectOne(new QueryWrapper<User>().eq("uid", user.getUid())) != null) {
            return "-3";//注册失败（用户名存在）
        }
        user.setUname("未设置用户名");
        try {
            FileInputStream inputStream = new FileInputStream("F:\\Code\\IDEA\\BookMarket\\src\\main\\java\\com\\example\\bookmarket\\1.jpg");
            user.setImage(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (userDao.insert(user) == 1) {
            return "1";
        }
        return "-4";
    }

    private User imageHandle(User user) {
        byte[] image = user.getImage();
        String imageString = "";
        if (image != null) {
            user.setImage(null);
            imageString = ImageUtils.encodeImageString(image);
        }
        user.setImageString(imageString);
        return user;
    }
}
