package com.ai.ecs.guacamole.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/2/5 18:09
 */
@Mapper
public interface ShortMessageMapper {
    void sendSmsMessage(Map<String, Object> smMap);
}
