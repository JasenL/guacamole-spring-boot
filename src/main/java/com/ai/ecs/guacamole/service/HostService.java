package com.ai.ecs.guacamole.service;

import com.ai.ecs.guacamole.mapper.HostMapper;
import com.ai.ecs.guacamole.model.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/1/28 17:00
 */
@Service("hostService")
public class HostService {
    @Autowired
    private HostMapper hostDao;

    public List<Host> getHostList() {
        return hostDao.getHostList();
    }

    public Host getHostById(String id){
        return hostDao.getHostById(id);
    }
}
