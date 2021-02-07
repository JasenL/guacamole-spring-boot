package com.ai.ecs.guacamole.config.smscode;

import com.ai.ecs.guacamole.execption.ViewException;
import com.ai.ecs.guacamole.util.JedisClusterUtils;
import com.ai.ecs.guacamole.web.LoginController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SmsCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equalsIgnoreCase("/dqgac/login/mobile", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(new ServletWebRequest(httpServletRequest));
            } catch (ViewException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        String smsCodeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");
        String mobileInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mobile");
        String key = LoginController.SESSION_KEY_SMS_CODE + mobileInRequest;

        if (StringUtils.isBlank(smsCodeInRequest)) {
            throw new ViewException("验证码不能为空！");
        }

        if (!JedisClusterUtils.exists(key)) {
            throw new ViewException("验证码已过期！");
        }
        String code = JedisClusterUtils.get(key);

        if (!StringUtils.equalsIgnoreCase(code, smsCodeInRequest)) {
            JedisClusterUtils.del(key);
            throw new ViewException("验证码不正确！请重新发送验证码！");
        }

    }
}