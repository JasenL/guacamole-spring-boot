package com.ai.ecs.guacamole.service;


import com.ai.ecs.guacamole.mapper.ShortMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信发送
 */

@Service("ShortMessageService")
@Slf4j
public class ShortMessageService {

    @Autowired
    private ShortMessageMapper shortMessageMapper;

    public void sendSmsMessage(Map<String, Object> smMap) {
        shortMessageMapper.sendSmsMessage(smMap);
    }


}
