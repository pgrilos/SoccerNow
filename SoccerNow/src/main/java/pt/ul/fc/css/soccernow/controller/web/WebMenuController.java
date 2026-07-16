package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebMenuController {
    @GetMapping("/web/menu")
    public String menu() {
        return "menu";
    }
}