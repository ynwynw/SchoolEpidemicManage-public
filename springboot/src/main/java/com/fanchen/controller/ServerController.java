package com.fanchen.controller;

import com.fanchen.common.lang.Result;
import com.fanchen.entity.server.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
    public Result server() throws Exception {
        Server server = new Server();
        server.copyTo();
        return Result.succ(server);
    }

}
