package com.botduel.backend.controller.match;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangs
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "pk/index.html";
    }
}

