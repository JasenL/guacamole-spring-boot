package com.ai.ecs.guacamole.handle;

import com.ai.ecs.guacamole.common.JsonData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * 用户登出处理类
 */

@Component("UserLogoutSuccessHandler")
@Slf4j
public class UserLogoutSuccessHandler implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        JsonData jsonData = new JsonData(200,"退出成功");
        String json = new Gson().toJson(jsonData);
        log.info("{} Logout Success", authentication.getName());
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        out.write(json);
        out.flush();
        out.close();

    }
}
