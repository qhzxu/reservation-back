package com.reservation.reservation_server.controller;

import com.reservation.reservation_server.config.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    public final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/set")
    public String setValue() {
        redisService.save("testKey", "hello redis");
        return "saved";
    }

    @GetMapping("/get")
    public Object getValue() {
        return redisService.get("testKey");
    }

    @GetMapping("/delete")
    public String deleteValue() {
        redisService.delete("testKey");
        return "deleted";
    }
}
