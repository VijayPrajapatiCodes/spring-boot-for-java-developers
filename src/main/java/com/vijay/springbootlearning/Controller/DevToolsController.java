package com.vijay.springbootlearning.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevToolsController {
    @GetMapping("/devtools")
    public String devTools(){
        return "devTools  Auto Restart Working";
    }
}
