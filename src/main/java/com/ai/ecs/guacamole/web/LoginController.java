package com.ai.ecs.guacamole.web;

import cn.hutool.core.map.MapUtil;
import com.ai.ecs.guacamole.service.ShortMessageService;
import com.ai.ecs.guacamole.service.UserService;
import com.ai.ecs.guacamole.util.JedisClusterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/2/5 11:02
 */
@RestController
@Slf4j
public class LoginController {

    public final static String SESSION_KEY_SMS_CODE = "SESSION_KEY_SMS_CODE_";
    public final static String SMS_FLAG = "SMS_FLAG_";
    @Autowired
    private UserService userService;
    @Autowired
    private ShortMessageService shortMessageService;

    @RequestMapping("/code/sms")
    public Map<String, Object> createSmsCode(HttpServletRequest request) {
        Map<String, Object> result = MapUtil.newHashMap();
        String mobile = request.getParameter("mobile");
        if (userService.findUserByPhone(mobile) == null) {
            log.error("用户{}不存在！", mobile);
            result.put("code", "-1");
            result.put("msg", "用户不存在！");
            return result;
        }
        if (JedisClusterUtils.exists(SMS_FLAG + mobile)) {
            result.put("code", "-1");
            result.put("msg", "请在1分钟后发送短信！");
            return result;
        }
        String code = RandomStringUtils.randomNumeric(6);
        log.info("号码{}生成验证码为{}。", mobile, code);
        JedisClusterUtils.set(SESSION_KEY_SMS_CODE + mobile, code, 60);
        JedisClusterUtils.set(SMS_FLAG + mobile, "1", 60);
        try {
            //新疆移动号码，下发短信
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("CHAN_ID", "E022");
            map.put("EPARCHY_CODE", "ZZZZ");
            map.put("BRAND_CODE", "G000");
            map.put("RECV_OBJECT", mobile);
            map.put("NOTICE_CONTENT", "【验证密码】您的验证码为" + code);
            shortMessageService.sendSmsMessage(map);
            result.put("msg", "短信发送成功！");
            result.put("code", "0");
        } catch (Exception e) {
            log.error("-----短信发送失败----" + e.toString());
            result.put("msg", "短信发送失败！");
            result.put("code", "-1");
        }

        return result;
    }


}

