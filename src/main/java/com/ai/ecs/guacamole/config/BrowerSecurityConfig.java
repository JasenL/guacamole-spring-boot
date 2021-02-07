package com.ai.ecs.guacamole.config;

import com.ai.ecs.guacamole.config.smscode.SmsAuthenticationConfig;
import com.ai.ecs.guacamole.config.smscode.SmsCodeFilter;
import com.ai.ecs.guacamole.handle.UserLoginAuthenticationFailureHandler;
import com.ai.ecs.guacamole.handle.UserLoginAuthenticationSuccessHandler;
import com.ai.ecs.guacamole.handle.UserLogoutSuccessHandler;
import com.ai.ecs.guacamole.service.MyUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {


    private final static BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MyUserDetailService myUserDetailService() {
        return new MyUserDetailService();
    }

    @Autowired
    private TelphoneFilter telphoneFilter;

    @Autowired
    private UserLoginAuthenticationFailureHandler userLoginAuthenticationFailureHandler;

    @Autowired
    private UserLoginAuthenticationSuccessHandler userLoginAuthenticationSuccessHandler;

    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;
    @Autowired
    private SmsCodeFilter smsCodeFilter;
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(smsAuthenticationConfig)
                .and().headers().frameOptions().sameOrigin()//设置弹出层
                .and()
                .authorizeRequests()
                .antMatchers("/static/**", "/code/sms", "/smslogin")//静态资源等不需要验证
                .permitAll()//不需要身份认证
                .anyRequest().authenticated()//其他路径必须验证身份
                .and().addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class).addFilterBefore(telphoneFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/smslogin")//登录页面
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler(userLoginAuthenticationFailureHandler)//验证失败处理
                .successHandler(userLoginAuthenticationSuccessHandler)//验证成功处理
                .permitAll()//登录页面不需要验证
                .and()
                .logout()
                .logoutSuccessHandler(userLogoutSuccessHandler)//登出处理
                .permitAll()
                .and()
                .csrf().disable().sessionManagement().maximumSessions(3).expiredUrl("/smslogin");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService()).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {

                return ENCODER.encode(charSequence);
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                if (!ENCODER.matches(charSequence, s)) {
//                    log.info("{}","密码对不上");
                } else {
//                    log.info("{}","密码OK");
                }


                return ENCODER.matches(charSequence, s);
            }
        });
    }

}
