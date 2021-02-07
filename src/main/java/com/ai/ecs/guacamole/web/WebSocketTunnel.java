package com.ai.ecs.guacamole.web;

import com.ai.ecs.guacamole.model.Host;
import com.ai.ecs.guacamole.model.User;
import com.ai.ecs.guacamole.service.HostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/1/15 17:59
 */
@ServerEndpoint(value = "/webSocket/{index}/{width}/{height}/{dpi}", subprotocols = "guacamole" )
@Component
@Slf4j
public class WebSocketTunnel extends GuacamoleWebSocketTunnelEndpoint {


    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        log.info("websocket连接建立中......");

        String width = session.getPathParameters().get("width");
        String height = session.getPathParameters().get("height");
        String dpi = session.getPathParameters().get("dpi");
        String index = session.getPathParameters().get("index");

        String username = session.getUserPrincipal().getName();
        String hostname = "";
        int port = ;
        HostService hostService = applicationContext.getBean(HostService.class);

        Host host = hostService.getHostById(index);
        if (host == null) {
            return null;
        }
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(host.getProtocol());
        if ("rdp".equals(host.getProtocol())) {
            configuration.setParameter("ignore-cert", "true");
            configuration.setParameter("width", width);
            configuration.setParameter("height", height);
            configuration.setParameter("dpi", dpi);
            /**
             * enable-drive = true
             * drive-path = '/yourpath'
             * create-drive-path = true
             */
            configuration.setParameter("enable-drive", "true");
            configuration.setParameter("drive-path", "");
            configuration.setParameter("create-drive-path", "true");
        }
        configuration.setParameter("hostname", host.getHostname());
        if (!host.getPort().isEmpty()) {
            configuration.setParameter("port", host.getPort());
        }
        if (!host.getUsername().isEmpty()) {
            configuration.setParameter("username",host.getUsername());
        }

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(hostname, port),
                configuration
        );

        GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
        log.info("websocket连接建立成功。");
        return tunnel;
    }
}
