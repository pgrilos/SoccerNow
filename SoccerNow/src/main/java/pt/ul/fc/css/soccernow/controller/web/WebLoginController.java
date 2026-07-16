package pt.ul.fc.css.soccernow.controller.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebLoginController {

    @GetMapping("/web/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/web/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        // Mock: aceita qualquer username/password
        if (username == null || username.isBlank()) {
            model.addAttribute("erro", "Username obrigatório");
            return "login";
        }
        session.setAttribute("user", username);
        return "redirect:/web/menu";
    }

    @GetMapping("/web/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/web/login";
    }
}