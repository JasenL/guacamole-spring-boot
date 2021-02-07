package com.ai.ecs.guacamole.security;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密方法
 */
public class TestEncoder {
    String privateKey = "";
    String publicKey = "";

    /**
     * 用户密码加密
     */
    @Test
    public void encoder() {
        String password = "";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        String enPassword = encoder.encode(password);
        System.out.println(enPassword);
    }

    /**
     * 数据库加密
     *
     * @throws Exception
     */
    @Test
    public void passwordEncoder() throws Exception {
        String password = "";
        //密钥生成
//        String[] arr = genKeyPair(512);
//        System.out.println("privateKey:" + arr[0]);
//        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(privateKey, password));

    }
}
