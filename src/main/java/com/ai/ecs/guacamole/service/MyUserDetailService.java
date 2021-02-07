package com.ai.ecs.guacamole.service;

import com.ai.ecs.guacamole.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component("MyUserDetailService")
@Slf4j
public class MyUserDetailService implements UserDetailsService{



    @Autowired
    private UserService userService;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userService.findByUserName(s);//数据库查询操作
        if (user == null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        return user;

    }
}
