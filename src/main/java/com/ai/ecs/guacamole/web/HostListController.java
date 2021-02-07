package com.ai.ecs.guacamole.web;

import cn.hutool.core.map.MapUtil;
import com.ai.ecs.guacamole.model.Host;
import com.ai.ecs.guacamole.service.HostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/1/28 16:48
 */
@RestController
@RequestMapping("/host")
@Slf4j
public class HostListController {

    @Autowired
    private HostService hostService;

    @RequestMapping(value = "getHostList")
    public Map<String, Object> getHostList(
            HttpServletRequest request) {

        Map<String, Object> result = MapUtil.newHashMap();
        List<Host> hostList = hostService.getHostList();
        if (!hostList.isEmpty()) {
            result.put("RESULT_CODE", "0");
        }
        result.put("hostList", hostList);
        return result;
    }
}
