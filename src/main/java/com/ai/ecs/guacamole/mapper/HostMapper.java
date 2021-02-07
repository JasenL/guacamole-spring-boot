package com.ai.ecs.guacamole.mapper;

import com.ai.ecs.guacamole.model.Host;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/1/28 16:49
 */
@Mapper
public interface HostMapper {

    List<Host> getHostList();

    Host getHostById(@Param("id") String id);
}
