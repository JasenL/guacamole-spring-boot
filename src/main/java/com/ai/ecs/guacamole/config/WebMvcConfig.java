package com.ai.ecs.guacamole.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * 视图配置类
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("login_page").setViewName("login");
        registry.addViewController("").setViewName("index");
        registry.addViewController("view").setViewName("view");
        registry.addViewController("smslogin").setViewName("smslogin");


    }

}
