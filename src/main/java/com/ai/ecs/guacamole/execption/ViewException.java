package com.ai.ecs.guacamole.execption;

import org.springframework.security.core.AuthenticationException;

public class ViewException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    public ViewException(String message) {
        super(message);
    }
}
