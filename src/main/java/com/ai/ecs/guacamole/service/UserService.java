package com.ai.ecs.guacamole.service;


import com.ai.ecs.guacamole.mapper.UserMapper;
import com.ai.ecs.guacamole.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 处理用户
 *
 */

@Service("UserService")
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    public User findUserByPhone(String phone) {
        return userMapper.findUserByPhone(phone);
    }


}
