package com.velox.cloud.ms.rest.PeopleService.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api")
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;
 
    @PostConstruct
    public void init() {
    	System.out.println("appName = "  + appName);
    }
    
    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}