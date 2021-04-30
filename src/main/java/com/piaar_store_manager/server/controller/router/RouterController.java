package com.piaar_store_manager.server.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouterController {
    @GetMapping("/")
    public String HomePage(){
        return "index.html";
    }
}
