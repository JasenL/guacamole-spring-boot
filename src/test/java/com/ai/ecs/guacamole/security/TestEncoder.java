package com.ai.ecs.guacamole.security;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密方法
 */
public class TestEncoder {
    String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAucLWrzYFZqDr0PmubTzOZdi6Gq6m28yBg5I63z9z42D8hB4k3IeevfTzifY0wEk4Fubz6tjY2cViJLULqmLa9wIDAQABAkAGiroMyZY/GwOzBFgEONP7+jnznpr5uqYFAC1al115cG2IbWnGOeUmIJy69FkHnmrys94zDzZQ+QGQzRFYos/BAiEA7mmWEGcKe+Xk3VpSX/zkuzakIbHPxL4CEK7uFERQQv8CIQDHdu7EozlSwiChGqrugAPtAFY+J5NaMD9D8wt/au+ACQIgY77jHMuvdDSzsvRmAGjS3Yy30K0O6xY/0PEPBIWsOssCIFI33ffD6C8KwfRmT6r7raKVjvcTzRNR6+IQXt7or0wxAiEAv9m9uDy2SPOAO2FGLfR59oCoIiQGEa1seLjUFKuQid8=";
    String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALnC1q82BWag69D5rm08zmXYuhquptvMgYOSOt8/c+Ng/IQeJNyHnr3084n2NMBJOBbm8+rY2NnFYiS1C6pi2vcCAwEAAQ==";

    /**
     * 用户密码加密
     */
    @Test
    public void encoder() {
        String password = "admin";
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
        String password = "W1upx2wl_8";
        //密钥生成
//        String[] arr = genKeyPair(512);
//        System.out.println("privateKey:" + arr[0]);
//        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(privateKey, password));

    }
}
