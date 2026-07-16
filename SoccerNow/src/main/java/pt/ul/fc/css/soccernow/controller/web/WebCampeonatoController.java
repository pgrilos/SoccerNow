package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.handlers.CampeonatoHandler;

@Controller
public class WebCampeonatoController {

    private final CampeonatoHandler campeonatoHandler;

    public WebCampeonatoController(CampeonatoHandler campeonatoHandler) {
        this.campeonatoHandler = campeonatoHandler;
    }

    @GetMapping("/web/campeonatos")
    public String buscarCampeonatos(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String equipa,
        @RequestParam(required = false) Integer realizados,
        @RequestParam(required = false) Integer porRealizar,
        Model model
    ) {
        model.addAttribute("campeonatos", campeonatoHandler.filtrarCampeonatos(nome, equipa, realizados, porRealizar));
        return "campeonatos";
    }
}