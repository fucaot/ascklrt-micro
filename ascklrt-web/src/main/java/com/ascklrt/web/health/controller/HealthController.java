package com.ascklrt.web.health.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author goumang
 * @description
 * @date 2023/2/13 16:42
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @RequestMapping("/check")
    public String check() {
        return "is health!";
    }
}
