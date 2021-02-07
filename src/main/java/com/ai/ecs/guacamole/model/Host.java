package com.ai.ecs.guacamole.model;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/1/28 17:35
 */
public class Host {
    String id;
    String protocol;
    String hostname;
    String port;
    String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port == null ? "" : port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
