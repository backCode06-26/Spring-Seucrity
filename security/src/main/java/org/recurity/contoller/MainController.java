package org.recurity.contoller;

import org.springframework.web.bind.annotation.GetMapping;

public class MainController {

    @GetMapping("/")
    public String main() {
        return "main";
    }
}
