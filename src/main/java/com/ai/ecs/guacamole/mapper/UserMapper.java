package com.ai.ecs.guacamole.mapper;

import com.ai.ecs.guacamole.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User findByUserName(@Param("userName") String userName);

    User findUserByPhone(@Param("phone") String phone);

}